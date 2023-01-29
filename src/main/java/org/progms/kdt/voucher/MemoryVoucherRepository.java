package org.progms.kdt.voucher;

import org.progms.kdt.voucher.Voucher;
import org.progms.kdt.voucher.VoucherRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//Voucher repo와 Order repo를 메모리 기반으로 관리되도록 별도의 클래스로 뺀다.
@Repository
public class MemoryVoucherRepository implements VoucherRepository {
    //메모리상 관리 Hash map 사용
    private final Map<UUID, Voucher> storage = new ConcurrentHashMap<>();


    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        return Optional.ofNullable(storage.get(voucherId));   //null일 경우 empty되도록 처리
    }

    @Override
    public Voucher insert(Voucher voucher) {
        storage.put(voucher.getVoucher(), voucher);
        return voucher;
    }
}
