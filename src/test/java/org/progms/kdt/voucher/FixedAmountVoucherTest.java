package org.progms.kdt.voucher;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;  //Assert 문에 대한 static import가 자동으로 들어가진다.

class FixedAmountVoucherTest {   //SUT가 된다.

    //test code가 매번 동작하도록 하는 beforeall
    /*
    * Life cycle annotation
    * @BeforeAll : 클래스가 생성되기 전에 initailize될 때 한 번만 실행
    *   static으로 작성해야한다.
    * @BeforeEach : 매 메소드마다 호출 되는 것
     */

   private static final Logger logger = LoggerFactory.getLogger(FixedAmountVoucherTest.class);

   //test code가 실행 되기 전 한 번만 실행
    @BeforeAll
    static void setup(){
        logger.info("@BeforeAll - 단 한 번 실행");
    }

    //test code 실행 전 매번 실행
    @BeforeEach
    void init(){
        logger.info("@BeforeEach - 매 테스트 마다 실행");
    }

    //test method. MUT가 된다.
    @Test
    @DisplayName("기본적인 assertEquals 테스트 \uD83C\uDF20 ")
    void testAssertEqual() {
        assertEquals(2, 1+1); //두 번째 인자: 실제 값
    }

    @Test
    @DisplayName("주어진 금액만큼 할인을 해야한다. ")
    void testDiscount() {
        //test 하고자 하는 것 == sut
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 100);

        assertEquals(900, sut.discount(1000));
    }

    @Test
    @DisplayName("디스카운트된 금액은 마이너스가 될 수 없다.  ")
    void testMinusDiscountAmount() {
        //test 하고자 하는 것 == sut
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 1000);

        assertEquals(0, sut.discount(900));
    }

    @Test   //예외 발생 test
    @DisplayName("할인 금액은 마이너스가 될 수 없다. ")
//  @Disabled        // 사용하면 이 메소드 동작 X
    void testWithMinusDiscount() {
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }

    @Test   //예외 발생 test
    @DisplayName("유효한 금액으로만 생성할 수 있다. ")
    void testVoucherCreation() {
        assertAll("FixedAmountVoucher creation",
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), 100000))
        );

    }

}