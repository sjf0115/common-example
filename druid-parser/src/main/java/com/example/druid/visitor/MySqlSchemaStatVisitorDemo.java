package com.example.druid.visitor;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 功能：MySqlSchemaStatVisitor 示例
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/4 12:13
 */
public class MySqlSchemaStatVisitorDemo {
    public static void main(String[] args) {
        String sql = "select id as user_id, name as user_name from user where id > 1";

        // 新建 MySQL Parser
        MySqlStatementParser parser = new MySqlStatementParser(sql);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement statement = parser.parseStatement();

        // 使用visitor来访问AST 从visitor中拿出你所关注的信息
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);

        Map<TableStat.Name, TableStat> tables = visitor.getTables();
        for (TableStat.Name name : tables.keySet()) {
            TableStat tableStat = tables.get(name);
            System.out.println("TableName: " + name.getName());
        }

        // 列名
        Collection<TableStat.Column> columns = visitor.getColumns();
        for (TableStat.Column column : columns) {
            String name = column.getName();
            String fullName = column.getFullName();
            String dataType = column.getDataType();
            System.out.println("ColumnName: " + name + ", FullName: " + fullName + ", DataType: " + dataType);
        }

        // Where
        List<TableStat.Condition> conditions = visitor.getConditions();
        for (TableStat.Condition condition : conditions) {
            TableStat.Column column = condition.getColumn();
            String operator = condition.getOperator();
            List<Object> values = condition.getValues();
            System.out.println("ColumnName: " + column.getName() + ", Operator: " + operator + ", Values: " + values);
        }


        /*List<SQLStatement> statements = parser.parseStatementList();
        for (SQLStatement sqlStatement : statements) {

        }*/
    }
}
