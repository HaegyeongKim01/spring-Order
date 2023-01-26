package org.progms.kdt;

import java.util.List;
import java.util.UUID;

public class Order {   //ENTITY  //비즈니스 lOGIC이 들어간다.
    private final UUID orderId;
    private final UUID customerId;
    private final List<OrderItem> orderItems;
    private FixedAmountVoucher fixedAmountVoucher;
    private OrderStatus orderStatus;

    public Order(UUID orderId, UUID customerId, List<OrderItem> orderItems, long discountAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.fixedAmountVoucher = new FixedAmountVoucher(discountAmount);   //매개변수로 받은 값을 객체 생성 인자에 put하여 객체 인스턴스 생성
    }

    public long totalAmount(){
        var beforeDiscount = orderItems.stream().map(v -> v.getProductPrice()*v.getQuantity())
                .reduce(0L, Long::sum);
        return fixedAmountVoucher.discount(beforeDiscount);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
