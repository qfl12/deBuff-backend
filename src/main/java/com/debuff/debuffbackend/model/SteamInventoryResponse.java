package com.debuff.debuffbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Steam库存接口响应数据模型
 * 用于解析Steam inventory API返回的JSON数据
 */
@Data
public class SteamInventoryResponse {
    /**
     * 资产列表
     */
    private List<Asset> assets;

    /**
     * 物品描述列表
     */
    private List<Description> descriptions;

    /**
     * 库存总数量
     */
    @JsonProperty("total_inventory_count")
    private int totalInventoryCount;

    /**
     * 请求是否成功
     */
    private int success;

    /**
     * 错误代码
     */
    private int rwgrsn;

    /**
     * 资产信息内部类
     */
    @Data
    public static class Asset {
        private String appid;
        private String contextid;
        private String assetid;
        private String classid;
        private String instanceid;
        private String amount;
    }

    /**
     * 物品描述内部类
     */
    @Data
    public static class Description {
        private String appid;
        private String classid;
        private String instanceid;
        private int currency;
        @JsonProperty("background_color")
        private String backgroundColor;
        @JsonProperty("icon_url")
        private String iconUrl;
        private List<DescriptionItem> descriptions;
        private int tradable;
        private List<Action> actions;
        private String name;
        @JsonProperty("name_color")
        private String nameColor;
        private String type;
        @JsonProperty("market_name")
        private String marketName;
        @JsonProperty("market_hash_name")
        private String marketHashName;
        @JsonProperty("market_actions")
        private List<MarketAction> marketActions;
        private int commodity;
        @JsonProperty("market_tradable_restriction")
        private int marketTradableRestriction;
        @JsonProperty("market_marketable_restriction")
        private int marketMarketableRestriction;
        private int marketable;
        private List<Tag> tags;

        /**
         * 描述信息内部类
         */
        @Data
        public static class DescriptionItem {
            private String type;
            private String value;
            private String name;
        }

        /**
         * 操作信息内部类
         */
        @Data
        public static class Action {
            private String link;
            private String name;
        }

        /**
         * 市场操作信息内部类
         */
        @Data
        public static class MarketAction {
            private String link;
            private String name;
        }

        /**
         * 标签信息内部类
         */
        @Data
        public static class Tag {
            private String category;
            @JsonProperty("internal_name")
            private String internalName;
            @JsonProperty("localized_category_name")
            private String localizedCategoryName;
            @JsonProperty("localized_tag_name")
            private String localizedTagName;
            private String color;
        }
    }
}