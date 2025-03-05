package com.example;

import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * URIBuilder示例
 */
public class URIBuilderExample {

    // 构造 URI 示例
    public static void buildUri() throws URISyntaxException {
        URIBuilder uriBuilder =  new URIBuilder();
        uriBuilder.setScheme("https")
                .setHost("example.com")
                .setPort(8080)
                .setPath("/api/resource")
                .addParameter("param1", "value1")
                .addParameter("param2", "value2")
                .setFragment("section1");
        URI uri = uriBuilder.build();
        System.out.println("Generated URI: " + uri.toString());
    }

    // 修改 URI
    public static void modifyUri() throws URISyntaxException {
        // 原先URI
        URIBuilder uriBuilder =  new URIBuilder("https://example.com:8080/api/resource?param=value");
        URI uri = uriBuilder.build();
        System.out.println("Generated URI: " + uri.toString());

        // 新URI
        URIBuilder newUriBuilder = new URIBuilder(uri)
                .setPath("/api/resource2")
                .setParameter("param", "value2");
        URI newUri = newUriBuilder.build();
        System.out.println("Modified URI: " + newUri.toString());
    }

    // 路径的动态拼接
    public static void pathParameter() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder("https://example.com")
                .setPath("/api/resource/{id}")
                .setParameter("id", "123");
        URI uri = uriBuilder.build();
        System.out.println("URI with path parameter: " + uri.toString());
    }

    // 设置字符集
    public static void charset() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder("https://example.com/api/resource")
                .addParameter("query", "搜索")
                .setCharset(StandardCharsets.UTF_8);
        URI uri = uriBuilder.build();
        System.out.println("URI with custom charset: " + uri.toString());
    }

    public static void main(String[] args) throws URISyntaxException {
        // modifyUri();
        // pathParameter();
        charset();
    }
}
