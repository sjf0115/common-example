package com.example.druid.parser.mysql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;
import com.example.druid.parser.bean.MySQLSelect;
import com.example.druid.parser.core.SelectSQLParser;

import java.util.List;

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
            MySqlSelectQueryBlock queryBlock = (MySqlSelectQueryBlock) query;
            SQLTableSource from = queryBlock.getFrom();

            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            for (SQLSelectItem item : selectList) {

            }

            if(from instanceof SQLExprTableSource) {
                //
                SQLExprTableSource tableSource = (SQLExprTableSource) from;
                String tableName = tableSource.getTableName();
            } else if (from instanceof SQLLateralViewTableSource) {
                //
                SQLLateralViewTableSource lateralViewTableSource = (SQLLateralViewTableSource) from;
                SQLTableSource tableSource = lateralViewTableSource.getTableSource();
            } else if (from instanceof SQLValuesTableSource) {
                //
                SQLValuesTableSource valuesTableSource = (SQLValuesTableSource) from;
                String tableAlias = valuesTableSource.getAlias();
                System.out.println("TableAlias: " + tableAlias);
                List<SQLListExpr> values = valuesTableSource.getValues();
                for (SQLListExpr listExpr : values) {
                    List<SQLExpr> items = listExpr.getItems();
                    for (SQLExpr expr : items) {
                        if (expr instanceof SQLCharExpr) {
                            SQLCharExpr charExpr = (SQLCharExpr) expr;
                            Object value = charExpr.getValue();
                            System.out.println("Value: " + value);
                        }
                    }
                }
                List<SQLName> columns = valuesTableSource.getColumns();
                for (SQLName name : columns) {
                    System.out.println("Name: " + name.getSimpleName());
                }
            }
            System.out.println("对应单表查询");
        } else if (query instanceof SQLUnionQuery) {
            // 对应 UNION 查询
            System.out.println("对应 UNION 查询");
            SQLUnionQuery unionQuery = (SQLUnionQuery) query;
            SQLUnionOperator operator = unionQuery.getOperator();
            String operatorName = operator.name();
            System.out.println("Operator: " + operatorName);

            SQLOrderBy orderBy = unionQuery.getOrderBy();
            List<SQLSelectOrderByItem> items = orderBy.getItems();

            List<SQLSelectQuery> relations = unionQuery.getRelations();
            for (SQLSelectQuery selectQuery : relations) {

            }
        } else if (query instanceof SQLValuesQuery) {
            // 对应 Values 查询
            System.out.println("对应 Values 查询");
        }
        return null;
    }
}
