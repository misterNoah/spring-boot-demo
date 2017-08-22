package com.zs.schedule;

import com.zs.schedule.common.SpringContextHolder;
import com.zs.schedule.quartz.Quartz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: zsq-1186
 * Date: 2017-08-22-15:23
 */
@Component
public class Scheduler implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 应用启动时调用该方法
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("onApplicationEvent......");
        String[] names = SpringContextHolder.getApplicationContext().getBeanNamesForType(Quartz.class);
        for (String name:names){
            logger.warn(name);
        }
    }
}
