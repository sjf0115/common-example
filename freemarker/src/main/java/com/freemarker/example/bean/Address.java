package com.freemarker.example.bean;

/**
 * 功能：Address 对象
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/6/8 上午7:52
 */
public class Address {
    private String prov;
    private String city;

    public Address() {
    }

    public Address(String prov, String city) {
        this.prov = prov;
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "prov='" + prov + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
