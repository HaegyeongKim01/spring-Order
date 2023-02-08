package org.progms.kdt.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.progms.kdt.voucher.FixedAmountVoucher;
import org.progms.kdt.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

@SpringJUnitConfig
@ActiveProfiles("test")
public class AopTests {

    @Configuration
    @ComponentScan(
            basePackages = {"org.progms.kdt.voucher", "org.progms.kdt.aop"}
    )

    @EnableAspectJAutoProxy   //AOP 적용하려면 필요
    static class Config{

    }

    @Autowired // application context를 autowired를 통해 자동으로 불러올 수 있다..
    ApplicationContext applicationContext;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("orderService를 사용해서 주문을 생성할 수 있다.")
    void testOrderService(){
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
    }
}