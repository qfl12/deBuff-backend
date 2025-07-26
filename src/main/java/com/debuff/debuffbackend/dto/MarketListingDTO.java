package com.debuff.debuffbackend.dto;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品挂牌数据传输对象
 * 用于标准化前后端商品挂牌相关数据交换
 */
@Data
public class MarketListingDTO {
    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    /**
     * 挂牌价格
     */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    /**
     * 卖家备注
     */
    private String sellerNote;
}