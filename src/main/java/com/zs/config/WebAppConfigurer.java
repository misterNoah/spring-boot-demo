package com.zs.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.zs.interceptor.BaseInterceptor;
import com.zs.interceptor.ShowSqlInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 启动加载
 * Created by zhangshuqing on 2017/8/3.
 */

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    BaseInterceptor interceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 从前台过来的数据转换成对应类型的转换器
        super.addFormatters(registry);

    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 默认转换器注册后, 插入自定义的请求响应转换器
        super.extendMessageConverters(converters);
        converters.add(new FastJsonHttpMessageConverter4());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(interceptor).addPathPatterns("/**");
       super.addInterceptors(registry);
    }

    /**
     *  自定义的returnHandler先级最低
     *  返回值的处理 spring mvc无法解析的返回值，可以利用这个来返回自定义的类型
     *
     */

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
        returnValueHandlers.add(new HandlerMethodReturnValueHandler() {
            @Override
            public boolean supportsReturnType(MethodParameter methodParameter) {
                return false;
            }

            @Override
            public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {

            }
        });
    }


    //可以对 用户自定义的参数或annotation进行解析
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter methodParameter) {
                return false;
            }

            @Override
            public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
                return null;
            }
        });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域处理
        super.addCorsMappings(registry);
        //registry.addMapping("/**").allowedMethods(Const.SUPPORT_METHODS);
    }

}
