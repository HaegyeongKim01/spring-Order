package org.progms.kdt.voucher;

import org.progms.kdt.voucher.Voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    private static final long MAX_VOUCHER_AMOUNT = 10000;

    private final UUID voucherId;
    private final long amount;

    public FixedAmountVoucher(UUID voucherId, long amount) {
        //testcode에서 오류를 발견하고 오류 처리하였다.
        if (amount < 0) throw new IllegalArgumentException("Amount should be positive");   // 음수인 경우 예외발생 처리 .
        if (amount == 0) throw new IllegalArgumentException("Amount should be positive");   // 0인 경우 예외발생 처리 .
        if (amount > MAX_VOUCHER_AMOUNT) throw new IllegalArgumentException("Amount should be positive");   // MAX_VALUE보다 큰 경우 예외발생 처리 .

        this.voucherId = voucherId;
        this.amount = amount;
    }
    //어떻게 discount 할지 Logic을 들고 있게 만든다.

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    public long discount(long beforeDiscount){
        var discountAmount = beforeDiscount - amount;
        return (discountAmount<0) ? 0: discountAmount;
    }
}
