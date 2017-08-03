package com.zs.config;

import com.zs.interceptor.BaseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
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
