package com.example.todoapp.model;

import lombok.Getter;
import lombok.Setter;

public class TodoList {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    public TodoList() {
    }

    public TodoList(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
