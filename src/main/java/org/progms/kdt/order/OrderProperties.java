package org.progms.kdt.order;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLOutput;
import java.text.MessageFormat;
import java.util.List;

@Component
public class OrderProperties implements InitializingBean {
    @Value("${kdt.version:v0.0.0}")
    private String version;

    @Value("${kdt.minimum-order-amount}")
    private int minimumOrderAmount;

    @Value("${kdt.support-vendors}")
    private List<String> supportVendors;

    @Value("${kdt.JAVA_HOME}")
    private String javaHome;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(MessageFormat.format("version -> {0}",version));
        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}",minimumOrderAmount));
        System.out.println(MessageFormat.format("supportVendors -> {0}",supportVendors));

    }

}
