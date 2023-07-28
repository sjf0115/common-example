package com.freemarker.example.directive;

import com.freemarker.example.HelloWorld;
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
 * 功能：
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/7/28 08:48
 */
public class AssignExample {
    public static void main(String[] args) {
        try {
            // 1. 创建 Configuration 实例
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            String path = HelloWorld.class.getClassLoader().getResource("templates").getPath();
            cfg.setDirectoryForTemplateLoading(new File(path));

            // 2. 创建数据模型
            Map<String, Object> root = new HashMap();


            // 3. 加载模板
            Template template = cfg.getTemplate("assign.ftl");

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
