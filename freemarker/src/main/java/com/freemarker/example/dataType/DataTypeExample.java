package com.freemarker.example.dataType;

import com.freemarker.example.HelloWorld;
import com.freemarker.example.bean.Address;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：数据类型演示
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/9 下午10:51
 */
public class DataTypeExample {
    public static void main(String[] args) {
        try {
            // 1. 创建 Configuration 实例
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            String path = HelloWorld.class.getClassLoader().getResource("templates").getPath();
            cfg.setDirectoryForTemplateLoading(new File(path));

            // 2. 创建数据模型
            Map<String, Object> root = new HashMap();
            // 布尔类型
            root.put("boolean_flag", true);
            // 日期类型
            root.put("date", new Date());
            root.put("dateStr", "2023-10-01 12:23:45");
            // 数值类型
            root.put("age", 20);
            root.put("salary", 10003202);
            root.put("avg", 0.1475);
            // 字符串类型
            root.put("str", " aBa ");

            // 3. 加载模板
            Template template = cfg.getTemplate("DataType.ftl");

            // 4. 显示生成的数据,将合并后的数据打印到控制台
            Writer out = new OutputStreamWriter(System.out);
            template.process(root, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
