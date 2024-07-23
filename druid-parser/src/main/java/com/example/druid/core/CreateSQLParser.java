package com.example.druid.core;

/**
 * 功能：创建语句解析
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:21
 */
public interface CreateSQLParser<T> {
    public T parse(String sql);
}
