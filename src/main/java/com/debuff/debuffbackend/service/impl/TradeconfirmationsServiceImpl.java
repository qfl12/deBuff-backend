package com.debuff.debuffbackend.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Items;
import com.debuff.debuffbackend.entity.MarketListing;
import com.debuff.debuffbackend.entity.Tradeconfirmations;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.mapper.ItemsMapper;
import com.debuff.debuffbackend.mapper.MarketListingMapper;
import com.debuff.debuffbackend.mapper.TradeconfirmationsMapper;
import com.debuff.debuffbackend.mapper.UsersMapper;
import com.debuff.debuffbackend.service.EmailService;
import com.debuff.debuffbackend.service.TradeconfirmationsService;
import com.debuff.debuffbackend.service.UsersService;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TradeconfirmationsServiceImpl extends ServiceImpl<TradeconfirmationsMapper, Tradeconfirmations>
    implements TradeconfirmationsService{
    private static final Logger log = LoggerFactory.getLogger(TradeconfirmationsServiceImpl.class);

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MarketListingMapper marketListingMapper;

    @Autowired
    private EmailService emailService;



    @Value("${steam.api.key}")
    private String steamApiKey;

    @Value("${steam.api.create_trade_url}")
    private String steamCreateTradeUrl;

    @Value("${steam.api.check_trade_url}")
    private String steamCheckTradeUrl;

    @Value("${steam.api.trade_history_url}")
    private String steamTradeHistoryUrl;

    @Value("${steam.api.webhook}")
    private String webhookUrl;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ItemsMapper itemsMapper;


    @Override
    public List<Tradeconfirmations> getUserTradeRecords(Integer userId, String status) {
        QueryWrapper<Tradeconfirmations> queryWrapper = new QueryWrapper<>();
        // 查询当前用户作为买家或卖家的交易
        queryWrapper.eq("buyer_id", userId).or().eq("seller_id", userId);
        
        // 根据状态筛选
        if ("TRADING".equals(status)) {
            queryWrapper.eq("trade_completed", 0);
        } else if ("COMPLETED".equals(status)) {
            queryWrapper.eq("trade_completed", 1);
        } else if ("CANCELED".equals(status)) {
            queryWrapper.in("trade_completed", -1, -2);
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");

        // 根据状态选择不同的查询方式
        if ("COMPLETED".equals(status)) {
            // 对于已完成订单，使用默认查询
            return baseMapper.selectList(queryWrapper);
        } else {
            // 其他状态使用关联查询
            return baseMapper.selectUserTradeRecordsWithItems(queryWrapper);
        }
    }

    @Override
    public Map<String, Object> purchaseItem(Long itemId, Integer buyerId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 记录购买请求参数
            log.info("Received purchase request - itemId: {}, buyerId: {}", itemId, buyerId);
            // 1. 获取商品信息
            MarketListing item = marketListingMapper.selectByItemId(itemId);
            log.info("Item: {}", item);
            if (item == null || !"ON_SALE".equals(item.getStatus())) {
                result.put("success", false);
                result.put("message", "商品不存在或已售出");
                return result;
            }

            Items items = itemsMapper.selectById(item.getItemId());
            log.info("Items查询结果: {}", items);

            Users users = usersMapper.selectById(items.getUserId());

            // 2. 检查买家余额
            Users buyer = usersMapper.selectById(buyerId);
            if (buyer.getBalance().compareTo(item.getPrice()) < 0) {
                result.put("success", false);
                result.put("message", "余额不足，无法购买");
                return result;
            }

            // 3. 扣减买家余额
            buyer.setBalance(buyer.getBalance().subtract(item.getPrice()));
            usersMapper.updateById(buyer);


            // 4. 调用Steam API创建交易报价
            JSONObject steamRequest = new JSONObject();
            // 获取买家的steamloginsecure凭证
            String buyerSteamLogin = buyer.getApiKey();
            if (buyerSteamLogin == null || buyerSteamLogin.isEmpty()) {
                throw new RuntimeException("用户未绑定Steam账号");
            }
            steamRequest.put("steamloginsecure", buyerSteamLogin);
            steamRequest.put("partnersteamid", users.getSteamId());
            log.info("partneritemassetids: {}", items.getAssetid());
            steamRequest.put("partneritemassetids", String.valueOf(items.getAssetid()));
            steamRequest.put("tradelink", buyer.getTradeLink());
            steamRequest.put("message", "通过Debuff平台购买的商品");
            steamRequest.put("game", "cs2");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String fullCreateUrl = steamCreateTradeUrl + "?key=" + steamApiKey;
            HttpEntity<String> request = new HttpEntity<>(steamRequest.toString(), headers);
            // 记录发起报价请求详情
            log.info("发起Steam交易报价请求 - URL: {}", fullCreateUrl);
            log.info("报价请求参数: {}", steamRequest.toString());
            JSONObject steamResponse = restTemplate.postForObject(fullCreateUrl, request, JSONObject.class);
            log.info("Steam报价响应结果: {}", steamResponse);

            // 5. 创建交易记录
            Tradeconfirmations trade = new Tradeconfirmations();
            trade.setOrderId((item.getId()));
            trade.setBuyerId(buyerId);
            trade.setSellerId(item.getUserId());
            trade.setName(items.getName());
            trade.setPaymentConfirmed(0);
            trade.setShippingConfirmed(0);
            trade.setTradeCompleted(0);
            // 设置Steam交易报价ID
            trade.setTradeofferid(Long.valueOf(steamResponse.getString("tradeofferid")));
            this.save(trade);

            if (steamResponse.containsKey("tradeofferid")) {
                // 6. 发送邮件通知卖家
                Users seller = usersMapper.selectById(users.getUserId());
                emailService.sendTradeOfferEmail(seller.getEmail(), items.getName(), steamResponse.getString("tradeofferid"));

                // 7. 启动定时任务检查交易状态
                startTradeStatusChecker(Long.valueOf(trade.getConfirmationId()), steamResponse.getString("tradeofferid"), buyerId, item.getUserId(), itemId, item.getPrice());

                result.put("success", true);
                result.put("orderId", trade.getOrderId());
                result.put("message", "购买请求已提交");
            } else {
                // 交易创建失败，回滚余额
                buyer.setBalance(buyer.getBalance().add(item.getPrice()));
                usersMapper.updateById(buyer);
                result.put("success", false);
                result.put("message", "Steam交易创建失败: " + steamResponse.getString("error"));
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "购买处理失败: " + e.getMessage());
        }

        return result;
    }

    private void startTradeStatusChecker(Long confirmationId, String tradeOfferId, Integer buyerId, Integer sellerId, Long itemId, BigDecimal price) {
        // 检查买家和卖家是否为同一人
        if (buyerId.equals(sellerId)) {
            log.error("买家和卖家不能为同一人，购买请求被拒绝。买家ID: {}", buyerId);
            throw new ServiceException("买家和卖家不能为同一人");
        }
        
        // 获取买家的steamloginsecure凭证
        Users seller = usersMapper.selectById(sellerId);
        Users buyer = usersMapper.selectById(buyerId);
        if (seller == null || seller.getApiKey() == null || seller.getApiKey().isEmpty()) {
            log.error("无法获取买家的Steam登录凭证，买家ID: {}", buyerId);
            throw new ServiceException("用户未绑定Steam账号或凭证已过期");
        }
        String steamLoginSecure = seller.getApiKey();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int checkCount = 0;
String lastAfterTime = null;

            @Override
            public void run() {
                // 最多检查30次（5分钟）
                if (checkCount >= 30) {
                    timer.cancel();
                    return;
                }

                try {
                    // 调用Steam API检查交易状态
                    // 构建交易历史API请求URL
                    Items items = itemsMapper.selectById(itemId);
String historyUrl = steamTradeHistoryUrl + "?key=" + steamApiKey + "&assetid=" + items.getAssetid();
log.info("itemId的值: {}", itemId);
log.info("调用Steam交易历史API - URL: {}", historyUrl);

// 构建JSON请求体
JSONObject requestBody = new JSONObject();
requestBody.put("steamloginsecure", steamLoginSecure);
requestBody.put("webhook", webhookUrl);
// 添加分页参数
if (checkCount > 0 && lastAfterTime != null) {
    requestBody.put("after_time", lastAfterTime);
    log.info("使用分页参数获取后续交易: after_time={}", lastAfterTime);
} else {
    requestBody.put("after_time", System.currentTimeMillis() / 1000 - 86400); // 首次请求检查最近24小时内的交易
}

HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);
// 添加请求头日志
log.info("Steam交易历史API请求头: {}", headers);
log.info("Steam交易历史API请求体: {}", requestBody);
HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(historyUrl, requestEntity, JSONObject.class);
// 记录完整响应
log.info("Steam交易历史API完整响应: {}", responseEntity);
log.info("Steam交易状态检查API响应状态码: {}", responseEntity.getStatusCode());
if (!responseEntity.getStatusCode().is2xxSuccessful()) {
    log.error("Steam API请求失败，状态码: {}, 响应体: {}", responseEntity.getStatusCode(), responseEntity.getBody());
    // 处理特定错误码
    if (responseEntity.getStatusCode().value() == 401) {
        throw new ServiceException("Steam登录凭证无效或已过期，请重新绑定Steam账号");
    } else if (responseEntity.getStatusCode().value() == 429) {
        throw new ServiceException("请求过于频繁，请稍后再试");
    } else if (responseEntity.getStatusCode().value() == 405) {
        throw new ServiceException("API请求方法错误，请检查接口配置");
    } else {
        throw new ServiceException("获取交易状态失败: " + responseEntity.getStatusCode());
    }
}

JSONObject responseBody = responseEntity.getBody();
if (responseBody == null) {
    log.error("Steam交易历史API响应为空");
    throw new ServiceException("获取交易状态失败: 响应为空");
}
log.info("Steam交易状态检查API响应内容: {}", responseBody);

// 更新分页参数
lastAfterTime = responseBody.getString("nexthistoryaftertimestamp");
log.info("获取下一页交易的after_time: {}", lastAfterTime);

// 解析交易历史响应（正确逻辑）
JSONArray data = responseBody.getJSONArray("data"); // data 是交易数组
if (data == null) {
    log.error("Steam交易历史API响应格式错误: data字段不存在");
    throw new ServiceException("获取交易状态失败: 响应格式错误");
}
log.info("交易数据数组长度: {}", data.size());

// 处理空数组（无交易数据）
if (data.isEmpty()) {
    log.info("当前无交易数据，继续等待...（检查次数: {}）", checkCount);
    return;
}

// 遍历数组寻找目标交易（按 tradeOfferId 匹配）
JSONObject targetTrade = null;
for (int i = 0; i < data.size(); i++) {
    JSONObject tradeItem = data.getJSONObject(i);
    String itemTradeOfferId = tradeItem.getString("tradeofferid");
    if (tradeOfferId.equals(itemTradeOfferId)) { // 匹配当前检查的交易ID
        targetTrade = tradeItem;
        break;
    }
}

// 未找到目标交易，继续检查
if (targetTrade == null) {
    log.info("未找到目标交易（tradeOfferId: {}），继续等待...", tradeOfferId);
    return;
}

// 从目标交易中获取状态和退货信息
String tradeStatus = targetTrade.getString("status");
JSONObject tradeReturned = targetTrade.getJSONObject("tradereturned");
log.info("目标交易状态: {}, 退货信息: {}", tradeStatus, tradeReturned);

// 获取买家SteamID并匹配
String buyerSteamId = buyer.getSteamId();
boolean isBuyerMatch = false;
String participantSteamId = targetTrade.getString("participantsteamid");
if (buyerSteamId.equals(participantSteamId)) {
    isBuyerMatch = true;
    log.info("买家SteamID匹配: {}", buyerSteamId);
}

// 交易状态判断逻辑（全部基于目标交易对象）
if ("traded".equals(tradeStatus) && (tradeReturned == null || !tradeReturned.getBoolean("tradeReversed"))) {
    // 交易成功逻辑
    Tradeconfirmations trade = getById(confirmationId);
    trade.setTradeCompleted(1);
    if (isBuyerMatch) {
        trade.setShippingConfirmed(1);
    }
    updateById(trade);

    // 卖家加钱
    Users seller = usersMapper.selectById(sellerId);
    seller.setBalance(seller.getBalance().add(price));
    usersMapper.updateById(seller);

    // 删除marketListing中的商品记录
    // 使用QueryWrapper按item_id删除市场列表记录
QueryWrapper<MarketListing> queryWrapper = new QueryWrapper<>();
queryWrapper.eq("item_id", itemId);
int deletedRows = marketListingMapper.delete(queryWrapper);
log.info("交易完成，已从marketListing表中删除{}条记录，itemId: {}", deletedRows, itemId);
    

    // 发送邮件（使用目标交易的 tradeofferid）
    String targetTradeOfferId = targetTrade.getString("tradeofferid");
    emailService.sendTradeSuccessEmail(buyer.getEmail(), targetTradeOfferId);
    emailService.sendTradeSuccessEmail(seller.getEmail(), targetTradeOfferId);

    timer.cancel();

} else if ("reversed".equals(tradeStatus) || (tradeReturned != null && tradeReturned.getBoolean("tradeReversed"))) {
    // 交易撤销逻辑
    Tradeconfirmations trade = getById(confirmationId);
    trade.setTradeCompleted(-1);
    updateById(trade);

    // 买家余额回滚
    buyer.setBalance(buyer.getBalance().add(price));
    usersMapper.updateById(buyer);

    String targetTradeOfferId = targetTrade.getString("tradeofferid");
    emailService.sendTradeReversedEmail(buyer.getEmail(), targetTradeOfferId);
    timer.cancel();

} else if ("failed".equals(tradeStatus)) {
    // 交易失败逻辑
    Tradeconfirmations trade = getById(confirmationId);
    trade.setTradeCompleted(-2);
    updateById(trade);

    // 买家余额回滚
    buyer.setBalance(buyer.getBalance().add(price));
    usersMapper.updateById(buyer);

    String targetTradeOfferId = targetTrade.getString("tradeofferid");
    emailService.sendTradeFailedEmail(buyer.getEmail(), targetTradeOfferId);
    timer.cancel();
}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                checkCount++;
            }
        }, 0, 10000); // 立即开始，每10秒检查一次
    }
}




