package org.progms.kdt;

import java.util.Optional;
import java.util.UUID;
import org.progms.kdt.configuration.YamlPropertiesFactory;
import org.progms.kdt.order.Order;
import org.progms.kdt.voucher.Voucher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@ComponentScan(basePackages = {"org.progms.kdt.order", "org.progms.kdt.voucher"})
@ComponentScan(basePackageClasses = {Order.class, Voucher.class})
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class)
public class AppConfiguration {


}
