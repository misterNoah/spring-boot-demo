package com.zs.interceptor;

import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangshuqing on 2017/8/3.
 */

@Component
public class BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        bindLogger();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


    private void bindLogger(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = request.getMethod();
        String ip=request.getRemoteAddr();
        UserAgent userAgent=UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String browser=userAgent.getBrowser().toString();
        String operation=userAgent.getOperatingSystem().toString();
        MDC.put("USER_IP","【用户IP:"+ip+"】");
        MDC.put("BROWSER","【浏览器:"+browser+"】");
        MDC.put("OPERATION","【操作系统:"+operation+"】");
    }



}
