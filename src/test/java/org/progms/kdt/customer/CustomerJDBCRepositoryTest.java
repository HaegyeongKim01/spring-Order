package org.progms.kdt.customer;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@SpringJUnitConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)   //PER_CLASS : 인스턴스가 하나. clean메소드를 static으로 안 해도 된다.  || PER_METHOD
class CustomerJDBCRepositoryTest {

    @Configuration
    @ComponentScan(
        basePackages = {"org.progms.kdt.customer"}
    )
    static class Config{
        @Bean
        public DataSource dataSource(){
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("0917")
                    .type(HikariDataSource.class)
                    .build();
            //connectionPoll이 최대 1000까지 만들어진다. show status Threaqd_connected를 보면 알 수 있다.
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100); //pool에 minimunIde상태로 100개가 들어가있다.
            return dataSource;
        }
    }

    @Autowired
    CustomerRepository customerJDBCRepository;

    @Autowired
    DataSource dataSource;

    @BeforeAll
    void clean(){
        customerJDBCRepository.deleteAll();
    }

    @Test
    @DisplayName("HikariConnectionPool 정상동작 test")
    public void testHikariConnectionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }
    
    @Test
    @DisplayName("전체 고객을 조회할 수 있다. ")
    public void testFindAll() throws InterruptedException {
        var customers = customerJDBCRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @DisplayName("이름으로 고객을 조회할 수 있다. ")
    public void testFindByName() throws InterruptedException {
        var customer = customerJDBCRepository.findByName("updated-user2");
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJDBCRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));
    }
}