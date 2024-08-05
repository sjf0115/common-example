package com.example.pattern.behavior.visitor.v2;


/**
 * 功能：提取器 V2版本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 10:13
 */
public class Extractor {
    public void extract2txt(PPTFile pptFile) {
        System.out.println("extract ppt to txt v2");
    }

    public void extract2txt(PdfFile pdfFile) {
        System.out.println("extract pdf to txt v2");
    }

    public void extract2txt(WordFile wordFile) {
        System.out.println("extract word to txt v2");
    }
}
