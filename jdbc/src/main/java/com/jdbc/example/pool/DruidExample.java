package com.jdbc.example.pool;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 功能：Druid 连接池 - 编程方式
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/12/18 23:08
 */
public class DruidExample {
    public static void main(String[] args) {
        // 创建 Druid 连接池对象
        DruidDataSource dataSource = new DruidDataSource();

        // 配置数据库连接参数
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 配置连接池参数
        dataSource.setInitialSize(5); // 初始化连接数
        dataSource.setMinIdle(5);     // 最小空闲连接数
        dataSource.setMaxActive(20);  // 最大活跃连接数
        dataSource.setMaxWait(60000); // 获取连接的最大等待时间（毫秒）

        // 其他可选配置
        dataSource.setTimeBetweenEvictionRunsMillis(60000); // 连接池检查连接的间隔时间
        dataSource.setMinEvictableIdleTimeMillis(300000);  // 连接池中连接空闲的最小时间
        dataSource.setValidationQuery("SELECT 1");         // 验证连接是否有效的 SQL 语句
        dataSource.setTestWhileIdle(true);                 // 空闲时是否进行连接的验证

        // 使用连接池对象进行数据库操作
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from tb_user");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                System.out.println("id: " + id + ", name: " + name + ", age: " + age + ", email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接池
            dataSource.close();
        }
    }
}
