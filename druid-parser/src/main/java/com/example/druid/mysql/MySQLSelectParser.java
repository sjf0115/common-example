package com.example.druid.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;
import com.example.druid.bean.MySQLSelect;
import com.example.druid.core.CreateSQLParser;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/24 23:08
 */
public class MySQLSelectParser implements CreateSQLParser<MySQLSelect> {
    @Override
    public MySQLSelect parse(String sql) {

        MySqlSelectParser parser = new MySqlSelectParser(sql);
        SQLSelect select = parser.select();
        SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {

        } else if (query instanceof SQLUnionQuery) {

        }
        return null;
    }
}
