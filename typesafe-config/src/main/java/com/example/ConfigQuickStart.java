package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

/**
 * 功能：快速入门
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/15 23:00
 */
public class ConfigQuickStart {
    public static void main(String[] args) {
        // 加载配置（默认加载 application.conf）
        Config config = ConfigFactory.load();

        // 基本类型读取
        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        System.out.println("host: " + host + ", port: " + port);

        // 使用嵌套配置读取
        Config dbConfig = config.getConfig("database");
        String host2 = dbConfig.getString("host");
        System.out.println( "host2: " + host2);

        // 或者直接使用路径
        String poolMax = config.getString("database.pool.max");
        System.out.println( "poolMax: " + poolMax);

        // 读取列表
        List<String> tables = config.getStringList("database.tables");
        System.out.println( "tables: " + tables);
    }
}
