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
public class ConfigValueUpdateExample {

    public static void print(Config config) {
        System.out.println(config);
        System.out.println("-----------------------------------------------------------");
    }

    // 修改单个配置值(withValue)
    public static void modifyConfigValue(Config config) {
        Config newConfig = config.withValue("server.port", ConfigValueFactory.fromAnyRef(9090));
        print(newConfig);
    }

    // 修改多个配置值(withValue)
    public static void modifyMultipleConfigValue(Config config) {
        Map<String, Object> updates = Maps.newHashMap();
        updates.put("server.host", "127.0.0.1");
        updates.put("server.port", 1111);
        updates.put("server.enabled", false);

        Config newConfig = config;
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            newConfig = newConfig.withValue(entry.getKey(), ConfigValueFactory.fromAnyRef(entry.getValue()));
        }
        print(newConfig);
    }

    // 批量更新（使用 parseMap + withFallback，推荐）
    public static void batchUpdate(Config config) {
        Map<String, Object> updates = Maps.newHashMap();
        updates.put("server.host", "127.0.0.1");
        updates.put("server.port", 1111);
        updates.put("server.url", "www.baidu.com");

        // 将更新转为 Config，优先级更高
        Config overrides = ConfigFactory.parseMap(updates);

        // overrides 会覆盖 config 中的同名配置
        Config newConfig = overrides.withFallback(config).resolve();
        print(newConfig);
    }

    public static void main(String[] args) {
        String str = "server { host = \"localhost\", port = 8080, enabled = true }";
        Config config = ConfigFactory.parseString(str);
        modifyConfigValue(config);
        modifyMultipleConfigValue(config);
        batchUpdate(config);
    }
}
