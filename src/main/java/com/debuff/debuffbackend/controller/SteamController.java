package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.SteamService;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.utils.JwtUtils;
import com.debuff.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Steam 控制器
 * 提供与 Steam 相关的 API 接口
 */
@RestController
@RequestMapping("/api/steam")
@Api(tags = "Steam 接口")
public class SteamController {

    private static final Logger log = LoggerFactory.getLogger(SteamController.class);

    private static final String OPENID_NS          = "http://specs.openid.net/auth/2.0";
    private static final String STEAM_OPENID_URL   = "https://steamcommunity.com/openid/login";
    private static final Pattern STEAM_ID_PATTERN  = Pattern.compile("https?://steamcommunity\\.com/openid/id/(\\d+)");

    private final SteamService   steamService;
    private final RestTemplate   restTemplate;
    private final UsersService   usersService;
    private final JwtUtils       jwtUtils;


    @Value("${steam.callback.url}")
    private String steamCallbackUrl;

    /* 构造器注入，消除字段注入警告 */
    public SteamController(SteamService steamService,
                           RestTemplate restTemplate,
                           UsersService usersService,
                           JwtUtils       jwtUtils) {
        this.steamService = steamService;
        this.restTemplate = restTemplate;
        this.usersService = usersService;
        this.jwtUtils = jwtUtils;
    }

    /* 1. 生成登录 URL（供弹窗打开） */
    @GetMapping("/auth/login")
    @ApiOperation("重定向到 Steam 登录页面")
    public ResponseEntity<String> generateSteamLoginUrl() {
        log.info("开始生成Steam登录URL");
        // 使用回调URL的域名作为realm，解决本地环境认证问题
        String realm = steamCallbackUrl.split("/")[0] + "//" + steamCallbackUrl.split("/")[2];
        String returnTo = steamCallbackUrl;
        log.debug("生成Steam登录URL参数 - realm={}, returnTo={}", realm, returnTo);

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
        log.info("开始处理Steam解绑请求");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetails userDetails)) {
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
            log.info("用户Steam账号解绑成功，用户ID={}", user.getUserId());
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
    @GetMapping("/inventory/{steamId}")
    @ApiOperation("获取 Steam 用户库存")
    public ResponseEntity<Result<Object>> getSteamInventory(@PathVariable String steamId) {
        log.info("获取Steam用户库存请求，steamId={}", steamId);
        try {
            return ResponseEntity.ok(steamService.getSteamInventory(steamId));
        } catch (Exception e) {
            log.error("获取 Steam 库存失败，steamId={}", steamId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}