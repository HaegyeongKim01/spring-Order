package org.progms.kdt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Datasource에 의해 connection을 가져오도록 처리
 * CustomerJDBCRepositoryTest에서 ComponentScan사용하려면 대상 class에 @Repository 어노테이션 필요.그래야 Bean을 찾음
 */
@Repository
public class CustomerNamedJDBCRepository implements CustomerRepository{


    private static final Logger logger = LoggerFactory.getLogger(CustomerNamedJDBCRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final TransactionTemplate transactionTemplate;

    //resultSet과 index를 받으면 Customer를 반환해주는 것
    private static final RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var creatdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        return new Customer(customerId, customerName, email, lastLoginAt, creatdAt);
    };

    /**
     * @param jdbcTemplate        생성자 주입으로 입력받음
     * @param transactionTemplate
     */
    public CustomerNamedJDBCRepository(NamedParameterJdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    private Map<String, Object> toParamMap(Customer customer) {
        return new HashMap<>(){{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt", Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null );
        }};
    }

    @Override
    public Customer insert(Customer customer) {
        var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UNHEX(REPLACE(:customerId, '-', '')), :name, :email, :createdAt)",
                toParamMap(customer));

        if(update != 1) throw new RuntimeException("Noting was inserted");

        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE customers SET name = :name, last_login_at = :lastLoginAt WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))",
                toParamMap(customer));

        if (update != 1){
            throw new RuntimeException("Noting was inserted");
        }

        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers",Collections.emptyMap() ,Integer.class); //Integer로 return 받겠다.
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        //query() 는 List를 반환한다. 하나의 Object가지고 오고 싶을 때는 queryForObject()사용
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))",
                    Collections.singletonMap("customerId", customerId.toString().getBytes()),
                    customerRowMapper)
            );
        } catch (EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = :name",
                    Collections.singletonMap("name", name),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("got empty result" , e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = :email",
                    Collections.singletonMap("email", email),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from customers", Collections.emptyMap());
    }

        public void testTransaction(Customer customer) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.update("UPDATE customers SET name = :name WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
                jdbcTemplate.update("UPDATE customers SET email = :email WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
            }
        });
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    private void mapToCustomer(List<Customer> allCustomers, ResultSet resultSet) throws SQLException{
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));   //version4가 되도록
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }

}