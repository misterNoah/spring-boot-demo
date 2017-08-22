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
 * 有时候需要实现动态定时任务，即工程启动后，可以实现启动和关闭任务，同时也可以设置定时计划。这就需要利用到quartz，spring官方对于这个包下面各类的介绍，后续抽空配置下这类业务的实现：
 http://docs.spring.io/spring/docs/3.2.x/javadoc-api/org/springframework/scheduling/quartz/package-summary.html。

 quartz API:
 http://www.quartz-scheduler.org/api/2.1.7/index.html
 *
 * @author: zsq-1186
 * Date: 2017-08-22-15:23
 */
@Component
public class Scheduler implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 应用启动时调用该方法，可以使用ApplicationListener来做一些初始化的工作
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
