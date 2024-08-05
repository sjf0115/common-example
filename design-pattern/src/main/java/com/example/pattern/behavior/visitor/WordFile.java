package com.example.pattern.behavior.visitor;

/**
 * 功能：Word 提取文本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 09:43
 */
public class WordFile extends ResourceFile {
    public WordFile(String filePath) {
        super(filePath);
    }

    public void extract2txt() {
        // 抽取 filePath 路径 Word 中的文本并保存为同名的 Txt 文件中
        System.out.println("extract word to txt");
    }
}
