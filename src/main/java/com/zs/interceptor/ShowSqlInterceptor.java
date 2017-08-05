package com.zs.interceptor;


import com.alibaba.druid.sql.SQLUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.Properties;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-04
 * Time: 18:37
 */

public class ShowSqlInterceptor implements StatementInterceptor {
    Logger logger= LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public void init(Connection connection, Properties properties) throws SQLException {

    }

    @Override
    public ResultSetInternalMethods preProcess(String s, Statement statement, Connection connection) throws SQLException {
       if(StringUtils.isEmpty(s) && statement!=null){
           s=statement.toString();
           if(!StringUtils.isEmpty(s) && s.indexOf(":")>0){
               s= SQLUtils.formatMySql(s.substring(s.indexOf(":")+1).trim());
           }
           if(!StringUtils.isEmpty(s)){
               logger.info(s);
           }
       }
        return null;
    }

    @Override
    public ResultSetInternalMethods postProcess(String s, Statement statement, ResultSetInternalMethods resultSetInternalMethods, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override
    public void destroy() {

    }
}
