package com.example.todoapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TodoListInfo {
    @Getter
    @Setter
    private TodoList list;
    @Getter
    @Setter
    private List<TodoItem> items;

    public TodoListInfo(TodoList list, List<TodoItem> items) {
        this.list = list;
        this.items = items;
    }
}
