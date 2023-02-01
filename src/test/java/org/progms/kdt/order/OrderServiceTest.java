package org.progms.kdt.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.progms.kdt.voucher.FixedAmountVoucher;
import org.progms.kdt.voucher.MemoryVoucherRepository;
import org.progms.kdt.voucher.VoucherService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

//Mock Onject의 지원을 받아서
class OrderServiceTest {
//    OrderRepository가 없다면 여기서 만들어도 가능
    class OrderRepositoryStub implements OrderRepository{

    @Override
    public Order insert(Order order) {
        return null;
    }
}

    @Test
    void createOrder() {
    //      Given
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        var sut = new OrderService(new VoucherService(voucherRepository), new OrderRepositoryStub());     //가짜 객체
    //      When
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

    //        Then. 상태에 집중
        assertThat(order.totalAmount(), is(100L));  //Long Type으로 정확히 기술해야한다.
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    @DisplayName("오더가 생성되야한다. (stub)")
    void createOrderByMock(){
        //Given. 행위에 집중. 어떤 메소드들이 제공되는지 제공해주어야한다. setup
        var voucherServiceMock =  mock(VoucherService.class);  //mock을 만들면 실제로 객체만 만들어진다.
        var orderRepositoryMock = mock(OrderRepository.class);
        var fixedAmountVoucher= new FixedAmountVoucher(UUID.randomUUID(), 100);
        //fixedAmountVoucher 를 가져오면 return 한다.
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);

        var sut = new OrderService(voucherServiceMock, orderRepositoryMock);

        //When. 실행
        var order = sut.createOrder(
                UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId()
        );


        //Then. 상태 검증 & 어떤 행동 메소드가 호출되는지 잘 작동, 사용 되는지 검증
        assertThat(order.totalAmount(), is(100L));  //Long Type으로 정확히 기술해야한다.
        assertThat(order.getVoucher().isEmpty(), is(false));
        /*
        * 특정 순서에 따라 호출 하고 싶다면 inOrder 사용하여 처리 가능
        * */
        var inOrder = inOrder(voucherServiceMock, orderRepositoryMock); //어떤 순서에 따라 호출되어야 하는지 정의 가능
        inOrder.verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());
        inOrder.verify(orderRepositoryMock).insert(order);
        inOrder.verify(voucherServiceMock).useVoucher(fixedAmountVoucher);
    }
}