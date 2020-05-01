package umari.datafilter;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import umari.datafilter.impl.UdfJpaTemplateImpl;
import umari.datafilter.service.UdfTemplate;
import umari.datafilter.sql.SqlContext;
import umari.datafilter.sql.h2.H2ContextImpl;
import umari.datafilter.sql.mysql.MySQLContextImpl;
import umari.datafilter.sql.oracle.OracleContextImpl;
import umari.datafilter.sql.postgre.PostgreContextImpl;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@AutoConfigurationPackage
public class UdfConfig {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UdfConfig.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DataSource dataSource;

    @Bean
    public UdfTemplate getUdfTemplate() {
        return new UdfJpaTemplateImpl();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "org.h2.Driver", matchIfMissing = true)
    public SqlContext getH2SqlContext() {
        log.info("SqlContext: H2");
        return new H2ContextImpl(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "com.mysql.jdbc.Driver")
    public SqlContext getMysqlSqlContext() {
        log.info("SqlContext: MySQL");
        return new MySQLContextImpl(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "oracle.jdbc.OracleDriver")
    public SqlContext getOracleSqlContext() {
        log.info("SqlContext: Oracle");
        return new OracleContextImpl(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "org.postgresql.Driver")
    public SqlContext getPostgreSqlContext() {
        log.info("SqlContext: Oracle");
        return new PostgreContextImpl(jdbcTemplate);
    }


}
