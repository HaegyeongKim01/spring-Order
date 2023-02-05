package org.progms.kdt.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer insert(Customer customer);

    Customer update(Customer customer);

    //Customer save(Customer customer);  //JPA에서 지원 없으면 insert 존재하면 update

    List<Customer> findAll();

    /**
     *
     * @param customerId UUID
     * @return customerId로 Customer를 찾았는데 없는 경우에는 Optional 로 처리
     */
    Optional<Customer> findById(UUID customerId);
    Optional<Customer> findByName(String name);
    Optional<Customer> findByEmail(String email);
    void deleteAll();
}
