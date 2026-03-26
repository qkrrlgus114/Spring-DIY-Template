package com.diy.app.lecture;

public class Lecture {

    private Long id;

    private String name;

    private Integer price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
