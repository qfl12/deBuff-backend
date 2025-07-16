package com.debuff.debuffbackend.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

/**
 * RestTemplate HTTPS 配置类
 *
 * 使用 HttpClient 5.x 配置支持HTTPS的RestTemplate
 * 注意：此配置会跳过SSL证书验证，仅限开发测试环境使用
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建支持HTTPS的RestTemplate实例
     *
     * @return 配置好的RestTemplate
     * @throws NoSuchAlgorithmException 当SSLContext获取失败时抛出
     * @throws KeyManagementException 当SSL上下文初始化失败时抛出
     */
    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        // ==================== 1. 配置SSL上下文 ====================
        // 创建信任所有证书的SSL上下文（生产环境不应使用此方式）
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // 空实现表示信任所有客户端证书
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // 空实现表示信任所有服务端证书
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // 返回空数组表示接受所有证书颁发机构
                    return new X509Certificate[0];
                }
            }
        }, null);

        // ==================== 2. 创建SSL套接字工厂 ====================
        // 参数说明：
        // sslContext - 自定义的SSL上下文
        // NoopHostnameVerifier.INSTANCE - 禁用主机名验证（不安全）
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
            sslContext,
            NoopHostnameVerifier.INSTANCE);

        // ==================== 3. 配置HttpClient ====================
        // HttpClient 5.x 需要通过连接管理器设置SSL
        HttpClient httpClient = HttpClients.custom()
            .setConnectionManager(
                PoolingHttpClientConnectionManagerBuilder.create()
                    // 设置SSL套接字工厂（关键步骤）
                    .setSSLSocketFactory(socketFactory)
                    .build()
            )
            .build();

        // ==================== 4. 配置请求工厂 ====================
        HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory(httpClient);

        // 设置连接超时（5秒）
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        // 设置读取超时（10秒）
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        // ==================== 5. 创建RestTemplate ====================
        return new RestTemplate(requestFactory);
    }
}