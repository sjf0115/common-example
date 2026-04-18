package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigMemorySize;
import com.typesafe.config.ConfigRenderOptions;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能：示例
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/18 22:17
 */
public class ConfigGetExample {

    // 基本类型读取
    public static void getPrimitives(Config config) {
        // 字符串
        String host = config.getString("database.host");
        System.out.println("host: " + host);

        // 整数（自动类型转换）
        int port = config.getInt("database.port");
        System.out.println("port: " + port);

        long max = config.getLong("database.pool.max");
        System.out.println("max: " + max);

        double ratio = config.getDouble("database.pool.ratio");
        System.out.println("ratio: " + ratio);

        // 布尔值（支持多种格式）
        boolean enabled = config.getBoolean("database.enabled");
        // 支持：true/false, yes/no, on/off
        System.out.println("enabled: " + enabled);

        // 枚举类型
        TimeUnit unit = config.getEnum(TimeUnit.class, "database.pool.unit");
        System.out.println("unit: " + unit);
    }

    // 特殊类型
    public static void getSpecialTypes(Config config) {
        // Duration 类型：支持人性化的时间格式
        Duration timeout = config.getDuration("database.pool.timeout");
        // 配置文件中可以写："10s", "5m", "1h", "2d", "500ms"
        System.out.println("timeout: " + timeout);

        // 获取特定单位
        long expire = config.getDuration("database.pool.expire", TimeUnit.MILLISECONDS);
        System.out.println("expire: " + expire);

        // MemorySize 类型：支持人性化的存储单位
        ConfigMemorySize cacheSize = config.getMemorySize("database.cache.maxSize");
        // 配置文件中可以写："512k", "10m", "1g", "2t"
        long bytes = cacheSize.toBytes();
        long bytesDirect = config.getBytes("database.cache.maxSize");
        System.out.println("bytes: " + bytes);
        System.out.println("bytesDirect: " + bytesDirect);
    }

    // 复杂类型
    public static void getComplexTypes(Config config) {
        // 字符串列表
        List<String> tables = config.getStringList("database.tables");
        System.out.println("tables: " + tables);

        // 整数列表
        List<Integer> ports = config.getIntList("server.ports");
        System.out.println("ports: " + ports);

        // Duration 列表
        List<Duration> intervals = config.getDurationList("retry.intervals");
        System.out.println("intervals: " + intervals);
    }

    public static String toHocon(Config config) {
        return config.root().render(ConfigRenderOptions.defaults()
                .setFormatted(true)        // 格式化缩进
                .setComments(true)         // 保留注释
                .setOriginComments(false)  // 不显示来源注释
                .setJson(false)            // 输出 HOCON，不是 JSON
        );
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("database.host", "localhost");
        map.put("database.port", 8080);
        map.put("database.enabled", true);
        map.put("database.pool.unit", "SECONDS");
        map.put("database.pool.max", 3600L);
        map.put("database.pool.ratio", 0.5);
        map.put("database.pool.expire", 1776522287);
        map.put("database.pool.timeout", "30s");
        map.put("database.cache.maxSize", "512k");
        map.put("database.tables", Arrays.asList("t_a", "t_b"));
        map.put("server.ports", Arrays.asList(8080, 8081));
        map.put("retry.intervals", Arrays.asList("10s", "15s"));

        Config config = ConfigFactory.parseMap(map);
        System.out.println(toHocon(config));

        getPrimitives(config);
        getSpecialTypes(config);
        getComplexTypes(config);
    }
}
