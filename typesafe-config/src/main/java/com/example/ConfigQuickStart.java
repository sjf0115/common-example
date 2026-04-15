package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

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

        // 嵌套配置读取
        Config dbConfig = config.getConfig("database-var");
        String mavenHome = dbConfig.getString("maven-home");
        System.out.println( "mavenHome: " + mavenHome);

        // 或者直接使用路径
        String poolMax = config.getString("database.pool.max");
        System.out.println( "poolMax: " + poolMax);
    }
}
