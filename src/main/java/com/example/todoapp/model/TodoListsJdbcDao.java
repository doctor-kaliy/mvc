package com.example.todoapp.model;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.model.TodoList;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TodoListsJdbcDao extends JdbcDaoSupport {
    public TodoListsJdbcDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void createLists() {
        String sql = "CREATE TABLE LISTS(id integer primary key, name varchar(255) NOT NULL)";
        getJdbcTemplate().execute(sql);
    }

    public void createItems() {
        String sql = "CREATE TABLE ITEMS(id integer primary key, list_id integer, name varchar(255), status integer)";
        getJdbcTemplate().execute(sql);
    }

    public TodoList getList(int id) {
        return getJdbcTemplate().query(
                "SELECT * FROM LISTS WHERE id=" + id,
                new BeanPropertyRowMapper<>(TodoList.class)
        ).get(0);
    }
    public List<TodoList> getLists() {
        return getJdbcTemplate().query(
                "SELECT * FROM LISTS",
                new BeanPropertyRowMapper(TodoList.class)
        );
    }

    public List<TodoItem> getItems(int listId) {
        return getJdbcTemplate().query(
                "SELECT * FROM ITEMS WHERE list_id=" + listId,
                new BeanPropertyRowMapper<>(TodoItem.class)
        );
    }

    public void addList(String name) {
        String sql = "INSERT INTO LISTS (name) VALUES (?)";
        getJdbcTemplate().update(sql, name);
    }

    public void addItem(int list_id, String name) {
        String sql = "INSERT INTO ITEMS (list_id, name, status) VALUES (?, ?, 0)";
        getJdbcTemplate().update(sql, list_id, name);
    }

    public void deleteList(int id) {
        getJdbcTemplate().execute("DELETE FROM LISTS WHERE id=" + id);
        getJdbcTemplate().execute("DELETE FROM ITEMS WHERE list_id=" + id);
    }

    public void deleteItem(int id) {
        getJdbcTemplate().execute("DELETE FROM ITEMS WHERE id=" + id);
    }

    public void updateItem(int id, int status) {
        getJdbcTemplate().update("UPDATE ITEMS SET status=" + status + " WHERE id=" + id);
    }
}
