package org.progms.kdt;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher{
    private final UUID voucherId;
    private final long amount;

    public FixedAmountVoucher(UUID voucherId, long amount) {
        this.voucherId = voucherId;
        this.amount = amount;
    }
    //어떻게 discount 할지 Logic을 들고 있게 만든다.

    @Override
    public UUID getVoucher() {
        return voucherId;
    }

    public long discount(long beforeDiscount){
        return beforeDiscount - amount;
    }
}