package com.teamoldspice.model;

/**
 * Created by neo on 1/3/16.
 */
public class Todo {
    String name;
    Boolean completed = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
