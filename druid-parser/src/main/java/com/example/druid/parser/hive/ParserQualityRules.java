package com.example.druid.parser.hive;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
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

    public class QualityRuleVisitor extends HiveSchemaStatVisitor {
        // 是否是 SELECT *
        private boolean isSelectAll = false;
        // 是否是覆盖写
        private boolean isInsertOverwrite = false;
        // 是否是简单查询
        private boolean isSimpleSelect = true;

        @Override
        public boolean visit(HiveInsertStatement insertStatement) {
            isInsertOverwrite = insertStatement.isOverwrite();
            return super.visit(insertStatement);
        }

        @Override
        public boolean visit(SQLSelectStatement selectStatement) {
            if (Objects.equals(selectStatement, null)) {
                isSimpleSelect = true;
                return false;
            }
            SQLSelect select = selectStatement.getSelect();
            SQLSelectQuery query = select.getQuery();
            if (query instanceof SQLUnionQuery || query instanceof SQLSelectQueryBlock) {
                return super.visit(selectStatement);
            }
            return false;
        }

        @Override
        public boolean visit(SQLSelectQueryBlock queryBlock) {
            // 是否有去重
            if (queryBlock.isDistinct()) {
                isSimpleSelect = false;
                return false;
            }
            // 是否有分组
            if (!Objects.equals(queryBlock.getGroupBy(), null)) {
                isSimpleSelect = false;
                return false;
            }
            // 是否有排序
            if (!Objects.equals(queryBlock.getOrderBy(), null)) {
                isSimpleSelect = false;
                return false;
            }
            if (queryBlock.getClusterBy().size() > 0) {
                isSimpleSelect = false;
                return false;
            }
            if (queryBlock.getDistributeBy().size() > 0) {
                isSimpleSelect = false;
                return false;
            }
            if (queryBlock.getSortBy().size() > 0) {
                isSimpleSelect = false;
                return false;
            }
            // Where 是否为空
            if (Objects.equals(queryBlock.getWhere(), null)) {
                isSimpleSelect = false;
                return false;
            }
            // 是否有开窗
            if (!Objects.equals(queryBlock.getWindows(), null)) {
                isSimpleSelect = false;
                return false;
            }
            // 2. 获取 FROM
            SQLTableSource from = queryBlock.getFrom();
            // 是否有JOIN
            if (from instanceof SQLJoinTableSource) {
                isSimpleSelect = false;
                return false;
            }
            // 是否有子查询
            if (from instanceof SQLSubqueryTableSource) {
                isSimpleSelect = false;
                return false;
            }
            // 3. 获取 SELECT
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            for (SQLSelectItem item : selectList) {
                SQLExpr expr = item.getExpr();
                // 是否有聚合函数
                if (expr instanceof SQLAggregateExpr) {
                    isSimpleSelect = false;
                    return false;
                }
                // 是否有 SELECT *
                if (expr instanceof SQLAllColumnExpr) {
                    isSelectAll = true;
                    return false;
                }
            }
            return super.visit(queryBlock);
        }

        public boolean isSelectAll() {
            return isSelectAll;
        }

        public boolean isInsertOverwrite() {
            return isInsertOverwrite;
        }
    }

    public void parseQualityRule(String sql) {
        // 解析
        HiveStatementParser parser = new HiveStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        // 访问者模式遍历抽象语法树
        QualityRuleVisitor visitor = new QualityRuleVisitor();
        statement.accept(visitor);

        if (visitor.isSelectAll()) {
            System.out.println("触发【规则】禁止使用 SELECT *");
        }
        if (visitor.isInsertOverwrite()) {
            System.out.println("触发【规则】禁止使用 INSERT INTO SELECT");
        }
        if (visitor.isSimpleSelect) {
            System.out.println("触发【规则】禁止使用简单查询");
        }
    }


    @Test
    public void testInsertSelect() {
        String sql = "INSERT INTO result_table SELECT * FROM source_table";
        parseQualityRule(sql);
    }

    @Test
    public void testInsertValues() {
        String sql = "INSERT INTO result_table SELECT * FROM source_table";
        parseQualityRule(sql);
    }

    @Test
    public void testSelectAll() {
        String sql = "SELECT * FROM user";
        parseQualityRule(sql);
    }

    @Test
    public void testCreateTable() {
        String sql = "SELECT * FROM user";
        parseQualityRule(sql);
    }

    @Test
    public void testAggregateFunction() {
        String sql = "SELECT COUNT(*) FROM user";
        parseQualityRule(sql);
    }

    @Test
    public void testDistinct() {
        String sql = "SELECT user_name FROM user WHERE user_id = '1'";
        parseQualityRule(sql);
    }
}