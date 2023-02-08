package org.progms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "select * from customers where name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "INSERT INTO customers (customer_id, name, email) VALUES (UUID_TO_BIN(?), ?, ?)"; // ? == plcaeholder 나중에 치환된다.
    private final String UPDATE_BY_ID_SQL = "UPDATE customers set name = ?  WHERE customer_id = UUID_TO_BIN(?)"; // ? == plcaeholder 나중에 치환된다.
    private final String DELETE_SQL = "DELETE FROM customers";

    public List<String> findNames(String name){
        List<String> names = new ArrayList<>();

        /**
         * try block이 끝나면 resource들을 자동으로 close해준다.
         */
        try(
                var connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);      //prepareStmt 사용! SQL Injection 방지
        ) {
            statement.setString(1, name);   //시작 index, 실제 전달받는 변수   .  //parameter 전달해야함
            try(var resultSet = statement.executeQuery()){       //실행
                while (resultSet.next()) {
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();   //Local DateTime으로 바꿔서 사용 권장
                    logger.info("customer id -> {} , name -> {}, created_at", customerId, name, createdAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException throwables) {
            logger.error("Get error while closing connection", throwables);
        }

        return names;
    }

    public List<String> findAllName(){
        List<java.lang.String> names = new ArrayList<>();

        /**
         *  try block이 끝나면 resource들을 자동으로 close해준다.
         */
        try(
                var connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);      //prepareStmt 사용! SQL Injection 방지
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();   //Local DateTime으로 바꿔서 사용 권장
                names.add(customerName);
            }
        } catch (SQLException throwable) {
            logger.error("Get error while closing connection", throwable);
        }

        return names;
    }


    public List<UUID> findAllIds(){
        List<UUID> uuids = new ArrayList<>();

        /**
         * try block이 끝나면 resource들을 자동으로 close해준다.
         */
        try(
                var  connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);      //prepareStmt 사용! SQL Injection 방지
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
//                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));    => 이렇게 사용하면 UUID version이 다름. static method만들어서 사용
                var customerId = toUUID(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();   //Local DateTime으로 바꿔서 사용 권장
                uuids.add(customerId);
            }
        } catch (SQLException throwable) {
            logger.error("Get error while closing connection", throwable);
        }

        return uuids;
    }


    public int insertCustomer(UUID customerId, String name, String email){
        try(
                var connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
                var statement = connection.prepareStatement(INSERT_SQL);      //prepareStmt 사용! SQL Injection 방지
        ) {
            statement.setBytes(1, customerId.toString().getBytes());   //시작 index, 실제 전달받는 변수   .  //parameter 전달해야함
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();   //excuteUpdate() 한다.
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }

    public int updateCustomerName(UUID customerId, String name){
        try(
                var connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
                var statement = connection.prepareStatement(UPDATE_BY_ID_SQL);      //prepareStmt 사용! SQL Injection 방지
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());   //시작 index, 실제 전달받는 변수   .  //parameter 전달해야함
            return statement.executeUpdate();   //excuteUpdate() 한다.
        }catch (SQLException throwable){
            logger.error("Got error while closing connection", throwable);
        }
        return 0;
    }

    public int deleteAllCustomers(){
        try(
                var connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
                var statement = connection.prepareStatement(DELETE_SQL);      //prepareStmt 사용! SQL Injection 방지
        ) {
            return statement.executeUpdate();   //excuteUpdate() 한다.
        }catch (SQLException throwable){
            logger.error("Got error while closing connection!", throwable);
        }
        return 0;
    }

    static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static void main(String[] args) {
        var customerRepository = new JdbcCustomerRepository();

        //delete
        var count = customerRepository.deleteAllCustomers();
        logger.info("deleted count -> {}", count);

        //insert
        var customer1 = UUID.randomUUID();
        customerRepository.insertCustomer(customer1, "new-user", "new-user@gmail.com");
        //insert 된 cutomer1 UUID 조회
        logger.info("Customer id - >{}", customer1);

        //find id로 조회
        customerRepository.findAllIds().forEach(v -> logger.info("found id : {} ", v));

        var customer2 = UUID.randomUUID();
        customerRepository.insertCustomer(customer2, "new-user2", "new-user2@gmail.com");

        //find name으로 조회
        customerRepository.findAllName().forEach(v -> logger.info("found name : {} ", v));

        //update
        customerRepository.updateCustomerName(customer2, "updated-user2");

        //조회
        customerRepository.findAllName().forEach(v -> logger.info("updated-found name : {} ", v));

    }
}