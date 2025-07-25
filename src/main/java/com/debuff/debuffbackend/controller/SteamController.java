package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.common.Result;
import com.debuff.debuffbackend.entity.Items;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.ItemsService;
import com.debuff.debuffbackend.service.SteamService;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * Steam 控制器
 * 提供与 Steam 相关的 API 接口
 */
@RestController
@RequestMapping("/api/steam")
@Api(tags = "Steam库存接口")
@Slf4j
public class SteamController {


    
    private static final String OPENID_NS          = "http://specs.openid.net/auth/2.0";
    private static final String STEAM_OPENID_URL   = "https://steamcommunity.com/openid/login";
    private static final Pattern STEAM_ID_PATTERN  = Pattern.compile("https?://steamcommunity\\.com/openid/id/(\\d+)");

    private final SteamService   steamService;
    private final RestTemplate   restTemplate;
    private final UsersService   usersService;
    private final JwtUtils       jwtUtils;
    private final ItemsService   itemsService;


    @Value("${steam.callback.url}")
    private String steamCallbackUrl;
    
    /**
     * 获取当前用户的Steam库存
     * @return 包含库存物品列表的Result对象
     */
    @GetMapping("/inventory")
    @ApiOperation("获取当前用户Steam库存")
    public Result<List<Items>> getUserInventory() {
        log.info("===== 开始执行getUserInventory方法 =====");
        UserDetails userDetails = null;
        try {
            log.info("开始获取用户Steam库存");
            log.debug("当前请求路径: /api/steam/inventory");
            // 获取当前用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication.getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) authentication.getPrincipal();
                log.debug("用户主体类型：UserDetails，用户名={}", userDetails.getUsername());
            } else if (authentication.getPrincipal() instanceof String idStr) {
                log.debug("用户主体类型：String，用户ID字符串={}", idStr);
                if ("anonymousUser".equals(idStr)) {
                    log.error("获取Steam库存失败：用户未登录");
                    return Result.fail("用户未登录");
                }
                try {
                    Integer userId = Integer.parseInt(idStr);
                    Users u = usersService.getById(userId);
                    if (u == null) {
                        log.error("获取Steam库存失败：用户不存在，用户ID={}", userId);
                        return Result.fail("用户不存在");
                    }
                    userDetails = new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), Collections.emptyList());
                    log.debug("通过用户ID解析得到用户名：{}", userDetails.getUsername());
                } catch (NumberFormatException e) {
                    log.error("用户ID格式错误: {}", idStr, e);
                    return Result.fail("用户信息错误");
                }
            }
            
            if (userDetails == null) {
                log.error("获取Steam库存失败：用户未登录");
                log.debug("当前认证主体类型: {}", authentication.getPrincipal().getClass().getName());
                return Result.fail("用户未登录");
            }
            
            // 从数据库查询用户信息获取SteamID
            Users currentUser = null;
            // 使用用户ID查询用户
            try {
                Integer userId = Integer.parseInt(userDetails.getUsername());
                currentUser = usersService.getById(userId);
                if (currentUser == null) {
                    log.error("获取Steam库存失败：用户不存在，用户ID={}", userId);
                    return Result.fail("用户不存在");
                }
            } catch (NumberFormatException e) {
                // 如果解析失败，尝试使用用户名查询
                currentUser = usersService.getOne(lambdaQuery(Users.class).eq(Users::getUsername, userDetails.getUsername()));
                if (currentUser == null) {
                    log.error("获取Steam库存失败：用户不存在，用户名={}", userDetails.getUsername());
                    return Result.fail("用户不存在");
                }
            }
            
            if (currentUser.getSteamId() == null) {
                log.error("用户未绑定Steam账号，用户名: {}", userDetails.getUsername());
                return Result.fail("请先绑定Steam账号");
            }
            
            Result<List<Items>> inventoryResult = steamService.getSteamInventory(currentUser.getSteamId());
            log.info("获取用户Steam库存成功，物品数量: {}", inventoryResult.getData() != null ? inventoryResult.getData().size() : 0);
            return inventoryResult;
        } catch (Exception e) {
            log.error("获取Steam库存失败，用户名: {}", (userDetails != null ? userDetails.getUsername() : "未知用户"), e);
            return Result.fail("获取Steam库存失败: " + e.getMessage());
        }
    }


    /* 构造器注入，消除字段注入警告 */
    public SteamController(SteamService steamService,
                           RestTemplate restTemplate,
                           UsersService usersService,
                           JwtUtils jwtUtils,
                           ItemsService itemsService) {
        this.steamService = steamService;
        this.restTemplate = restTemplate;
        this.usersService = usersService;
        this.jwtUtils = jwtUtils;
        this.itemsService = itemsService;
    }

    /* 1. 生成登录 URL（供弹窗打开） */
    @GetMapping("/auth/login")
    @ApiOperation("重定向到 Steam 登录页面")
    public ResponseEntity<String> generateSteamLoginUrl() {
        log.info("===== 开始执行generateSteamLoginUrl方法 =====");
        log.info("开始生成Steam登录URL");
        log.debug("当前请求路径: /api/steam/auth/login");
        // 使用回调URL的域名作为realm，解决本地环境认证问题
        String realm = steamCallbackUrl.split("/")[0] + "//" + steamCallbackUrl.split("/")[2];
        String returnTo = steamCallbackUrl;
        log.debug("生成Steam登录URL参数 - realm={}, returnTo={}", realm, returnTo);
        log.info("Steam登录URL生成完成");

        // 仅一次编码
        String url = UriComponentsBuilder.fromUriString(STEAM_OPENID_URL)
                .queryParam("openid.ns", OPENID_NS)
                .queryParam("openid.mode", "checkid_setup")
                .queryParam("openid.return_to", returnTo)
                .queryParam("openid.realm", realm)
                .queryParam("openid.identity", OPENID_NS + "/identifier_select")
                .queryParam("openid.claimed_id", OPENID_NS + "/identifier_select")
                .toUriString();

        log.info("Steam登录URL生成成功，重定向URL={}", url);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("<html><head><script>window.location.href='" + url + "';</script></head></html>");
    }

    /* 2. 解绑 Steam */
    @DeleteMapping("/auth/unbind")
    @ApiOperation("解除 Steam 绑定")
    public ResponseEntity<String> unbindSteam() {
        log.info("===== 开始执行unbindSteam方法 =====");
        log.info("开始处理Steam解绑请求");
        log.debug("当前请求路径: /api/steam/auth/unbind");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = null;
        if (auth.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) auth.getPrincipal();
        }
        
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><script>alert('用户未登录');window.location.href='/login';</script></body></html>");
        }
        Users user = usersService.findByEmail(userDetails.getUsername());
        if (user == null) {
            log.warn("Steam解绑失败：用户不存在，用户名={}", userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><script>alert('用户不存在');window.location.href='/login';</script></body></html>");
        }
        user.setSteamId(null);
        log.debug("准备解绑用户Steam账号，用户ID={}", user.getUserId());
        boolean updateSuccess = usersService.updateById(user);
        if (updateSuccess) {
            log.info("用户Steam账号解绑成功，用户ID={}, 原SteamID={}", user.getUserId(), user.getSteamId());
            log.debug("解绑后用户信息: {}", user);
        } else {
            log.error("用户Steam账号解绑失败，用户ID={}", user.getUserId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><script>alert('解绑失败，请重试');window.location.href='/account-settings/profile';</script></body></html>");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("<html><body><script>alert('Steam 解绑成功');window.location.href='/account-settings/profile';</script></body></html>");
    }

    /* 4. 查询用户库存 */
    @GetMapping("/inventory/user/{steamId}")
    @ApiOperation("根据用户SteamID查询库存")
    /**
     * 根据SteamID获取用户库存
     * @param steamId Steam账号ID
     * @param userDetails 当前认证用户信息
     * @return 库存物品列表
     */
    public Result<List<Items>> getUserInventory(@PathVariable String steamId, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("===== 开始执行getUserInventory(steamId)方法 =====");
        log.info("开始查询SteamID为{}的用户库存", steamId);
        log.debug("当前请求路径: /api/steam/inventory/user/{}", steamId);
        Users user = usersService.findBySteamId(steamId);
        if (user == null) {
            log.warn("未找到SteamID为{}的用户", steamId);
            return Result.fail("用户不存在");
        }
        List<Items> inventoryItems = itemsService.getItemsByUserId(user.getUserId());
        log.debug("用户{}的库存物品列表: {}", user.getUserId(), inventoryItems);
        log.info("查询到用户{}的库存物品{}件", user.getUserId(), inventoryItems.size());
        return Result.success(inventoryItems);
    }

    /* 3. Steam 回调：登录 / 绑定完成后关闭弹窗 */
    @GetMapping("/auth/callback")
    @ApiOperation("处理 Steam 登录回调")
    public ResponseEntity<String> handleSteamCallback(HttpServletRequest request, @RequestParam Map<String, String> params,
                                                      HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            String currentUrl = request.getRequestURL().toString() + "?" + request.getQueryString();
            String encodedUrl = URLEncoder.encode(currentUrl, StandardCharsets.UTF_8);StandardCharsets.UTF_8.name();
            return ResponseEntity.status(HttpStatus.FOUND)
                    //.header("Location", "http://192.168.110.7:8082/login?returnUrl=" + encodedUrl)
                    .header("Location", "http://localhost:8082/login?returnUrl=" + encodedUrl)
                    .build();
        }
        log.info("接收到Steam登录回调请求，参数数量={}", params.size());
        log.debug("Steam回调参数：{} ", params);
        if ("error".equals(params.get("openid.mode"))) {
            log.warn("Steam回调返回错误状态：{} ", params.get("openid.error"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'Steam认证失败'},'*');window.close();</script></body></html>");
        }

        try {
            log.info("开始处理Steam回调验证逻辑");
            auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();
            UserDetails userDetails;
            if (principal instanceof UserDetails) {
                userDetails = (UserDetails) principal;
                log.debug("用户主体类型：UserDetails，用户名={}", userDetails.getUsername());
            } else if (principal instanceof String idStr) {
                log.debug("用户主体类型：String，用户ID字符串={}", idStr);
                if ("anonymousUser".equals(idStr)) {
                    throw new AccessDeniedException("请先登录系统再进行Steam绑定操作");
                }
                Users u = usersService.getById(Integer.parseInt(idStr));
                if (u == null) throw new UsernameNotFoundException("用户不存在");
                userDetails = new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), Collections.emptyList());
            } else {
                throw new IllegalStateException("无法识别的用户主体");
            }

            // 用户ID存储在username字段中，需要转换为整数
            Integer userId = Integer.parseInt(userDetails.getUsername());
            Users currentUser = usersService.getById(userId);
            log.debug("根据用户ID查询结果：{} ", currentUser != null ? "找到用户，ID=" + currentUser.getUserId() : "用户不存在");
            if (currentUser == null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'用户不存在'},'*');window.close();</script></body></html>");
            }

            // Steam 验证
            log.info("开始执行Steam OpenID验证");
            Map<String, String> validationParams = new HashMap<>();
            validationParams.put("openid.ns", OPENID_NS);
            String signed = params.get("openid.signed");
            if (!StringUtils.hasText(signed)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'缺少签名'},'*');window.close();</script></body></html>");
            }
            for (String key : signed.split(",")) {
                validationParams.put("openid." + key, params.get("openid." + key));
            }
            // 添加签名参数
            validationParams.put("openid.sig", params.get("openid.sig"));
            validationParams.put("openid.mode", "check_authentication");

            log.debug("发送Steam验证POST请求到：{} ", STEAM_OPENID_URL);
            // 创建POST请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            // 构建表单参数
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            validationParams.forEach(formData::add);
            log.debug("Steam验证POST参数: {} ", formData);
            
            // 发送POST请求
            HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(formData, headers);
            String steamResp = restTemplate.postForObject(STEAM_OPENID_URL, httpRequest, String.class);
            log.debug("Steam验证响应：{} ", steamResp);
            boolean isValid = steamResp != null && steamResp.contains("is_valid:true");
            if (!isValid) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'Steam验证失败'},'*');window.close();</script></body></html>");
            }

            // 提取 SteamID
            String identity = params.get("openid.identity");
            Matcher matcher = STEAM_ID_PATTERN.matcher(identity == null ? "" : identity);
            log.debug("尝试从identity提取SteamID：{} ", identity);
            if (!matcher.find()) {
                log.error("无法从identity提取SteamID：{} ", identity);
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'无法解析SteamID'},'*');window.close();</script></body></html>");
            }
            String steamId = matcher.group(1);
            log.info("成功提取SteamID：{} ", steamId);

            // 检查是否已绑定他人
            log.debug("检查SteamID是否已绑定其他用户：{} ", steamId);
            Users existing = usersService.findBySteamId(steamId);
            if (existing != null && !existing.getUserId().equals(currentUser.getUserId())) {
                log.warn("SteamID已绑定其他用户，SteamID={}, 当前用户ID={}, 已绑定用户ID={}", steamId, currentUser.getUserId(), existing.getUserId());
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'该Steam账号已绑定其他用户'},'*');window.close();</script></body></html>");
            }

            // 绑定
            currentUser.setSteamId(steamId);
            log.debug("准备绑定SteamID到用户，用户ID={}, SteamID={}", currentUser.getUserId(), steamId);
            boolean updateSuccess = usersService.updateById(currentUser);
            if (updateSuccess) {
                log.info("用户SteamID绑定成功，用户ID={}, SteamID={}", currentUser.getUserId(), steamId);
            } else {
                log.error("用户SteamID绑定失败，用户ID={}, SteamID={}", currentUser.getUserId(), steamId);
                throw new RuntimeException("更新用户SteamID失败");
            }

            // 生成 JWT
            log.debug("为用户生成JWT令牌，用户ID={}", currentUser.getUserId());
            String token = jwtUtils.generateToken(currentUser.getUserId());
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            // 通知父窗口并关闭弹窗
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body("""
                          <html>
                            <body>
                              <script>
                                try {
                                  window.opener.postMessage({type:'steam-ok',steamId:'%s'},'*');
                                  window.close();
                                } catch(e){
                                  console.error(e);
                                  document.body.innerHTML='<div style="text-align:center;padding:20px"><h3>绑定成功</h3><p>如果窗口未自动关闭，请手动关闭。</p></div>';
                                }
                              </script>
                            </body>
                          </html>
                          """.formatted(steamId));
        } catch (Exception e) {
            log.error("处理 Steam 回调失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><script>window.opener.postMessage({type:'steam-bind-error',msg:'服务器异常'},'*');window.close();</script></body></html>");
        }
    }

    /* 4. 获取库存 */
    @GetMapping({"/inventory/{steamId}", "/inventory/{steamId}"})
    /**
     * 获取用户库存物品列表
     * 根据SteamID查询并返回用户的库存物品
     * @param steamId Steam用户唯一标识
     * @return 包含库存物品列表的响应实体
     */
    @ApiOperation("获取 Steam 用户库存")
    public ResponseEntity<Result<List<Items>>> getSteamInventory(@PathVariable String steamId) {
        log.info("===== 开始执行getSteamInventory方法 =====");
        log.info("开始处理获取Steam用户库存请求，steamId={}", steamId);
        log.debug("当前请求路径: /api/steam/inventory/{}", steamId);
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth.getPrincipal() instanceof UserDetails userDetails)) {
                log.warn("获取库存失败：用户未登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Result.fail("请先登录系统"));
            }

            // 用户ID存储在username字段中，需要转换为整数
            Integer userId = Integer.parseInt(userDetails.getUsername());
            log.debug("从认证信息中解析到用户ID：{}", userId);
            log.info("当前操作用户ID: {}", userId);
            
            Users user = usersService.getById(userId);
            if (user == null) {
                log.warn("获取库存失败：用户不存在，userId={}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Result.fail("用户不存在"));
            }

            // 验证用户的SteamID是否匹配
            if (!steamId.equals(user.getSteamId())) {
                log.warn("获取库存失败：SteamID不匹配，用户绑定的SteamID={}，请求的SteamID={}", user.getSteamId(), steamId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Result.fail("无权访问该用户库存"));
            }

            // 调用Service层方法获取用户库存物品
            List<Items> items = itemsService.getItemsByUserId(userId);
            log.debug("用户{}的库存物品详情: {}", userId, items);
            log.info("成功获取用户库存，userId={}，物品数量={}", userId, items.size());
            return ResponseEntity.ok(Result.success(items));
        } catch (Exception e) {
            log.error("获取 Steam 库存失败，steamId={}", steamId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}