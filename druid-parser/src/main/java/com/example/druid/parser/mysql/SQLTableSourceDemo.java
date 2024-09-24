package com.example.druid.parser.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能：SQLTableSource 解析示例
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/9/22 20:00
 */
public class SQLTableSourceDemo {

    private static void parseTableSource(String sql) {
        SQLStatement statement = SQLUtils.parseSingleMysqlStatement(sql);
        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) statement;
        SQLSelect select = sqlSelectStatement.getSelect();
        SQLSelectQuery query = select.getQuery();
        MySqlSelectQueryBlock queryBlock = (MySqlSelectQueryBlock) query;
        SQLTableSource from = queryBlock.getFrom();

        if(from instanceof SQLExprTableSource) {
            SQLExprTableSource exprTableSource = (SQLExprTableSource) from;
            String tableName = exprTableSource.getTableName();
            System.out.println("[SQLExprTableSource] tableName: " + tableName);
        } else if (from instanceof SQLJoinTableSource) {
            SQLJoinTableSource joinTableSource = (SQLJoinTableSource) from;
            SQLTableSource leftTableSource = joinTableSource.getLeft();
            if (leftTableSource instanceof SQLExprTableSource) {
                SQLExprTableSource leftExprTableSource = (SQLExprTableSource) leftTableSource;
                String tableName = leftExprTableSource.getTableName();
                System.out.println("[SQLJoinTableSource] leftTableName: " + tableName);
            }
            SQLTableSource rightTableSource = joinTableSource.getRight();
            if (rightTableSource instanceof SQLExprTableSource) {
                SQLExprTableSource rightExprTableSource = (SQLExprTableSource) rightTableSource;
                String tableName = rightExprTableSource.getTableName();
                System.out.println("[SQLJoinTableSource] rightTableName: " + tableName);
            }
            SQLJoinTableSource.JoinType joinType = joinTableSource.getJoinType();
            System.out.println("[SQLJoinTableSource] joinType: " + joinType);
            SQLExpr condition = joinTableSource.getCondition();
            System.out.println("[SQLJoinTableSource] condition: " + condition);
        } else if (from instanceof SQLLateralViewTableSource) {
            SQLLateralViewTableSource lateralViewTableSource = (SQLLateralViewTableSource) from;
            SQLTableSource tableSource = lateralViewTableSource.getTableSource();
            if (tableSource instanceof SQLExprTableSource) {
                SQLExprTableSource exprTableSource = (SQLExprTableSource) tableSource;
                String tableName = exprTableSource.getTableName();
                System.out.println("[SQLLateralViewTableSource] tableName: " + tableName);
            }
            SQLMethodInvokeExpr method = lateralViewTableSource.getMethod();
            System.out.println("[SQLLateralViewTableSource] methodName: " + method.getMethodName());
            List<SQLName> columns = lateralViewTableSource.getColumns();
            System.out.println("[SQLLateralViewTableSource] columns: " + columns.stream().map(c->c.getSimpleName()).collect(Collectors.toList()));
        } else if (from instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource subqueryTableSource = (SQLSubqueryTableSource) from;
            SQLSelect subSelect = subqueryTableSource.getSelect();
            SQLSelectQuery subQuery = subSelect.getQuery();
            MySqlSelectQueryBlock subQueryBlock = (MySqlSelectQueryBlock) subQuery;
            SQLTableSource subFrom = subQueryBlock.getFrom();
            if(subFrom instanceof SQLExprTableSource) {
                SQLExprTableSource subExprTableSource = (SQLExprTableSource) subFrom;
                String tableName = subExprTableSource.getTableName();
                System.out.println("[SQLSubqueryTableSource] tableName: " + tableName);
            }
        } else if (from instanceof SQLUnionQueryTableSource) {
            SQLUnionQueryTableSource unionQueryTableSource = (SQLUnionQueryTableSource) from;
            SQLUnionQuery unionQuery = unionQueryTableSource.getUnion();

        } else if (from instanceof SQLUnnestTableSource) {

        } else if (from instanceof SQLValuesTableSource) {
            SQLValuesTableSource valuesTableSource = (SQLValuesTableSource) from;
            List<SQLName> columns = valuesTableSource.getColumns();
            System.out.println("[SQLValuesTableSource] columns: " + columns);
            List<SQLListExpr> values = valuesTableSource.getValues();
            System.out.println("[SQLValuesTableSource] values: " + values);
        }
    }

    public static void main(String[] args) {
        // 1. SQLExprTableSource
        String exprTableSourceSQL = "select id,name,age from user as a";
        //parseTableSource(exprTableSourceSQL);

        // 2. SQLJoinTableSource
        String joinTableSourceSQL = "SELECT a.id, a.name, a.age, b.department_name\n" +
                "FROM user AS a\n" +
                "LEFT OUTER JOIN department AS b\n" +
                "ON a.id = b.user_id";
        //parseTableSource(joinTableSourceSQL);

        // 3. SQLLateralViewTableSource
        String lateralViewTableSourceSQL = "SELECT sport\n" +
                "FROM user AS a\n" +
                "LATERAL VIEW EXPLODE(like_sports) like_sports AS sport";
        //parseTableSource(lateralViewTableSourceSQL);

        // 4. SQLSubqueryTableSource
        String subqueryTableSourceSQL = "SELECT id, name\n" +
                "FROM (\n" +
                "  SELECT id, name FROM user\n" +
                ") AS a";
        //parseTableSource(subqueryTableSourceSQL);

        // 5. SQLUnionQueryTableSource
        String unionQueryTableSourceSQL = "SELECT 'user' AS type, id, name\n" +
                "FROM user\n" +
                "UNION ALL\n" +
                "SELECT 'department' AS type, id, name\n" +
                "FROM department";
        //parseTableSource(unionQueryTableSourceSQL);

        // 6. SQLValuesTableSource
        String valuesTableSourceSQL = "SELECT id, name\n" +
                "FROM Values ('1', 'Lucy'),('2', 'Lily') t(id, name)";
        parseTableSource(valuesTableSourceSQL);
    }
}
