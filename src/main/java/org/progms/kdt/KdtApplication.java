package org.progms.kdt;

import org.progms.kdt.order.OrderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.progms.kdt.order", "org.progms.kdt.voucher", "org.progms.kdt.configuration", "org.progms.kdt.servlet"})
public class KdtApplication {
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);  //OrderTester가 Logger이름이 된다.

    public static void main(String[] args) {
        var applicationContext = SpringApplication.run(KdtApplication.class, args);
        var orderProperties = applicationContext.getBean(OrderProperties.class);
        logger.error("loggerName => {}", logger.getName());
        logger.warn("version -> {}", orderProperties.getVersion());
        logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());
    }

}
