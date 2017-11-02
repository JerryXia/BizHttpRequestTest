package com.guqiankun.ssmtemplate.infrastructure.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DateTimeTypeHandler extends BaseTypeHandler<DateTime> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DateTime parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getMillis()));
    }

    @Override
    public DateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toDateTime(rs.getTimestamp(columnName));
    }

    @Override
    public DateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toDateTime(rs.getTimestamp(columnIndex));
    }

    @Override
    public DateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toDateTime(cs.getTimestamp(columnIndex));
    }

    private static DateTime toDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            return new DateTime(timestamp.getTime(), DateTimeZone.UTC);
        } else {
            return null;
        }
    }
}