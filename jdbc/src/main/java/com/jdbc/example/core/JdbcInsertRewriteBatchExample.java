package com.jdbc.example.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 功能：Jdbc Batch Insert rewriteBatchedStatements 示例 使用 MySQL 演示
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/8 上午7:52
 */
public class JdbcInsertRewriteBatchExample {

    private static final String URL = "jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true&useSSL=false&characterEncoding=utf8";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // 获得数据库连接
            conn = DriverManager.getConnection(URL, "root", "root");
            // 查询 SQL
            String sql = "INSERT INTO tb_test(id, name) VALUES(?,?)";
            // 创建 PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 1; i < 4 ;i++) {
                ps.setInt(1, i);
                ps.setString(2, i+"");
                ps.addBatch();
            }
            // 执行更新
            int[] result = ps.executeBatch();
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

