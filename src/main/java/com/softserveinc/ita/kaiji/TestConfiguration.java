package com.softserveinc.ita.kaiji;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Kyrylo Bardachov
 * @version 1.0
 * @since 24.08.14.
 */

@Configuration
@Import({ContextConfiguration.class, SecurityConfiguration.class})
public class TestConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public EmbeddedDatabase db() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2)
                .addScript("file:src/test/resources/schema_h2.sql");
        return builder.build();
    }
 @Bean
 public DataSource dataSource() {
     return new EmbeddedDatabaseBuilder()
             .setType(EmbeddedDatabaseType.H2)
             .build();
 }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        return jpaVendorAdapter;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(
                "com.softserveinc.ita.kaiji");
        entityManagerFactoryBean.setJpaProperties(jpaProperties());
        return entityManagerFactoryBean;
    }

    private Properties jpaProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto","create");
        properties.put("hibernate.transaction.auto_close_session","true");
        properties.put("hibernate.hibernate.connection.release_mode","auto");
        return properties;
    }

}
