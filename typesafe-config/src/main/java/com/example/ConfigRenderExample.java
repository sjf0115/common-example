package com.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：配置输出示例
 * 作者：@SmartSi
 * 博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2026/4/18 23:18
 */
public class ConfigRenderExample {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("database.port", 8080);
        map.put("database.host", "localhost");
        map.put("tables", Arrays.asList("t_a", "t_b"));
        Config config = ConfigFactory.parseMap(map);

        // 渲染为字符串
        String output = config.root().render();
        System.out.println(output);

        // 美观格式输出
        String pretty = config.root().render(
                ConfigRenderOptions.defaults()
                        .setFormatted(true) // 开启格式化（美化输出）
                        .setJson(false) // 输出 HOCON 格式（false为HOCON，true为JSON）
                        .setComments(false) // 去掉配置文件中手写的注释
                        .setOriginComments(false) // 关闭自动生成的“配置来源”注释
        );
        System.out.println(pretty);

        // 简洁格式（单行）
        String concise = config.root().render(ConfigRenderOptions.concise());
        System.out.println(concise);

        // 输出为 JSON
        String json = config.root().render(
                ConfigRenderOptions.defaults()
                        .setFormatted(true) // 开启格式化（美化输出）
                        .setJson(true) // 输出 JSON 格式（false为HOCON，true为JSON）
                        .setComments(false) // 去掉配置文件中手写的注释
                        .setOriginComments(false) // 关闭自动生成的“配置来源”注释
        );
        System.out.println(json);
    }
}
