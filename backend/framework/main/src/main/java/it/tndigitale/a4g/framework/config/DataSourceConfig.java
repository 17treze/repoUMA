package it.tndigitale.a4g.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackages = {"it.tndigitale.a4g"})
@ConditionalOnProperty(prefix = "it.tndigit", name="database.multidatasource", havingValue="true")
public class DataSourceConfig {
    @Bean(name = "primaryDataSourceProperties")
    @ConfigurationProperties("primary.datasource")
    @Primary
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "primaryDataSource")
    @ConfigurationProperties("primary.datasource.configuration")
    @Primary
    public DataSource primaryDataSource() {
        DataSourceProperties dataSourceProperties = primaryDataSourceProperties();

        return Optional.ofNullable(dataSourceProperties.getJndiName())
                .map(this::toJndiDataSource)
                .orElseGet(() ->  dataSourceProperties.initializeDataSourceBuilder().build());
    }

    @Bean(name = "secondaryDataSourceProperties")
    @ConfigurationProperties("secondary.datasource")
    public DataSourceProperties secondarySourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties("secondary.datasource.configuration")
    public DataSource secondaryDataSource() {
        DataSourceProperties dataSourceProperties = secondarySourceProperties();

        return Optional.ofNullable(dataSourceProperties.getJndiName())
                .map(this::toJndiDataSource)
                .orElseGet(() ->  dataSourceProperties.initializeDataSourceBuilder().build());
    }

    protected DataSource toJndiDataSource(String jndiName) {
        return new JndiDataSourceLookup().getDataSource(jndiName);
    }
}
