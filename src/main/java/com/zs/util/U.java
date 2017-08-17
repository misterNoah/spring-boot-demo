package com.zs.util;

import org.springframework.util.StringUtils;

/**
 * Description:
 *
 * @author: zsq-1186
 * Date: 2017-08-17-11:43
 */
public class U {

    public static final String EMPTY="";

    public static boolean isNotBlank(String value){
        return !StringUtils.isEmpty(value);
    }

    public static boolean isBlank(String value){
        return StringUtils.isEmpty(value);
    }
}
