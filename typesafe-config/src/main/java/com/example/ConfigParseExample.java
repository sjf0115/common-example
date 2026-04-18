package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static void print(Config config) {
        System.out.println(config);
        System.out.println("-----------------------------------------------------------");
    }

    // 1. 从字符串解析（最灵活）
    public static void parseString() {
        // HOCON 格式
        String hoconConfigStr = "database = { host = \"localhost\", user = \"admin\" }";
        Config hoconConfig = ConfigFactory.parseString(hoconConfigStr);
        print(hoconConfig);

        // JSON 格式
        String jsonConfigStr = "{\"database\":{\"port\":8080}}";
        Config jsonConfig = ConfigFactory.parseString(jsonConfigStr);
        print(jsonConfig);
    }

    // 2. 从 Map 构建
    public static void parseMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("database.port", 8080);
        map.put("database.host", "localhost");
        map.put("tables", Arrays.asList("t_a", "t_b"));
        Config mapConfig = ConfigFactory.parseMap(map);
        print(mapConfig);
    }

    // 3. 从 Properties 构建
    public static void parseProperties() {
        Properties props = new Properties();
        props.setProperty("database.port", "8080");
        props.setProperty("database.host", "localhost");
        Config propsConfig = ConfigFactory.parseProperties(props);
        print(propsConfig);
    }

    // 4. 从文件系统加载
    public static void parseFile() {
        Config config = ConfigFactory.parseFile(new File("/opt/config/test.conf"));
        print(config);
    }

    // 4. 从 Resource 加载
    public static void parseResources() {
        // 加载 src/main/resources/my_application.conf
        Config config = ConfigFactory.parseResources("my_application.conf").resolve();
        print(config);

        // 指定 ClassLoader 加载资源，适用于 OSGi、多模块、插件化架构
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Config config1 = ConfigFactory.parseResources(classLoader, "my_application.conf");
        print(config1);
    }

    // 5. 从URL加载
    public static void parseURL() {
        // 从本地文件 URL 加载，也可以指定从 HTTP URL 加载配置
        URL url = null;
        try {
            url = new URL("file:///opt/config/test.conf");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Config config = ConfigFactory.parseURL(url);
        print(config);
    }

    // 6. 从环境变量构建
    public static void parseSystemEnvironment() {
        Config envConfig = ConfigFactory.systemEnvironment();   // 环境变量
        print(envConfig);
    }

    // 合并多个动态源
    /*public static void merge() {
        Config config1 = ConfigFactory.parseString("{k1=1}");
        Config config2 = ConfigFactory.parseString("{k1=11,k2=2}");
        Config config3 = ConfigFactory.parseString("{k1=12,k2=21,k3=3}");

        // 优先级：config1 > config2 > config3
        Config finalConfig = config1.withFallback(config2).withFallback(config3);
        System.out.println("k1=" + finalConfig.getString("k1") + ", k2=" + finalConfig.getString("k2") + ", k3=" + finalConfig.getString("k3"));
    }*/

    public static void main(String[] args) {
//        parseString();
//        parseMap();
//        parseProperties();
//        parseFile();
//        parseURL();
//        parseResources();
//        parseSystemEnvironment();
    }
}
