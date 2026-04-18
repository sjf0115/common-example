package com.example;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 功能：withValue 示例
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/18 10:17
 */
public class ConfigValueDeleteExample {

    public static void print(Config config) {
        System.out.println(config);
        System.out.println("-----------------------------------------------------------");
    }

    // 删除单个配置值
    public static void removePath(Config config, String path) {
        Config newConfig = config.withoutPath(path);
        print(newConfig);
    }

    // 批量删除多个配置项
    public static void removeMultiple(Config config, List<String> paths) {
        Config newConfig = config;
        for (String path : paths) {
            newConfig = newConfig.withoutPath(path);
        }
        print(newConfig);
    }

    // 设置NULL
    public static void setNull(Config config, String path) {
        Config newConfig = config.withValue(path, ConfigValueFactory.fromAnyRef(null));
        print(newConfig);
    }

    public static void main(String[] args) {
        String str = "server { host = \"localhost\", port = 8080, enabled = true }";
        Config config = ConfigFactory.parseString(str);
        removePath(config, "server.enabled");
        removeMultiple(config, Lists.newArrayList("server.enabled", "server.port"));
        setNull(config, "server.host");
    }
}
