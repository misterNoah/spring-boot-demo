package com.zs.handler;

import com.zs.type.Date4LongType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-05
 * Time: 9:16
 */
public class Date4LongHandler extends BaseTypeHandler<Date4LongType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date4LongType parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i,parameter.getCurrentTime().getTime());
    }

    @Override
    public Date4LongType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getReturnValue(rs.getLong(columnName));
    }

    @Override
    public Date4LongType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getReturnValue(rs.getLong(columnIndex));
    }

    @Override
    public Date4LongType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getReturnValue(cs.getLong(columnIndex));
    }

    private Date4LongType getReturnValue(Long value){
        Date4LongType type=new Date4LongType();
        type.setCurrentTime(new Date(value));
        return  type;
    }
}
