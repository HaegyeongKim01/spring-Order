package org.progms.kdt.voucher;

import org.progms.kdt.voucher.Voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    private final UUID voucherId;
    private final long amount;

    public FixedAmountVoucher(UUID voucherId, long amount) {
        if (amount<0) throw new IllegalArgumentException("Amount should be positive");   // 음수인 경우 예외발생 처리 .
        this.voucherId = voucherId;
        this.amount = amount;
    }
    //어떻게 discount 할지 Logic을 들고 있게 만든다.

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    public long discount(long beforeDiscount){
        return beforeDiscount - amount;
    }
}
