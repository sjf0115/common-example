package com.example.druid.parser.hive;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.dialect.hive.parser.HiveStatementParser;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * 功能：SQL 研发质量监测
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/10/26 09:28
 */
public class ParserQualityRules {

    // 质量规则1：禁止使用 SELECT *
    public class SelectAllQualityRuleVisitor extends HiveSchemaStatVisitor {
        // 是否是 SELECT *
        private boolean isSelectAll = false;
        @Override
        public boolean visit(SQLAllColumnExpr expr) {
            // 是否有 SELECT *
            isSelectAll = true;
            return false;
        }
    }

    // 质量规则2：禁止使用 INSERT INTO SELECT
    public class InsertIntoSelectQualityRuleVisitor extends HiveSchemaStatVisitor {
        // 是否是 INSERT INTO SELECT
        private boolean isInsertIntoSelect = false;

        @Override
        public boolean visit(HiveInsertStatement insertStatement) {
            boolean overwrite = insertStatement.isOverwrite();
            SQLSelect query = insertStatement.getQuery();
            if (!overwrite && !Objects.equals(query, null)) {
                isInsertIntoSelect = true;
            }
            return false;
        }
    }

    // 质量规则3：禁止使用简单查询
    public class SimpleQueryQualityRuleVisitor extends HiveSchemaStatVisitor {
        // 是否是简单查询
        private boolean isSimpleQuery = false;

        @Override
        public boolean visit(SQLSelectQueryBlock queryBlock) {
            // 是否有去重
            if (queryBlock.isDistinct()) {
                isSimpleQuery = false;
                return false;
            }
            // 是否有分组
            if (!Objects.equals(queryBlock.getGroupBy(), null)) {
                isSimpleQuery = false;
                return false;
            }
            // 是否有排序
            if (!Objects.equals(queryBlock.getOrderBy(), null)) {
                isSimpleQuery = false;
                return false;
            }
            if (queryBlock.getClusterBy().size() > 0) {
                isSimpleQuery = false;
                return false;
            }
            if (queryBlock.getDistributeBy().size() > 0) {
                isSimpleQuery = false;
                return false;
            }
            if (queryBlock.getSortBy().size() > 0) {
                isSimpleQuery = false;
                return false;
            }
            // Where 是否为空
            if (Objects.equals(queryBlock.getWhere(), null)) {
                isSimpleQuery = false;
                return false;
            }
            // 是否有开窗
            if (!Objects.equals(queryBlock.getWindows(), null)) {
                isSimpleQuery = false;
                return false;
            }
            // 2. 获取 FROM
            SQLTableSource from = queryBlock.getFrom();
            // 是否有JOIN
            if (from instanceof SQLJoinTableSource) {
                isSimpleQuery = false;
                return false;
            }
            // 是否有子查询
            if (from instanceof SQLSubqueryTableSource) {
                isSimpleQuery = false;
                return false;
            }
            // 3. 获取 SELECT
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            for (SQLSelectItem item : selectList) {
                SQLExpr expr = item.getExpr();
                // 是否有聚合函数
                if (expr instanceof SQLAggregateExpr) {
                    isSimpleQuery = false;
                    return false;
                }
            }
            isSimpleQuery = true;
            return false;
        }
    }

    public void parseQualityRule(String sql) {
        // 解析
        HiveStatementParser parser = new HiveStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        // 访问者模式遍历抽象语法树

        // 规则1：禁止使用 SELECT *
        SelectAllQualityRuleVisitor selectAllVisitor = new SelectAllQualityRuleVisitor();
        statement.accept(selectAllVisitor);
        if (selectAllVisitor.isSelectAll) {
            System.out.println("触发【规则】禁止使用 SELECT *");
        } else {
            System.out.println("未触发【规则】禁止使用 SELECT *");
        }

        // 规则2：禁止使用 SELECT *
        InsertIntoSelectQualityRuleVisitor insertIntoVisitor = new InsertIntoSelectQualityRuleVisitor();
        statement.accept(insertIntoVisitor);
        if (insertIntoVisitor.isInsertIntoSelect) {
            System.out.println("触发【规则】禁止使用 INSERT INTO SELECT");
        } else {
            System.out.println("未触发【规则】禁止使用 INSERT INTO SELECT");
        }

        // 规则3：禁止使用简单查询
        SimpleQueryQualityRuleVisitor simpleQueryVisitor = new SimpleQueryQualityRuleVisitor();
        statement.accept(simpleQueryVisitor);
        if (simpleQueryVisitor.isSimpleQuery) {
            System.out.println("触发【规则】禁止使用简单查询");
        } else {
            System.out.println("未触发【规则】禁止使用简单查询");
        }
    }

    // 规则1测试
    @Test
    public void testSelectAll() {
        // 触发【规则】禁止使用 SELECT *
        // 触发【规则】禁止使用简单查询
        String sql = "SELECT * FROM user WHERE user_id = '1' AND desc LIKE '%select *%'";
        parseQualityRule(sql);
    }

    @Test
    public void testSelectName() {
        // 未触发【规则】禁止使用 SELECT *
        // 触发【规则】禁止使用简单查询
        String sql = "SELECT user_name FROM user WHERE user_id = '1' AND desc LIKE '%select *%'";
        parseQualityRule(sql);
    }

    // 规则2测试
    @Test
    public void testInsertSelect() {
        // 触发【规则】禁止使用 SELECT *
        // 触发【规则】禁止使用 INSERT INTO SELECT
        // 未触发【规则】禁止使用简单查询
        String sql = "INSERT INTO result_table SELECT * FROM source_table";
        parseQualityRule(sql);
    }

    @Test
    public void testInsertValues() {
        // 未触发【规则】禁止使用 SELECT *
        // 未触发【规则】禁止使用 INSERT INTO SELECT
        // 未触发【规则】禁止使用简单查询
        String sql = "INSERT INTO Websites (name, country) VALUES ('百度', 'CN')";
        parseQualityRule(sql);
    }

    @Test
    public void testInsertOverwrite() {
        // 未触发【规则】禁止使用 SELECT *
        // 未触发【规则】禁止使用 INSERT INTO SELECT
        // 未触发【规则】禁止使用简单查询
        String sql = "INSERT OVERWRITE Websites (name, country) VALUES ('百度', 'CN')";
        parseQualityRule(sql);
    }

    // 规则3测试
    @Test
    public void testAggregateFunction() {
        String sql = "SELECT COUNT(*) FROM user";
        parseQualityRule(sql);
    }

    @Test
    public void testWhere() {
        String sql = "SELECT * FROM user WHERE user_id = '1'";
        parseQualityRule(sql);
    }
}