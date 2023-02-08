package org.progms.kdt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.progms.kdt.order.*;
import org.progms.kdt.voucher.FixedAmountVoucher;
import org.progms.kdt.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringJUnitConfig
@ActiveProfiles("test")
public class applicationContextTest {

    @Configuration
    @ComponentScan(
            basePackages = {"org.progms.kdt.voucher", "org.progms.kdt.order"}
    )

    @EnableAspectJAutoProxy   //AOP 적용하려면 필요
    static class Config{

    }

    @Autowired // application context를 autowired를 통해 자동으로 불러올 수 있다..
    ApplicationContext applicationContext;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    OrderService orderService;

    @Test
    @DisplayName("application Context가 생성되야 한다")
    void applicationContext(){
        assertThat(applicationContext, notNullValue());
    }

    @Test
    @DisplayName("VoucherRepository가 빈으로 등록되어 있어야 한다.")
    void testVoucherRepositoryCreation(){
        VoucherRepository bean = applicationContext.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());

    }

    @Test
    @DisplayName("orderService를 사용해서 주문을 생성할 수 있다.")
    void testOrderService(){
        //Given
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        //When
        var order = orderService.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //Then    //상태에 집중해서 테스트 코드 작성
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }
}