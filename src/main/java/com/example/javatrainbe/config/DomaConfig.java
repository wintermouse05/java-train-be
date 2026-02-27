package com.example.javatrainbe.config;

import com.example.javatrainbe.dao.StudentDao;
import com.example.javatrainbe.dao.StudentDaoImpl;
import com.example.javatrainbe.dao.StudentInfoDao;
import com.example.javatrainbe.dao.StudentInfoDaoImpl;
import com.example.javatrainbe.dao.UserDao;
import com.example.javatrainbe.dao.UserDaoImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Doma2 Configuration
 * Cấu hình DataSource và đăng ký DAO implementations
 */
@Configuration
@EnableTransactionManagement
public class DomaConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.connection-timeout:30000}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.initialization-fail-timeout:120000}")
    private long initializationFailTimeout;

    @Value("${spring.datasource.hikari.maximum-pool-size:5}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle:1}")
    private int minimumIdle;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setInitializationFailTimeout(initializationFailTimeout);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setValidationTimeout(5000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public Config config(DataSource dataSource) {
        return new Config() {
            @Override
            public Dialect getDialect() {
                return new MysqlDialect();
            }

            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        };
    }

    /**
     * Đăng ký StudentDao implementation
     */
    @Bean
    public StudentDao studentDao(Config config) {
        return new StudentDaoImpl(config);
    }

    /**
     * Đăng ký StudentInfoDao implementation
     */
    @Bean
    public StudentInfoDao studentInfoDao(Config config) {
        return new StudentInfoDaoImpl(config);
    }

    /**
     * Đăng ký UserDao implementation
     */
    @Bean
    public UserDao userDao(Config config) {
        return new UserDaoImpl(config);
    }
}
