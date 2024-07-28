package com.example.druid.parser.bean;

import java.util.List;

/**
 * 功能：表
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 22:28
 */
public class Table<T> {
    private String tableName;
    private String tableComment;
    private List<T> fields;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<T> getFields() {
        return fields;
    }

    public void setFields(List<T> fields) {
        this.fields = fields;
    }
}
