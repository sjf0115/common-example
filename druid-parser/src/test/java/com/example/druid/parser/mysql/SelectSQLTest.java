package com.example.druid.parser.mysql;

import com.example.druid.parser.bean.MySQLSelect;
import com.example.druid.parser.bean.MySQLTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

/**
 * 功能：查询语句解析示例
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:30
 */
public class SelectSQLTest {

    private static final Gson gson = new GsonBuilder().create();

    @Test
    public static void testSimpleSelect() {
        MySQLParserFactory factory = new MySQLParserFactory();
        MySQLSelectParser parser = (MySQLSelectParser)factory.selectSQLParser();
        String sql = "SELECT * FROM user_profile_user";
        MySQLSelect select = parser.parse(sql);
        System.out.println(gson.toJson(select));
    }
}
