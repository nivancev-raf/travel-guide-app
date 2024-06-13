package com.raf.rafvodic.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Activity {

    @NotNull
    private int id;
    @NotNull
    private String name;

    public Activity() {
    }

    public Activity(String name) {
        this.name = name;
    }


    public Activity(int id, String name) {
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
