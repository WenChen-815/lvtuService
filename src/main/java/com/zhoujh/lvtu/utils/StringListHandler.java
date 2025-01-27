package com.zhoujh.lvtu.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StringListHandler extends BaseTypeHandler<List<String>> {
    private static final Gson gson = new Gson();

    private final TypeToken<List<String>> typeToken;

    public StringListHandler() {
        // 使用 TypeToken 来指定 List 的类型
        this.typeToken = new TypeToken<List<String>>() {};
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        // 将 List<T> 转换为 JSON 字符串
        ps.setString(i, gson.toJson(parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 将数据库中的 JSON 字符串转换为 List<T>
        String json = rs.getString(columnName);
        return json == null ? null : gson.fromJson(json, typeToken.getType());
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 将数据库中的 JSON 字符串转换为 List<T>
        String json = rs.getString(columnIndex);
        return json == null ? null : gson.fromJson(json, typeToken.getType());
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 将数据库中的 JSON 字符串转换为 List<T>
        String json = cs.getString(columnIndex);
        return json == null ? null : gson.fromJson(json, typeToken.getType());
    }
}
