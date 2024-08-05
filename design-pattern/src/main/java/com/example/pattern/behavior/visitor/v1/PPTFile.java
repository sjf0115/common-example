package com.example.pattern.behavior.visitor.v1;

/**
 * 功能：PPT 提取文本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 09:39
 */
public class PPTFile extends ResourceFile {
    public PPTFile(String filePath) {
        super(filePath);
    }

    public void extract2txt() {
        // 抽取 filePath 路径 PPT 中的文本并保存为同名的 Txt 文件中
        System.out.println("extract ppt to txt");
    }
}
