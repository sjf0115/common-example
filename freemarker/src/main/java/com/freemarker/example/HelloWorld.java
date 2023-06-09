package com.freemarker.example;

import com.freemarker.example.bean.Address;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：模板引擎 FreeMarker 入门程序
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/7 下午11:55
 */
public class HelloWorld {
    public static void main(String[] args) {
        try {
            // 1. 创建 Configuration 实例
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            String path = HelloWorld.class.getClassLoader().getResource("templates").getPath();
            cfg.setDirectoryForTemplateLoading(new File(path));

            // 2. 创建数据模型
            Map<String, Object> root = new HashMap();
            root.put("user_name", "Lucy");
            /*Map<String, String> address = new HashMap();
            address.put("prov", "山东");
            address.put("city", "淄博");
            root.put("address", address);*/

            root.put("address", new Address("山东", "淄博"));

            // 3. 加载模板
            Template template = cfg.getTemplate("HelloWorld.ftl");

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
