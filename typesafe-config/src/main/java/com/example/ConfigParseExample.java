package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 功能：动态构建配置示例
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/16 22:44
 */
public class ConfigParseExample {
    public static void main(String[] args) {
        // 1. 从字符串解析（最灵活）
        // HOCON 格式
        String hoconConfigStr = "database = { url = \"jdbc:mysql://localhost/test\", user = \"admin\" }";
        Config hoconConfig = ConfigFactory.parseString(hoconConfigStr);
        // jdbc:mysql://localhost/test
        System.out.println(hoconConfig.getString("database.url"));

        // JSON 格式
        String jsonConfigStr = "{\"server\":{\"port\":8080}}";
        Config jsonConfig = ConfigFactory.parseString(jsonConfigStr);
        // 8080
        System.out.println(jsonConfig.getInt("server.port"));

        // 2. 从 Map 构建
        Map<String, Object> map = new HashMap<>();
        map.put("app.name", "dynamic-app");
        map.put("thread-pool.size", 10);
        map.put("features.enabled", Arrays.asList("logging", "metrics"));
        Config mapConfig = ConfigFactory.parseMap(map);
        System.out.println(mapConfig.getString("app.name"));

        // 3. 从 Properties 构建
        Properties props = new Properties();
        props.setProperty("akka.loglevel", "DEBUG");
        Config propsConfig = ConfigFactory.parseProperties(props);
        System.out.println(propsConfig.getString("akka.loglevel"));

        // 4. 从环境变量构建
        Config envConfig = ConfigFactory.systemEnvironment();   // 环境变量
        System.out.println(envConfig.getString("MAVEN_HOME"));

        // 4. 合并多个动态源
        Config config1 = ConfigFactory.parseString("{k1=1}");
        Config config2 = ConfigFactory.parseString("{k1=11,k2=2}");
        Config config3 = ConfigFactory.parseString("{k1=12,k2=21,k3=3}");

        // 优先级：config1 > config2 > config3
        Config finalConfig = config1.withFallback(config2).withFallback(config3);
        System.out.println("k1=" + finalConfig.getString("k1") + ", k2=" + finalConfig.getString("k2") + ", k3=" + finalConfig.getString("k3"));
    }
}
