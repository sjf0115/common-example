package com.example.druid.parser.mysql;

import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;
import com.example.druid.parser.bean.MySQLSelect;
import com.example.druid.parser.core.SelectSQLParser;

/**
 * 功能：MySQLSelectParser
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/7/24 23:08
 */
public class MySQLSelectParser implements SelectSQLParser<MySQLSelect> {
    //private static Logger LOG = LoggerFactory.getLogger(MySQLSelectParser.class);
    @Override
    public MySQLSelect parse(String sql) {
        MySqlSelectParser parser = new MySqlSelectParser(sql);
        SQLSelect select = parser.select();
        SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            // 对应单表查询
            System.out.println("对应单表查询");
        } else if (query instanceof SQLUnionQuery) {
            // 对应 UNION 查询
            System.out.println("对应 UNION 查询");
        } else if (query instanceof SQLValuesQuery) {
            // 对应 Values 查询
            System.out.println("对应 Values 查询");
        }
        return null;
    }
}
