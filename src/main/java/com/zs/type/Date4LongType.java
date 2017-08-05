package com.zs.type;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-05
 * Time: 9:13
 */

import java.util.Date;

/**
 * 存入数据为long 取出来为date
 */
public class Date4LongType {
    private Date currentTime;

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
