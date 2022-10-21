package com.example.demo.model;

import lombok.Data;

@Data
public class Test {

    private Integer id;
    private String name;
    private Level level;

    public Test(int id, String name, Level level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }
}
