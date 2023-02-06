package org.progms.kdt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
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

    /**
     *
     * @param dataSource 생성자 주입으로 입력받음
     */
    public CustomerJDBCRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Customer insert(Customer customer) {
        try(
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)")
        ) {
            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));

            var executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1) {  //insert는 하나여야 한다. 0 X. 여러 개 X
                throw new RuntimeException("Nothing was inserted");
            }
            return customer;

        } catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = new ArrayList<>();
        try(
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("select * from customers");      //prepareStmt 사용! SQL Injection 방지
            var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                mapToCustomer(allCustomers, resultSet);
            }
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable);
        }

        return allCustomers;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        List<Customer> allCustomers = new ArrayList<>();

        try(
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("select * from customers where customer_id = UUID_TO_BIN(?)");
            //UUID_TO_BIN() 함수 서야 제대로 변환이 되어 주의할 것!!!
        ){
            statement.setBytes(1, customerId.toString().getBytes());    //sql문의 ? 에 value 설정
            try(var resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    mapToCustomer(allCustomers, resultSet);
                }
            }

        } catch (SQLException throwable) {
            logger.error("Got error while closing connection");
            throw new RuntimeException(throwable);
        }
        return allCustomers.stream().findFirst();    //List에서 first가져온다.
    }

    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allCustomers = new ArrayList<>();

        try(
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("select * from customers where name = ?");
        ){
            statement.setString(1, name);    //sql문의 ? 에 value 설정
            try (var resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    mapToCustomer(allCustomers, resultSet);
                }
            }

        } catch (SQLException throwable) {
            logger.error("Got error while closing connection");
            throw new RuntimeException(throwable);
        }
        return allCustomers.stream().findFirst();    //List에서 first가져온다.
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allCustomer = new ArrayList<>();

        try(
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("select * from customers where email = ?");
        ){
            statement.setString(1, email);    //sql문의 ? 에 value 설정
            try(var resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    mapToCustomer(allCustomer, resultSet);
                }
            }

        } catch (SQLException throwable) {
            logger.error("Got error while closing connection");
            throw new RuntimeException(throwable);
        }
        return allCustomer.stream().findFirst();    //List에서 first가져온다.
    }

    @Override
    public void deleteAll() {
        try (
            var connection = dataSource.getConnection();
            var statemtent = connection.prepareStatement("DELETE FROM customers");
        ) {
            statemtent.executeUpdate();
        } catch (SQLException throwable){
            logger.error("Got error while closing connection");
            throw new RuntimeException(throwable);
        }
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
