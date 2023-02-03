package org.progms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    public static void main(String[] args) throws SQLException {
        var SELECT_SQL = "select * form customers";
        /**
         * try block이 끝나면 resource들을 자동으로 close해준다.
         */
        try(
            var  connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(SELECT_SQL);
        ) {
            while (resultSet.next()) {
                var name = resultSet.getString("name");
                var customerid = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();   //Local DateTime으로 바꿔서 사용 권장
                logger.info("customer id -> {} , name -> {}, created_at", customerid, name, createdAt);
            }
        } catch (SQLException throwables) {
            logger.error("Get error while closing connection", throwables);
        }
    }
}
