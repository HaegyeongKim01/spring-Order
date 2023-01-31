package org.progms.kdt;

import org.progms.kdt.order.OrderItem;
import org.progms.kdt.order.OrderProperties;
import org.progms.kdt.order.OrderService;
import org.progms.kdt.voucher.FixedAmountVoucher;
import org.progms.kdt.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;


public class OrderTester {
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);  //OrderTester가 Logger이름이 된다.

    public static void main(String[] args) {

        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);

        var applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);  //ApplicationContext 생성

        var customerId = UUID.randomUUID();

        //SINGLETON 확인 코드
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
//        var voucherRepository2 = applicationContext.getBean(VoucherRepository.class);
//        System.out.println(MessageFormat.format("voucherRepository {0}", voucherRepository));
//        System.out.println(MessageFormat.format("voucherRepository2 {0}", voucherRepository2));
//        System.out.println(MessageFormat.format("voucherRepository ==voucherRepository2 => {0}", voucherRepository==voucherRepository));
//        //same == SINGLETON

        var orderProperties =  applicationContext.getBean(OrderProperties.class);
        logger.error("logger name => {}", logger.getName());
        logger.warn("logger name -> {}", logger.getName());
        logger.warn("version -> {}", orderProperties.getVersion());
        logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());

//        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));
//
//        var orderService = applicationContext.getBean(OrderService.class);
//        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>() {{
//            add(new OrderItem(UUID.randomUUID(), 100L, 1));
//        }}, voucher.getVoucherId());
//
//        Assert.isTrue(order.totalAmount() == 90L, MessageFormat.format("totalAmount {0} is not 100L", order.totalAmount()));

        //Container에 등록된 모든 Bean이 소멸하고 소멸에 대한 콜백이 일어난다.
        applicationContext.close();
    }
}
