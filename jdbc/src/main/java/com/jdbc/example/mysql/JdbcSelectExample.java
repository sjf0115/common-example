package com.jdbc.example.mysql;


import com.jdbc.example.bean.Book;
import com.google.common.collect.Lists;

import java.sql.*;
import java.util.List;
import java.util.Objects;

/**
 * 功能：Jdbc SELECT 示例 使用 MySQL 演示
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/8 上午7:52
 */
public class JdbcSelectExample {

    // MySQL Server 8.0 以下版本
    // private static final String DRIVER = "com.mysql.jdbc.Driver";
    // private static final String URL = "jdbc:mysql://localhost:3306/test";

    // MySQL Server 8.0 以上版本
    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf8";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // JDBC4 之后不需要再显式通过 Class.forName 注册驱动
            // Class.forName(DRIVER);

            // 获得数据库连接
            conn = DriverManager.getConnection(URL, "root", "root");
            // 查询 SQL
            String sql = "SELECT id, type, name, description FROM tb_book where type = ?";
            // 创建 PreparedStatement
            PreparedStatement statement = conn.prepareStatement(sql);
            // 设置输入参数
            statement.setString(1, "计算机理论");
            // 执行查询
            ResultSet rs = statement.executeQuery();
            // 遍历查询结果
            List<Book> books = Lists.newArrayList();
            while(rs.next()){
                int id  = rs.getInt("id");
                String type = rs.getString("type");
                String name  = rs.getString("name");
                String description  = rs.getString("description");
                // 从数据库中取出结果并生成 Java 对象
                Book book = new Book();
                book.setId(id);
                book.setType(type);
                book.setName(name);
                book.setDescription(description);
                books.add(book);
            }
            // 输出结果
            for (Book book : books) {
                System.out.println("ID: " + book.getId() + ", Type: " + book.getType() + ", Name: " + book.getName() + ", Desc: " + book.getDescription());
            }
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

