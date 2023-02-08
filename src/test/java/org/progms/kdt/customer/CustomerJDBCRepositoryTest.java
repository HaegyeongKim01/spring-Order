package org.progms.kdt.customer;

import com.wix.mysql.EmbeddedMysql;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

//Embedded Database 사용 import
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)   // @Order를 사용하여 test method 순서를 지정
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
                    .url("jdbc:mysql://localhost:2215/test-order_mgmt")      //Embdded로 사용할 경우 Config에서 지정한 port num을 넣어줘야함. localhost:2215
                    .username("test")   //Config에서 정의한 username, pw 로 설정
                    .password("test1234!")
                    .type(HikariDataSource.class)
                    .build();
            //connectionPoll이 최대 1000까지 만들어진다. show status Threaqd_connected를 보면 알 수 있다.
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100); //pool에 minimunIde상태로 100개가 들어가있다.
            return dataSource;
        }

        /**
         * JDBC Template 이용할 때 Bean으로 코드를 작성필요
         * @param dataSource
         * @return
         */
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

    @Autowired
    CustomerRepository customerJDBCRepository;

    @Autowired
    DataSource dataSource;

    Customer newCustomer;

    EmbeddedMysql embeddedMysql;
    /**
     * 한 번만 호출
     * 원래 static method 여야하지만 TestInstance를 PER_CLASS로 지정하여 static 안 적게 해결
     * DB구동
     */
    @BeforeAll
    void setup(){
        //  Window는 정밀도가 밀리(3자리)
        newCustomer = new Customer(UUID.randomUUID(), "test-user10", "test-user10@gmail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        //Config에 의해 mysql이 만들어지도록
        var mysqlConfig = aMysqldConfig(v5_7_latest)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();

        embeddedMysql = anEmbeddedMysql(mysqlConfig)
                .addSchema("test-order_mgmt", classPathScript("schema.sql"))
                .start();

        //customerJDBCRepository.deleteAll();    //@AfterAll stop으로 처리
    }

    @AfterAll
    void cleanup(){
        embeddedMysql.stop();
    }

    @Test
    @Order(1)   // 여러 test method의 실행 순서를 줄 수 있는 annotation
    @DisplayName("HikariConnectionPool 정상동작 test")
    public void testHikariConnectionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @Order(2)
    @DisplayName("고객을 추가할 수 있다. ")
    public void testInsert() {

        customerJDBCRepository.insert(newCustomer);   //insert 안 되면 customerJDBCRepository에서 throw 가 된다.

        var retrievedCustomer = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));   //samePropertyValuesAs 두 개가 같은지 비교

    }

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다. ")
    public void testFindAll() {
        var customers = customerJDBCRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("이름으로 고객을 조회할 수 있다. ")
    public void testFindByName() throws InterruptedException {
        var customer = customerJDBCRepository.findByName(newCustomer.getName());
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJDBCRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(5)
    @DisplayName("이메일로 고객을 조회할 수 있다. ")
    public void testFindByEmail()  {
        var customer = customerJDBCRepository.findByEmail(newCustomer.getEmail());
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJDBCRepository.findByEmail("unknown-user@gmail.com");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(6)
    @DisplayName("고객을 수정할 수 있다. ")
    public void testUpdate()  {
        newCustomer.changeName("updated-user");
        customerJDBCRepository.update(newCustomer);

        var all = customerJDBCRepository.findAll();
        assertThat(all, hasSize(1));
        assertThat(all, everyItem(samePropertyValuesAs(newCustomer)));

        var retrievedCustomer = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));

    }

}