package com.mapstruct.app;

import com.mapstruct.dto.CarDto;
import com.mapstruct.mapper.CarMapper;
import com.mapstruct.po.CarDo;
import com.mapstruct.po.CarType;

/**
 * 功能：快速入门
 * 作者：SmartSi
 * CSDN博客：https://smartsi.blog.csdn.net/
 * 公众号：大数据生态
 * 日期：2023/5/25 下午10:36
 */
public class QuickStart {
    public static void main(String[] args) {
        // 生成 CarDo
        CarDo car = new CarDo("Morris", 5, CarType.SEDAN);
        System.out.println(car);
        // CarDo 转换为 CarDto
        CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
        System.out.println(carDto);
    }
}
