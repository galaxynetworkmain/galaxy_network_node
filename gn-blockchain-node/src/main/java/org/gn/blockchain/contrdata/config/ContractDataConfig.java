package org.gn.blockchain.contrdata.config;

import org.gn.blockchain.config.SystemProperties;
import org.gn.blockchain.datasource.DbSource;
import org.gn.blockchain.datasource.leveldb.LevelDbDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.gn.blockchain.contrdata" })
public class ContractDataConfig
{
    @Bean
    public SystemProperties systemProperties() {
        return SystemProperties.getDefault();
    }
    
    @Bean
    public DbSource<byte[]> storageDict() {
        final DbSource<byte[]> dataSource = (DbSource<byte[]>)new LevelDbDataSource("storageDict");
        dataSource.init();
        return dataSource;
    }
}
