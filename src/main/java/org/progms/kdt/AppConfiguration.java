package org.progms.kdt;

import org.progms.kdt.configuration.YamlPropertiesFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"org.progms.kdt.order", "org.progms.kdt.voucher", "org.progms.kdt.configuration"}) //이렇게도 가능
//@PropertySource("application.properties")
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class)  //properties
@EnableConfigurationProperties
public class AppConfiguration {


}
