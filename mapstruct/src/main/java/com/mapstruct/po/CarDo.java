package com.mapstruct.po;

/**
 * 功能：CarDo
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/5/25 下午11:03
 */
public class CarDo {
    private String make;
    private int numberOfSeats;
    private CarType type;

    public CarDo(String make, int numberOfSeats, CarType type) {
        this.make = make;
        this.numberOfSeats = numberOfSeats;
        this.type = type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CarDo{" +
                "make='" + make + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                ", type=" + type +
                '}';
    }
}
