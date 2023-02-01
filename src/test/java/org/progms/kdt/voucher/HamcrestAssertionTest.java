package org.progms.kdt.voucher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

public class HamcrestAssertionTest {

    @Test
    @DisplayName("여러 hamcrest matcher 테스트")
    void hamcrestTest() {
        assertEquals(2, 1+1); //expected, acutal
        assertThat(1+1, equalTo(2));  //acutal, expected
        assertThat(1+1, is(2));  //acutal, expected
        assertThat(1+1, anyOf(is(1), is(2)));  //acutal, expected
        assertNotEquals(1, 1+1);
        assertThat(1+1, not(equalTo(1)));
    }


    @Test      //리스트나 컬렉션을 테스트 가능
    @DisplayName("컬렉션에 대한 matcher 테스트")
    void hamcrestListMatcerTest(){
        var prices = List.of(2, 3, 4);
        assertThat(prices, hasSize(3));     //hasSize() 메소드 제공
        assertThat(prices, everyItem(greaterThan(1)));   //인자의 value보다 큰지 판단.모든 요소가
        assertThat(prices, containsInAnyOrder(3, 4, 2));   //순서를 모르는 경우에서
        assertThat(prices, hasItem(2));
        assertThat(prices, hasItem(greaterThanOrEqualTo(2)));
    }
}
