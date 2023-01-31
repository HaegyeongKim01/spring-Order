//package org.progms.kdt.order;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.text.MessageFormat;
//import java.util.List;
//
//@Component
//@ConfigurationProperties(prefix = "kdt")
//public class OrderProperties implements InitializingBean {
//    private String version;
//
//    private int minimumOrderAmount;
//
//    private List<String> supportVendors;
//
//    private String javaHome;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println(MessageFormat.format("version -> {0}",version));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}",minimumOrderAmount));
//        System.out.println(MessageFormat.format("supportVendors -> {0}",supportVendors));
//
//    }
//    public String getVersion() {
//        return version;
//    }
//
//    public int getMinimumOrderAmount() {
//        return minimumOrderAmount;
//    }
//
//    public List<String> getSupportVendors() {
//        return supportVendors;
//    }
//
//    public String getJavaHome() {
//        return javaHome;
//    }
//}
package org.progms.kdt.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "kdt")
public class OrderProperties implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(OrderProperties.class);

    private String version;

    private int minimumOrderAmount;

    private List<String> supportVendors;

    private List<String> description;

    @Value("${JAVA_HOME}")
    private String javaHome;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(int minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public List<String> getSupportVendors() {
        return supportVendors;
    }

    public void setSupportVendors(List<String> supportVendors) {
        this.supportVendors = supportVendors;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("version -> {}", version);
        logger.info("minimumOrderAmount -> {}", minimumOrderAmount);
        logger.info("supportVendors -> {}", supportVendors);
        logger.info("javaHome -> {}", javaHome);
        logger.info("description -> {}", description);
    }
}
