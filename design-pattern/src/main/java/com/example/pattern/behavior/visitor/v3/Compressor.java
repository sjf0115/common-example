package com.example.pattern.behavior.visitor.v3;

/**
 * 功能：压缩器 V3版本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 10:13
 */
public class Compressor {
    public void compress(PPTFile pptFile) {
        System.out.println("compress ppt v3");
    }

    public void compress(PdfFile pdfFile) {
        System.out.println("compress pdf v3");
    }

    public void compress(WordFile wordFile) {
        System.out.println("compress word v3");
    }
}
