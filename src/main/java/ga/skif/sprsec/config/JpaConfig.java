package ga.skif.sprsec.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        basePackages = { "ga.skif.sprsec.services" }
        )
public class JpaConfig {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("ga.skif.sprsec")
                .persistenceUnit("user")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(

            @Qualifier("entityManagerFactory")
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
//    @Value("${dataSource.driverClassName}")
//    private String driver;
//    @Value("${dataSource.url}")
//    private String url;
//    @Value("${dataSource.username}")
//    private String username;
//    @Value("${dataSource.password}")
//    private String password;
//    @Value("${hibernate.dialect}")
//    private String dialect;
//    @Value("${hibernate.hbm2ddl.auto}")
//    private String hbm2ddlAuto;

//    @Bean
//    public DataSource configureDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName(driver);
//        config.setJdbcUrl(url);
//        config.setUsername(username);
//        config.setPassword(password);
//        return new HikariDataSource(config);
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactoryBean.setDataSource(configureDataSource());
//        entityManagerFactoryBean.setPackagesToScan("ga.saha.entities");
//        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        Properties jpaProperties = new Properties();
////        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, dialect);
//        jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, hbm2ddlAuto);
//        entityManagerFactoryBean.setJpaProperties(jpaProperties);
//        return entityManagerFactoryBean;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new JpaTransactionManager();
//    }
}