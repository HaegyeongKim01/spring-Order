package org.progms.kdt;

import java.util.List;
import java.util.UUID;

public class Order {   //ENTITY  //비즈니스 lOGIC이 들어간다.
    private final UUID orderId;
    private final UUID customerId;
    private final List<OrderItem> orderItems;
    private long discountAmount;
    private OrderStatus orderStatus;

    public Order(UUID orderId, UUID customerId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
    }

    public long totalAmount(){
        var beforeDiscount = orderItems.stream().map(v -> v.getProductPrice()* v.getQuantity()).reduce(0L, Long::sum);
        return beforeDiscount - discountAmount;
    }
    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
