package com.zs.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: zsq-1186
 * Date: 2017-08-16-15:04
 */

@Component
public class ScheduledTask {

    Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    @Scheduled(cron="0/10 * *  * * ? ")   //每10秒执行一次
    public  void execTask(){
        Thread current = Thread.currentThread();
        logger.info("ScheduledTest.executeFileDownLoadTask 定时任务1:"+current.getId()+ ",name:"+current.getName());
    }

}
