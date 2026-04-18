package com.example;

import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import java.util.Map;

/**
 * 功能：withValue 示例
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/18 10:17
 */
public class ConfigValueAddExample {

    public static void print(Config config) {
        System.out.println(config);
        System.out.println("-----------------------------------------------------------");
    }

    // 增加单个配置值
    public static void addConfigValue(Config config) {
        Config newConfig = config.withValue("server.url", ConfigValueFactory.fromAnyRef("www.baidu.com"));
        print(newConfig);
    }

    // 增加多个配置值
    public static void addMultipleConfigValue(Config config) {
        Map<String, Object> objects = Maps.newHashMap();
        objects.put("server.url", "www.baidu.com2");
        objects.put("server.password", "123456");

        Config newConfig = config;
        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            newConfig = newConfig.withValue(entry.getKey(), ConfigValueFactory.fromAnyRef(entry.getValue()));
        }
        print(newConfig);
    }

    // 批量添加（使用 parseMap + withFallback，推荐）
    public static void batchAdd(Config config) {
        Map<String, Object> objects = Maps.newHashMap();
        objects.put("server.url", "www.baidu.com2");
        objects.put("server.password", "123456");

        // 将更新转为 Config，优先级更高
        Config overrides = ConfigFactory.parseMap(objects);
        Config newConfig = overrides.withFallback(config).resolve();
        print(newConfig);
    }

    public static void main(String[] args) {
        String str = "server { host = \"localhost\", port = 8080, enabled = true }";
        Config config = ConfigFactory.parseString(str);
        addConfigValue(config);
        addMultipleConfigValue(config);
        batchAdd(config);
    }
}
