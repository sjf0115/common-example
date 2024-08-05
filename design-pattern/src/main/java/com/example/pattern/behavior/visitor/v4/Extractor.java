package com.example.pattern.behavior.visitor.v4;

/**
 * 功能：提取器 V4版本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 10:13
 */
public class Extractor implements Visitor {

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("extract pdf to txt v4");
    }

    @Override
    public void visit(PPTFile pdfFile) {
        System.out.println("extract ppt to txt v4");
    }

    @Override
    public void visit(WordFile pdfFile) {
        System.out.println("extract word to txt v4");
    }
}
