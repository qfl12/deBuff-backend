package com.debuff.debuffbackend.service.impl;

import com.alibaba.fastjson.JSONObject;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    @Autowired
    private UsersService usersService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ItemsMapper itemsMapper;


    @Override
    public Map<String, Object> purchaseItem(Long itemId, Integer buyerId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 获取商品信息
            MarketListing item = marketListingMapper.selectByItemId(itemId);
            log.info("Item: {}", item);
            if (item == null || !"ON_SALE".equals(item.getStatus())) {
                result.put("success", false);
                result.put("message", "商品不存在或已售出");
                return result;
            }

            Items items = itemsMapper.selectById(itemId);

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

            // 4. 创建交易记录
            Tradeconfirmations trade = new Tradeconfirmations();
            trade.setOrderId(System.currentTimeMillis());
            trade.setBuyerId(buyerId);
            trade.setSellerId(item.getUserId());
            trade.setName(items.getName());
            trade.setPaymentConfirmed(0);
            trade.setShippingConfirmed(0);
            trade.setTradeCompleted(0);
            this.save(trade);

            // 5. 调用Steam API创建交易报价
            JSONObject steamRequest = new JSONObject();
            // 获取买家的steamloginsecure凭证
            String buyerSteamLogin = buyer.getSteamId();
            if (buyerSteamLogin == null || buyerSteamLogin.isEmpty()) {
                throw new RuntimeException("用户未绑定Steam账号");
            }
            steamRequest.put("steamloginsecure", buyerSteamLogin);
            steamRequest.put("partnersteamid", buyer.getSteamId());
            steamRequest.put("tradelink", buyer.getTradeLink());
            steamRequest.put("myitemassetids", items.getInstanceid());
            steamRequest.put("message", "通过Debuff平台购买的商品");
            steamRequest.put("game", "cs2");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String fullCreateUrl = steamCreateTradeUrl + "?key=" + steamApiKey;
            HttpEntity<String> request = new HttpEntity<>(steamRequest.toString(), headers);
            JSONObject steamResponse = restTemplate.postForObject(fullCreateUrl, request, JSONObject.class);

            if (steamResponse.containsKey("tradeofferid")) {
                // 6. 发送邮件通知卖家
                Users seller = usersMapper.selectById(buyer.getUserId());
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
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int checkCount = 0;

            @Override
            public void run() {
                // 最多检查30次（5分钟）
                if (checkCount >= 30) {
                    timer.cancel();
                    return;
                }

                try {
                    // 调用Steam API检查交易状态
                    String checkUrl = steamCheckTradeUrl + "?key=" + steamApiKey + "&tradeofferid=" + tradeOfferId;
                    JSONObject statusResponse = restTemplate.getForObject(checkUrl, JSONObject.class);

                    if ("traded".equals(statusResponse.getString("status"))) {
                        // 交易成功
                        Tradeconfirmations trade = getById(confirmationId);
                        trade.setTradeCompleted(1);
                        updateById(trade);

                        // 给卖家加钱
                        Users seller = usersMapper.selectById(sellerId);
            seller.setBalance(seller.getBalance().add(price));
            usersMapper.updateById(seller);

                        // 发送邮件通知买家和卖家
                        Users buyer = usersMapper.selectById(buyerId);
                        emailService.sendTradeSuccessEmail(buyer.getEmail(), statusResponse.getString("tradeofferid"));
                        Users sellerUser = usersMapper.selectById(sellerId);
                        emailService.sendTradeSuccessEmail(sellerUser.getEmail(), statusResponse.getString("tradeofferid"));

                        timer.cancel();
                    } else if ("reversed".equals(statusResponse.getString("status"))) {
                        // 交易被撤销，回滚买家余额
                        Tradeconfirmations trade = getById(confirmationId);
                        trade.setTradeCompleted(-1);
                        updateById(trade);
                        Users buyer = usersMapper.selectById(buyerId);
buyer.setBalance(buyer.getBalance().add(price));
usersMapper.updateById(buyer);
                        // 发送邮件通知买家交易已撤销
                        buyer = usersMapper.selectById(buyerId);
                        emailService.sendTradeReversedEmail(buyer.getEmail(), statusResponse.getString("tradeofferid"));
                        timer.cancel();
                    } else if ("failed".equals(statusResponse.getString("status"))) {
                        // 交易失败，回滚买家余额
                        Tradeconfirmations trade = getById(confirmationId);
                        trade.setTradeCompleted(-2);
                        updateById(trade);
                        Users buyer = usersMapper.selectById(buyerId);
buyer.setBalance(buyer.getBalance().add(price));
usersMapper.updateById(buyer);
                        // 发送邮件通知买家交易失败
                        buyer = usersMapper.selectById(buyerId);
                        emailService.sendTradeFailedEmail(buyer.getEmail(), statusResponse.getString("tradeofferid"));
                        timer.cancel();
                    } else if ("reversed".equals(statusResponse.getString("status"))) {
                        // 交易被撤销，回滚
                        Tradeconfirmations trade = getById(confirmationId);
                        trade.setTradeCompleted(-1);
                        updateById(trade);
                        usersService.addBalance(buyerId, price);
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




