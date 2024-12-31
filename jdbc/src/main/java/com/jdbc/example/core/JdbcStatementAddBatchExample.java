package com.jdbc.example.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 功能：Jdbc Statement 示例 使用 MySQL 演示
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/8 上午7:52
 */
public class JdbcStatementAddBatchExample {

    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf8";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // 获得数据库连接
            conn = DriverManager.getConnection(URL, "root", "root");
            // 创建 Statement
            Statement statement = conn.createStatement();
            // 批量SQL
            statement.addBatch("INSERT INTO tb_test (id, name) VALUES ('1', '1')");
            statement.addBatch("INSERT INTO tb_test (id, name) VALUES ('2', '2')");
            statement.addBatch("INSERT INTO tb_test (id, name) VALUES ('3', '3')");
            int[] updateCounts = statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

