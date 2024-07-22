package com.example.druid.mysql;

import com.example.druid.core.CreateSQLParser;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:30
 */
public class CreateSQLSimple {
    public static void main(String[] args) {
        MySQLParserFactory factory = new MySQLParserFactory();
        CreateSQLParser parser = factory.createSQLParser();
        String sql = "";
        parser.parse(sql);
    }
}
