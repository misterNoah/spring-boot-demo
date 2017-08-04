package com.zs.handler;

import com.zs.bean.User;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-04
 * Time: 17:36
 *需要将 User中的
 * 1.date 类型 存入数据为 Long 类型的毫秒数，读取的时候转化为Data类型
 * 2.interest 属性以{xxx,xxx,xxx}存入数据库  读取的时候能够转化成 java.utlil.List类型
 */
public class UserHandler extends BaseTypeHandler<User> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, User user, JdbcType jdbcType) throws SQLException {
        preparedStatement.setLong(i,user.getRegTime().getTime());
    }

    @Override
    public User getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return null;
    }

    @Override
    public User getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public User getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
