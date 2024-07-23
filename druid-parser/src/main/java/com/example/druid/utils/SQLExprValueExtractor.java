package com.example.druid.utils;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;

/**
 * 功能：SQLExpr Value 提取器
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/23 22:54
 */
public class SQLExprValueExtractor {
    public static Object extract(SQLExpr expr) {
        if (expr instanceof SQLIntegerExpr) {
            return ((SQLIntegerExpr) expr).getNumber().intValue();
        } else if (expr instanceof SQLCharExpr) {
            return ((SQLCharExpr) expr).getText();
        } else if (expr instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr) expr).getName();
        }
        return null;
    }
}
