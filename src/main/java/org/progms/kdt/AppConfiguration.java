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
    @Bean
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
    public VoucherService voucherService(VoucherRepository voucherRepository){  //DI
        return new VoucherService(voucherRepository);
    }
    @Bean
    public OrderService orderService(VoucherService voucherService, OrderRepository orderRepository){   //DI
        return new OrderService(voucherService, orderRepository);
    }

}
