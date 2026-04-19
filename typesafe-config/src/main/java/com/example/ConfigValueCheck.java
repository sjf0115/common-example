package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 功能：路径检查方法
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/19 15:42
 */
public class ConfigValueCheck {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("database.port", 8080);
        map.put("database.host", "localhost");
        map.put("database.user", null);
        map.put("tables", Arrays.asList("t_a", "t_b"));
        Config config = ConfigFactory.parseMap(map);

        // 检查路径是否存在
        if (config.hasPath("database.port")) {
            int port = config.getInt("database.port");
            System.out.println("port: " + port);
        }

        // 检查是否为 null
        if (config.getIsNull("database.user")) {
            System.out.println("user is null");
        }

        // 使用 Optional 模式
        Optional<String> value = config.hasPath("database.host") ? Optional.of(config.getString("database.host")) : Optional.empty();
        System.out.println("host: " + value.get());
    }
}
