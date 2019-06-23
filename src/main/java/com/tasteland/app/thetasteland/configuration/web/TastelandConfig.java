package com.tasteland.app.thetasteland.configuration.web;

import com.jolbox.bonecp.BoneCPDataSource;
import com.tasteland.app.thetasteland.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.tasteland.app")
@PropertySource("classpath:persistence-mysql.properties")
public class TastelandConfig {

    private final PropertyUtils prop;

    @Autowired
    public TastelandConfig(PropertyUtils prop) {
        this.prop = prop;
    }

    @Bean
    public DataSource dataSource() {

        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(prop.get("jdbc.driver"));
        dataSource.setJdbcUrl(prop.decode("jdbc.url"));
        dataSource.setUser(prop.decode("jdbc.user"));
        dataSource.setPassword(prop.decode("jdbc.password"));
        dataSource.setIdleMaxAgeInSeconds(prop.getInt("connection.pool.maxIdleTime"));
        dataSource.setMaxConnectionsPerPartition(prop.getInt("connection.pool.maxPoolSize"));
        dataSource.setMinConnectionsPerPartition(prop.getInt("connection.pool.minPoolSize"));
        return dataSource;

    }
    private Properties setJpaProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", prop.get("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", prop.get("hibernate.show_sql"));
        props.setProperty("hibernate.format_sql", prop.get("hibernate.format_sql"));
        return props;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(prop.get("hibernate.packagesToScan"));
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setJpaProperties(setJpaProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}

