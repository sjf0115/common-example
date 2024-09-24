package com.example.druid.parser.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;

/**
 * 功能：SQLStatement 示例
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/9/17 18:50
 */
public class SQLStatementDemo {
    public static void main(String[] args) {

        // 1. Create 语句
        String createSQL = "CREATE TABLE user (id int, name varchar(255), age int)";
        SQLStatement createStatement = SQLUtils.parseSingleMysqlStatement(createSQL);
        System.out.println("CREATE: " + (createStatement instanceof SQLCreateStatement)); // true

        // 2. Alter 语句
        String alterSQL = "ALTER TABLE user ADD age int;";
        SQLStatement alterStatement = SQLUtils.parseSingleMysqlStatement(alterSQL);
        System.out.println("ALTER: " + (alterStatement instanceof SQLAlterStatement)); // true

        // 3. Drop 语句
        String dropSQL = "DROP TABLE user";
        SQLStatement dropStatement = SQLUtils.parseSingleMysqlStatement(dropSQL);
        System.out.println("DROP: " + (dropStatement instanceof SQLDropStatement)); // true

        // 4. Insert 语句
        String insertSQL = "insert into user(id,name,age) values (1,'Lucy',21)";
        SQLStatement insertStatement = SQLUtils.parseSingleMysqlStatement(insertSQL);
        System.out.println("INSERT: " + (insertStatement instanceof SQLInsertStatement)); // true

        // 5. Update 语句
        String updateSQL = "update user set name = 'Tom' where id = 1";
        SQLStatement updateStatement = SQLUtils.parseSingleMysqlStatement(updateSQL);
        System.out.println("UPDATE: " + (updateStatement instanceof SQLUpdateStatement)); // true

        // 6. Delete 语句
        String deleteSQL = "delete from user where id = 1";
        SQLStatement deleteStatement = SQLUtils.parseSingleMysqlStatement(deleteSQL);
        System.out.println("DELETE: " + (deleteStatement instanceof SQLDeleteStatement)); // true

        // 7. Select 语句
        String selectSQL = "select id,name,age from user";
        SQLStatement selectStatement = SQLUtils.parseSingleMysqlStatement(selectSQL);
        System.out.println("SELECT: " + (selectStatement instanceof SQLSelectStatement)); // true
    }
}
