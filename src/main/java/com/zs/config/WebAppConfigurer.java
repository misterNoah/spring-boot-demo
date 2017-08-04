package com.zs.config;

import com.zs.interceptor.BaseInterceptor;
import com.zs.interceptor.ShowSqlInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 启动加载
 * Created by zhangshuqing on 2017/8/3.
 */

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    BaseInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(interceptor).addPathPatterns("/**");
       super.addInterceptors(registry);
    }


}
