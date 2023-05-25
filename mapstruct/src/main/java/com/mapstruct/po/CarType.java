package com.mapstruct.po;

public enum CarType {

    SEDAN(1, "SEDAN"),
    TRUCK(2, "truck");

    private int id;
    private String name;

    CarType (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
