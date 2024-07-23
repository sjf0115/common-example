package com.example.druid.mysql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlCreateTableParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.example.druid.bean.MySQLTable;
import com.example.druid.bean.MySQLTableField;
import com.example.druid.core.CreateSQLParser;
import com.example.druid.utils.SQLExprValueExtractor;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 功能：MySQL 创建语句解析器
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:23
 */
public class MySQLCreateParser implements CreateSQLParser<MySQLTable> {
    @Override
    public MySQLTable parse(String sql) {
        MySqlCreateTableParser parser = new MySqlCreateTableParser(sql);
        SQLCreateTableStatement createTableStatement = parser.parseCreateTable();
        MySqlCreateTableStatement statement = (MySqlCreateTableStatement)createTableStatement;

        MySQLTable table = new MySQLTable();
        // 表信息
        String tableName = statement.getTableName();
        table.setTableName(tableName);
        String comment = (String) SQLExprValueExtractor.extract(statement.getComment());
        table.setTableComment(comment);
        // 字段信息
        List<MySQLTableField> fields = Lists.newArrayList();
        List<SQLColumnDefinition> columnDefinitions = statement.getColumnDefinitions();
        for (SQLColumnDefinition definition : columnDefinitions) {
            MySQLTableField field = new MySQLTableField();
            // 字段名称
            String columnName = definition.getColumnName();
            field.setFieldName(columnName);
            // 字段描述
            String columnComment = (String) SQLExprValueExtractor.extract(definition.getComment());
            field.setFieldComment(columnComment);
            // 字段数据类型
            String dataType = definition.getDataType().getName();
            field.setFieldType(dataType);
            // 默认值
            Integer extract = (Integer) SQLExprValueExtractor.extract(definition.getDefaultExpr());
            // 约束 NOT NULL
            List<SQLColumnConstraint> constraints = definition.getConstraints();
            // OnUpdate
            SQLExpr onUpdate = definition.getOnUpdate();

            fields.add(field);
        }
        table.setFields(fields);

        // 使用visitor来访问AST
        // MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        // createTableStatement.accept(visitor);

        return table;
    }
}
