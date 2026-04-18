package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 功能：加载配置示例
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/16 22:44
 */
public class ConfigLoadExample {
    public static void main(String[] args) {
        // 加载配置（默认加载 application.conf）
        Config config = ConfigFactory.load();
        int port = config.getInt("database.port");
        System.out.println("port: " + port);

        // 2. 加载指定名称的配置文件（加载 myapp.conf）
        Config customConfig = ConfigFactory.load("my_application.conf");
        int port2 = customConfig.getInt("database.port");
        System.out.println("port2: " + port2);
    }
}
