package org.progms.kdt.voucher;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Repository
//@Profile({"dev", "staging", "production"})
//@Qualifier("jdbc")
@Profile("dev")
public class JdbcVoucherRepository {
    public class JDBCVoucherRepository implements VoucherRepository {
        private final Map<UUID, Voucher> storage = new ConcurrentHashMap<>();

        @Override
        public Optional<Voucher> findById(UUID voucherId) {
            return Optional.ofNullable(storage.get(voucherId));
        }

        @Override
        public Voucher insert(Voucher voucher) {
            storage.put(voucher.getVoucherId(), voucher);
            return voucher;
        }
    }
}
