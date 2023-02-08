package org.progms.kdt.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    @Transactional   //insert 중 오류가 난다면 rollback 등 처리
    public void cerateCustomers(List<Customer> customers) {
        customers.forEach(customerRepository::insert);
    }


}
