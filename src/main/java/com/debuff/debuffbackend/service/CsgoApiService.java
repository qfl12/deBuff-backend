package com.debuff.debuffbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * CSGO API服务类，用于与本地CSGO-API-main服务通信
 * 提供获取游戏物品数据的功能
 */
@Service
@Slf4j
public class CsgoApiService {

    @Value("${csgo.api.base-url:http://localhost:3001/api}")
    private String csgoApiBaseUrl;

    @Value("${csgo.api.timeout:5000}")
    private int timeout;

    /**
     * 获取指定语言的皮肤数据
     * @param lang 语言代码，如zh-CN、en、ja等
     * @return 皮肤数据JSON字符串
     * @throws IOException 当HTTP请求失败或响应处理异常时抛出
     */
    public String getSkinsData(String lang) throws IOException {
        String url = csgoApiBaseUrl + "/" + lang + "/skins.json";
        log.info("开始请求CSGO皮肤数据，URL: {}", url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    log.error("请求CSGO皮肤数据失败，状态码: {}", statusCode);
                    throw new IOException("Failed to fetch CSGO skins data, status code: " + statusCode);
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    log.error("CSGO皮肤数据响应为空");
                    throw new IOException("CSGO skins data response is empty");
                }

                String result = null;
                try {
                    result = EntityUtils.toString(entity, "UTF-8");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                log.info("成功获取CSGO皮肤数据，长度: {} 字符", result.length());
                return result;
            }
        } catch (IOException e) {
            log.error("获取CSGO皮肤数据异常", e);
            throw e;
        }
    }

    /**
     * 获取指定语言的贴纸数据
     * @param lang 语言代码，如zh-CN、en、ja等
     * @return 贴纸数据JSON字符串
     * @throws IOException 当HTTP请求失败或响应处理异常时抛出
     */
    public String getStickersData(String lang) throws IOException {
        return fetchData("stickers", lang);
    }

    /**
     * 根据类型获取物品数据
     * @param type 物品类型 (skins, stickers, crates等)
     * @param lang 语言代码
     * @return 物品数据JSON字符串
     * @throws IOException 当HTTP请求失败时抛出
     */
    public String getItemsDataByType(String type, String lang) throws IOException {
        return fetchData(type, lang);
    }

    /**
     * 通用获取数据方法
     * @param type 物品类型
     * @param lang 语言代码
     * @return 物品数据JSON字符串
     * @throws IOException 当HTTP请求失败或响应处理异常时抛出
     */
    private String fetchData(String type, String lang) throws IOException {
        String url = csgoApiBaseUrl + "/" + lang + "/" + type + ".json";
        log.info("开始请求CSGO{}数据，URL: {}", type, url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    log.error("请求CSGO{}数据失败，状态码: {}", type, statusCode);
                    throw new IOException("Failed to fetch CSGO " + type + " data, status code: " + statusCode);
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    log.error("CSGO{}数据响应为空", type);
                    throw new IOException("CSGO " + type + " data response is empty");
                }

                String result = null;
                try {
                    result = EntityUtils.toString(entity, "UTF-8");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                log.info("成功获取CSGO{}数据，长度: {} 字符", type, result.length());
                return result;
            }
        } catch (IOException e) {
            log.error("获取CSGO{}数据异常", type, e);
            throw e;
        }
    }
}