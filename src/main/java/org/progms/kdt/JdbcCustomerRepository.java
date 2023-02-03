package org.progms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection= DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0917");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from customers");
            while(resultSet.next()){
                var name =  resultSet.getString("name");
                var customerid = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                logger.info("customer id -> {} , name -> {}", customerid, name);
             }

        }
        catch(SQLException throwables){
            throwables.printStackTrace();
            throw throwables;
        }finally {
            try{
                if(connection != null) {connection.close();}
                if(statement != null) {statement.close();}
                if(resultSet != null) {resultSet.close();}
            } catch (SQLException exception){
                logger.error("Gor error while closing connection", exception);
            }
        }
    }

}
