package com.example.druid.mysql;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlCreateTableParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.example.druid.core.CreateSQLParser;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:23
 */
public class MySQLCreateParser implements CreateSQLParser {
    @Override
    public void parse(String sql) {
        MySqlCreateTableParser parser = new MySqlCreateTableParser(sql);
        SQLCreateTableStatement createTableStatement = parser.parseCreateTable();
        // 使用visitor来访问AST
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        createTableStatement.accept(visitor);
    }
}
