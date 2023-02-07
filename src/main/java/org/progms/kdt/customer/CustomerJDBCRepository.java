package org.progms.kdt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Datasource에 의해 connection을 가져오도록 처리
 * CustomerJDBCRepositoryTest에서 ComponentScan사용하려면 대상 class에 @Repository 어노테이션 필요.그래야 Bean을 찾음
 */
@Repository
public class CustomerJDBCRepository implements CustomerRepository{


    private static final Logger logger = LoggerFactory.getLogger(CustomerJDBCRepository.class);

    /**
     * 주입 받을 Datasource 생성
     */
    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    //resultSet과 index를 받으면 Customer를 반환해주는 것
    private static final
    RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var creatdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        return new Customer(customerId, customerName, email, lastLoginAt, creatdAt);
    };

    /**
     *
     * @param dataSource 생성자 주입으로 입력받음
     */
    public CustomerJDBCRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Customer insert(Customer customer) {
        //var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)",  window 는 오류
        var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?, ?)",
                customer.getCustomerId().toString().getBytes(),    //?에 들어갈 parameter들
                customer.getName(),
                customer.getEmail(),
                Timestamp.valueOf(customer.getCreatedAt())
        );

        if (update != 1){
            throw new RuntimeException("Noting was inserted");
        }

        return customer;
    }

    @Override
    public Customer update(Customer customer) {

        //var update = jdbcTemplate.update("UPDATE customers SET name = ?, email = ? , last_login_at = ? WHERE customer_id = UUID_TO_BIN(?)",
        var update = jdbcTemplate.update("UPDATE customers SET name = ?, email = ?, last_login_at = ?  WHERE customer_id = UNHEX(REPLACE(?, '-', ''))",
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getCreatedAt()) : null,
                customer.getCustomerId().toString().getBytes()
        );

        if (update != 1){
            throw new RuntimeException("Noting was inserted");
        }

        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers", Integer.class); //Integer로 return 받겠다.
    }

    @Override
    public List<Customer> findAll() {
       return jdbcTemplate.query("select * from customers", customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        //query() 는 List를 반환한다. 하나의 Object가지고 오고 싶을 때는 queryForObject()사용
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customer_id = UNHEX(REPLACE(?, '-', ''))",
                    customerRowMapper,
                    customerId.toString().getBytes())
            );
        } catch (EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }

    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = ?",
                    customerRowMapper,
                    name));
        } catch (EmptyResultDataAccessException e) {
            logger.error("got empty result" , e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = ?",
                    customerRowMapper,
                    email));
        } catch (EmptyResultDataAccessException e) {
            logger.error("got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from customers");
    }
    
    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    private void mapToCustomer(List<Customer> allCustomers, java.sql.ResultSet resultSet) throws SQLException{
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));   //version4가 되도록
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }

}
