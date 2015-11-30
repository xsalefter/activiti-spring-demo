package com.baeldung.activitispringdemo.spring;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(value={"com.baeldung.activitispringdemo.activiti", "com.baeldung.activitispringdemo.spring"})
@EnableJpaRepositories(value={"com.baeldung.activitispringdemo.repository"})
@EnableTransactionManagement
public class ApplicationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    public static final String ACTIVITI_JDBC_URL = "jdbc:h2:mem:activiti_workflow;DB_CLOSE_DELAY=10000";
    public static final String ACTIVITI_JDBC_USER = "sa";
    public static final String ACTIVITI_JDBC_PASSWORD = "";
    public static final String APP_JDBC_URL = "jdbc:h2:mem:activiti_app;DB_CLOSE_DELAY=10000";
    public static final String APP_JDBC_USER = "sa";
    public static final String APP_JDBC_PASSWORD = "";

    public static final Resource[] BPMN_FILE_RESOURCES = new Resource[] {
        new ClassPathResource("./reinsurance-demo.bpmn20.xml")
    };

    @Bean(name="workFlowDataSource")
    public DataSource workFlowDataSource() {
        return createDataSource(ACTIVITI_JDBC_URL, ACTIVITI_JDBC_USER, ACTIVITI_JDBC_PASSWORD);
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(@Qualifier("workFlowDataSource") DataSource dataSource) {
        logger.info(">> Initialize processEngineConfiguration()");
        final PlatformTransactionManager tx = new DataSourceTransactionManager(dataSource);

        final SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setTransactionManager(tx);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setDeploymentResources(BPMN_FILE_RESOURCES);

        return config;
    }

    @Bean
    public ProcessEngine processEngine(SpringProcessEngineConfiguration config) 
    throws Exception {
        logger.info(">> Initialize processEngine() with config: {}", config);

        final ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(config);
        return factoryBean.getObject();
    }

    // ----- Spring Data JPA ----- //

    @Bean(name="appDataSource")
    public DataSource appDataSource() {
        return createDataSource(APP_JDBC_URL, APP_JDBC_USER, APP_JDBC_PASSWORD);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("appDataSource") DataSource dataSource) {
        logger.info(">> Initialize entityManagerFactory()");

        final LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setPackagesToScan("com.baeldung.activitispringdemo.entity");
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfb.setJpaProperties(createJpaProperties());
        return emfb;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        logger.info(">> Initialize transactionManager() with emf: {}, and emf properties: {}", emf, emf.getProperties());

       JpaTransactionManager transactionManager = new JpaTransactionManager();
       transactionManager.setEntityManagerFactory(emf);
  
       return transactionManager;
    }

    private static DataSource createDataSource(final String jdbcUrl, final String username, final String password) {
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(10);
        ds.setJdbcUrl(jdbcUrl);
        ds.addDataSourceProperty("user", username);
        ds.addDataSourceProperty("password", password);
        ds.addDataSourceProperty("cachePrepStmts", true);
        ds.addDataSourceProperty("prepStmtCacheSize", 250);
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        ds.addDataSourceProperty("useServerPrepStmts", true);
        return ds;
    }

    private static Properties createJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return properties;
    }
}
