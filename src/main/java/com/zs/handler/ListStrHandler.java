package com.zs.handler;

import com.alibaba.fastjson.JSON;
import com.zs.type.ListStrType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-05
 * Time: 9:16
 */
public class ListStrHandler extends BaseTypeHandler<ListStrType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ListStrType parameter, JdbcType jdbcType) throws SQLException {
        List<String> values=parameter.getValues();
        StringBuilder sb=new StringBuilder();
        ps.setString(i,JSON.toJSONString(values));
    }

    @Override
    public ListStrType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getReturnResult(rs.getString(columnName));
    }

    @Override
    public ListStrType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        return getReturnResult(rs.getString(columnIndex));
    }

    @Override
    public ListStrType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getReturnResult(cs.getString(columnIndex));
    }

    private ListStrType getReturnResult(String value){
        List<String> list=JSON.parseArray(value,String.class);
        ListStrType type=new ListStrType();
        type.setValues(list);
        return type;
    }
}
