package com.example.druid.parser.mysql;

import com.example.druid.parser.bean.MySQLTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 功能：创建语句解析示例
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:30
 */
public class CreateSQLTest {

    private static final Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {
        MySQLParserFactory factory = new MySQLParserFactory();
        MySQLCreateParser parser = (MySQLCreateParser)factory.createSQLParser();
        String sql = "CREATE TABLE IF NOT EXISTS `profile_meta_user`(\n" +
                "    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '自增ID',\n" +
                "    `status` INT NOT NULL DEFAULT 1 COMMENT '状态:1-启用,2-停用',\n" +
                "    `user_id` VARCHAR(40) NOT NULL COMMENT '用户ID',\n" +
                "    `user_name` VARCHAR(100) NOT NULL COMMENT '用户名称',\n" +
                "    `password` VARCHAR(100) NOT NULL COMMENT '密码',\n" +
                "    `source_type` INT NOT NULL DEFAULT 1 COMMENT '创建方式: 1-系统内置,2-自定义',\n" +
                "    `creator` VARCHAR(100) NOT NULL COMMENT '创建者',\n" +
                "    `modifier` VARCHAR(100) NOT NULL COMMENT '修改者',\n" +
                "    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                "    index idx_gmt_modified(`gmt_modified`),\n" +
                "    index idx_user_id(`user_id`),\n" +
                "    PRIMARY KEY (`id`)\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '画像-用户';";
        MySQLTable table = parser.parse(sql);
        String result = gson.toJson(table);
        System.out.println(result);
    }
}
