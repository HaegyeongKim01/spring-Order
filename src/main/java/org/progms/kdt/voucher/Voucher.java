package org.progms.kdt.voucher;

import java.util.UUID;

public interface Voucher {
    UUID getVoucher();

    long discount(long beforeDiscount);
}
