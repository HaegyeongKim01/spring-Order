package org.progms.kdt;

import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        var customerId = UUID.randomUUID();
        var orderContext = new OrderContext();
       // var orderService = ordercontext.orderService();
        var orderItems = new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }};
        //orderService.createOrder(customerId, orderItems, );

        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 10L);
        var order= new Order(UUID.randomUUID(), customerId ,orderItems, fixedAmountVoucher);
        Assert.isTrue(order.totalAmount() == 90L, MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));
    }
}
