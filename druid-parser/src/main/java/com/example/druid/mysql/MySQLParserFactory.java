package com.example.druid.mysql;

import com.example.druid.core.*;

/**
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 07:22
 */
public class MySQLParserFactory implements SQLParserFactory {
    @Override
    public CreateSQLParser createSQLParser() {
        return new MySQLCreateParser();
    }

    @Override
    public SelectSQLParser selectSQLParser() {
        return null;
    }

    @Override
    public InsertSQLParser insertSQLParser() {
        return null;
    }

    @Override
    public UpdateSQLParser updateSQLParser() {
        return null;
    }
}
