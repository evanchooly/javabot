package javabot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ApplicationContext {
//    @Value("#{jdbc.url}")
//    private String jdbcUrl;
//    @Value("#{jdbc.username}")
//    private String username;
//    @Value("#{jdbc.password}")
//    private String password;
//
//    @Bean
//    public DataSource dataSource() {
//        return new DriverManagerDataSource(jdbcUrl, username, password);
//    }
}
