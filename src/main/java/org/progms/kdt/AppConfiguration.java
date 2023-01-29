package org.progms.kdt;

import java.util.Optional;
import java.util.UUID;
import org.progms.kdt.configuration.YamlPropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class)
public class AppConfiguration {

    VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }


    @Bean
    public OrderRepository orderRepository(){
       return new OrderRepository() {
           @Override
           public void insert(Order order) {

           }
       };
    }
    @Bean
    public VoucherService voucherService(){
        return new VoucherService(voucherRepository());
    }
    @Bean
    public OrderService orderService(){
        return new OrderService(voucherService(), orderRepository());
    }

}
