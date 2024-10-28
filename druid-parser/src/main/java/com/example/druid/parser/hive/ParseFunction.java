package com.example.druid.parser.hive;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.hive.parser.HiveStatementParser;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;


/**
 * 功能：解析方法名
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/10/28 23:04
 */
public class ParseFunction {

    public class FunctionVisitor extends HiveSchemaStatVisitor {

        private Set<String> methodNames = Sets.newHashSet();

        @Override
        public boolean visit(SQLAggregateExpr aggregateExpr) {
            String methodName = aggregateExpr.getMethodName();
            methodNames.add(methodName);
            return super.visit(aggregateExpr);
        }

        @Override
        public boolean visit(SQLMethodInvokeExpr methodInvokeExpr) {
            String methodName = methodInvokeExpr.getMethodName();
            methodNames.add(methodName);
            List<SQLExpr> arguments = methodInvokeExpr.getArguments();
            for (SQLExpr expr : arguments) {
                System.out.println(expr);
            }
            return super.visit(methodInvokeExpr);
        }
    }

    public void parseFunction(String sql) {
        // 解析
        HiveStatementParser parser = new HiveStatementParser(sql);
        SQLStatement statement = parser.parseStatement();

        // 访问者模式遍历抽象语法树
        FunctionVisitor functionVisitor = new FunctionVisitor();
        statement.accept(functionVisitor);

        // 输出方法名
        for (String methodName : functionVisitor.methodNames) {
            System.out.println("方法名：" + methodName);
        }
    }

    @Test
    public void testAggregateFunction() {
        String sql = "SELECT COUNT(*) FROM user";
        parseFunction(sql);
    }

    @Test
    public void testLateralViewExplodeFunction() {
        String sql = "SELECT sport\n" +
                "FROM user AS a\n" +
                "LATERAL VIEW EXPLODE(like_sports) like_sports AS sport";
        parseFunction(sql);
    }

    @Test
    public void testUDF() {
        String sql = "SELECT user_id, app_version, BI_VERSION_FORMAT(app_version) FROM behavior";
        parseFunction(sql);
    }

    @Test
    public void testJoinFunction() {
        String sql = "SELECT a.user_id, a.app_version, b.play_duration, b.vv\n" +
                "FROM (\n" +
                "    SELECT user_id, BI_VERSION_FORMAT(app_version) AS app_version\n" +
                "    FROM user_attr\n" +
                ") AS a\n" +
                "LEFT OUTER JOIN (\n" +
                "  SELECT user_id, SUM(play_duration) AS play_duration, COUNT(DISTINCT item_id) AS item_num, COUNT(*) AS vv\n" +
                "  FROM behavior\n" +
                "  WHERE event_id = 'play'\n" +
                "  GROUP BY user_id\n" +
                ") AS b\n" +
                "ON a.user_id = b.user_id";
        parseFunction(sql);
    }
}
