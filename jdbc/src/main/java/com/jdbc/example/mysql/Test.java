package com.jdbc.example.mysql;

import java.util.UUID;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/17 22:40
 */
public class Test {
    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
    }
}
