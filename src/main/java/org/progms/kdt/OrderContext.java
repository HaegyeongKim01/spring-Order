package org.progms.kdt;

import java.util.Optional;
import java.util.UUID;

//주요 객체에 대한 생성, 관계 설정하는 class
//각각의 컴포넌트 생성하는 메소드 생성, 의존관계 생성
public class OrderContext {
    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }
    public OrderRepository orderRepository(){
        return new OrderRepository() {
            @Override
            public void insert(Order order) {

            }
        };
    }
    public VoucherService voucherService(){
        return new VoucherService(voucherRepository());
    }
    public OrderService orderService(){
        return new OrderService(voucherService(), orderRepository());
    }
}
