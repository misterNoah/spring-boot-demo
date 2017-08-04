package com.zs.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-04
 * Time: 17:44
 */

@Configuration
@EnableCaching
@MapperScan(basePackages = "com.zs.repository")
public class DataSourceInit {

    @Bean
    @ConfigurationProperties(prefix = "druid") // 加载以 druid 开头的配置
    public DataSource setupDruid() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

}
