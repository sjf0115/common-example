package com.example.pattern.behavior.visitor.v2;

/**
 * 功能：资源文件 V2 版本
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2024/8/5 09:37
 */
public abstract class ResourceFile {
    protected String filePath;
    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }
}
