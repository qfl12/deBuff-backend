package com.debuff.debuffbackend.service.impl;

import com.debuff.debuffbackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendTradeOfferEmail(String toEmail, String itemName, String tradeOfferId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("您有新的交易报价");
        message.setText(String.format("您的商品【%s】收到新的交易报价，交易ID: %s，请尽快登录Steam确认交易。", itemName, tradeOfferId));
        mailSender.send(message);
    }

    @Override
    public void sendTradeSuccessEmail(String toEmail, String tradeOfferId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("交易成功通知");
        message.setText(String.format("您的交易已完成，交易ID: %s，商品已成功转移到您的Steam库存。", tradeOfferId));
        mailSender.send(message);
    }

    @Override
    public void sendTradeReversedEmail(String toEmail, String tradeOfferId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("交易撤销通知");
        message.setText(String.format("您的交易已被撤销，交易ID: %s，金额已退回您的账户。", tradeOfferId));
        mailSender.send(message);
    }

    @Override
    public void sendTradeFailedEmail(String toEmail, String tradeOfferId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("交易失败通知");
        message.setText(String.format("您的交易失败，交易ID: %s，金额已退回您的账户。", tradeOfferId));
        mailSender.send(message);
    }
}