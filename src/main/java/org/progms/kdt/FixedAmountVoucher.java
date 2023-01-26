package org.progms.kdt;

public class FixedAmountVoucher {
    private final long amount;

    public FixedAmountVoucher(long amount) {
        this.amount = amount;
    }
    //어떻게 discount 할지 Logic을 들고 있게 만든다.

    public long discount(long beforeDiscount){
        return beforeDiscount - amount;
    }
}
