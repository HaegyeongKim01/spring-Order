package org.progms.kdt;

import java.util.List;
import java.util.UUID;

/*
* 오더에 대해서 주문에 대한 비즈니스 로직을 담는 class
* voucherService와 OrderRepository(정보를 기록하고 조회할 수 있는) 대해 의존성을 가진다.
* */
public class OrderService {
    private final VoucherService voucherService;
    private final OrderRepository orderRepository;

    //OrderService 생성할 때 생성자를 통해서 객체를 외부에 의해 주입할 수 있도록 작성
    public OrderService(VoucherService voucherService, OrderRepository orderRepository) {
        this.voucherService = voucherService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherId){
        var voucher = voucherService.getVoucher(voucherId);
        var order = new Order(UUID.randomUUID(), customerId, orderItems, voucher);
        orderRepository.insert(order);
        voucherService.useVoucher(voucher);
        return order;
    }
}
