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

    //지워도 가능하다. MemoryOrderRepository에서 만들었기에!!
//    @Bean
//    VoucherRepository voucherRepository(){
//        return new VoucherRepository() {
//            @Override
//            public Optional<Voucher> findById(UUID voucherId) {
//                return Optional.empty();
//            }
//        };
//    }
//
//
//    @Bean
//    public OrderRepository orderRepository(){
//       return new OrderRepository() {
//           @Override
//           public void insert(Order order) {
//
//           }
//       };
//    }


    //Service에 @Service Annotation을 달았으니 지워도 코드가 실행된다.
//    @Bean
//    public VoucherService voucherService(VoucherRepository voucherRepository){  //DI
//        return new VoucherService(voucherRepository);
//    }
//    @Bean
//    public OrderService orderService(VoucherService voucherService, OrderRepository orderRepository){   //DI
//        return new OrderService(voucherService, orderRepository);
//    }

}
