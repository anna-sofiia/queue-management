package com.example.queuemanagement.model;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ServiceType {
    DEPOSIT("Грошові вклади"),
    CREDIT("Кредити"),
    CARD("Банківські/кредитні картки"),
    MORTGAGE("Іпотека"),
    SAFE("Індивідуальні сейфи"),
    PAYMENT("Платежі"),
    CASH("Прийом/видача готівки/пенсія"),
    TRANSFER("Грошові перекази"),
    CURRENCY_EXCHANGE("Валютно-обмінні операції");

    private final String ukrainianName;

    ServiceType(String ukrainianName) {
        this.ukrainianName = ukrainianName;
    }

    public String getUkrainianName() {
        return ukrainianName;
    }

    public static Map<ServiceType, String> getUkrainianMap() {
        Map<ServiceType, String> map = new LinkedHashMap<>();
        for (ServiceType type : ServiceType.values()) {
            map.put(type, type.getUkrainianName());
        }
        return map;
    }
}