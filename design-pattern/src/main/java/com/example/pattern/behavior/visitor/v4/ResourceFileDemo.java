package com.example.pattern.behavior.visitor.v4;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：资源文件提取示例 V4 版本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 09:44
 */
public class ResourceFileDemo {
    public static void main(String[] args) {
        // 创建提取器
        Extractor extractor = new Extractor();
        // 创建压缩器
        Compressor compressor = new Compressor();

        // 创建不同的资源文件
        List<ResourceFile> resourceFiles = new ArrayList<>();
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new PPTFile("c.ppt"));

        // 提取&压缩文件
        for (ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(extractor);
            resourceFile.accept(compressor);
        }
    }
}
