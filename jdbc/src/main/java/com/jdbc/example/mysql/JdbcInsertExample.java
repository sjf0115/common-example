package com.jdbc.example.mysql;


import com.jdbc.example.bean.Book;

import java.sql.*;
import java.util.Objects;

/**
 * 功能：Jdbc Insert 示例 使用 MySQL 演示
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/8 上午7:52
 */
public class JdbcInsertExample {

    // MySQL Server 8.0 以下版本
    // private static final String DRIVER = "com.mysql.jdbc.Driver";
    // private static final String URL = "jdbc:mysql://localhost:3306/test";

    // MySQL Server 8.0 以上版本
    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf8";

    public static void main(String[] args) {
        Book book = new Book();
        book.setType("计算机理论");
        book.setName("深入理解 Jdbc");
        book.setDescription("深入了解 JDBC 的好书");

        Connection conn = null;
        try {
            // 获得数据库连接
            conn = DriverManager.getConnection(URL, "root", "root");
            // 查询 SQL
            String sql = "INSERT INTO tb_book(type, name, description) VALUES(?,?,?)";
            // 创建 PreparedStatement
            PreparedStatement statement = conn.prepareStatement(sql);
            // 设置输入参数
            statement.setString(1, book.getType());
            statement.setString(2, book.getName());
            statement.setString(3, book.getDescription());
            // 执行更新
            int num = statement.executeUpdate();
            System.out.println("更新" + num + "条记录");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            try {
                if (!Objects.equals(conn, null)) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

