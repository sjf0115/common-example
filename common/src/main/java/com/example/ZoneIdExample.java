package com.example;

import java.time.ZoneId;
import java.util.Set;

public class ZoneIdExample {
    public static void main(String[] args) {
        // 1. 默认系统时区
        ZoneId defaultZoneId = ZoneId.systemDefault();
        // Asia/Shanghai
        System.out.println(defaultZoneId);

        // 2. 所有可用时区
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        System.out.println("总共" + availableZoneIds.size() + "时区: ");
        for (String zoneId: availableZoneIds) {
            //System.out.println(zoneId);
        }

        // 3. 指定时区
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        System.out.println(zoneId);
    }
}
