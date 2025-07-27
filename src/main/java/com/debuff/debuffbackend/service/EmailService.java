package com.debuff.debuffbackend.service;

public interface EmailService {
    /**
     * 发送交易报价通知邮件给卖家
     */
    void sendTradeOfferEmail(String toEmail, String itemName, String tradeOfferId);

    /**
     * 发送交易成功通知邮件给买家
     */
    void sendTradeSuccessEmail(String toEmail, String tradeOfferId);

    void sendTradeReversedEmail(String toEmail, String tradeOfferId);

    void sendTradeFailedEmail(String toEmail, String tradeOfferId);
}