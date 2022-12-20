package com.example.todoapp.controller;

import com.example.todoapp.model.TodoListsJdbcDao;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.model.TodoItemStatus;
import com.example.todoapp.model.TodoList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.todoapp.model.TodoListInfo;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TodoListsController {
    private final TodoListsJdbcDao todoListsJdbcDao;

    public TodoListsController(TodoListsJdbcDao todoListsJdbcDao) {
        this.todoListsJdbcDao = todoListsJdbcDao;
    }

    @RequestMapping(value = "/get-lists", method = RequestMethod.GET)
    public String getLists(ModelMap map) {
        prepareModelMap(map);
        return "todoIndex";
    }

    @RequestMapping(value = "/get-list/{listId}", method = RequestMethod.GET)
    public String getList(@PathVariable("listId") int listId, ModelMap map) {
        prepareModelMapForList(map, listId);
        return "todoList";
    }

    @RequestMapping(value = "/add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute("itemToAdd") TodoItem item) {
        todoListsJdbcDao.addItem(item.getListId(), item.getName());
        return "redirect:/get-list/" + item.getListId();
    }

    @RequestMapping(value = "/change-item", method = RequestMethod.POST)
    public String changeItem(@ModelAttribute("itemToChange") TodoItem item) {
        var status =
                item.getStatus() == TodoItemStatus.TODO ? 0 : item.getStatus() == TodoItemStatus.IN_PROGRESS ? 1 : 2;
        todoListsJdbcDao.updateItem(item.getId(), status);
        return "redirect:/get-list/" + item.getListId();
    }

    @RequestMapping(value = "/remove-item", method = RequestMethod.POST)
    public String removeItem(@ModelAttribute("itemToRemove") TodoItem item) {
        todoListsJdbcDao.deleteItem(item.getId());
        return "redirect:/get-list/" + item.getListId();
    }

    @RequestMapping(value = "/add-list", method = RequestMethod.POST)
    public String addList(@ModelAttribute("listToAdd") TodoList list) {
        todoListsJdbcDao.addList(list.getName());
        return getRedirection("get-lists");
    }

    @RequestMapping(value = "/remove-list", method = RequestMethod.POST)
    public String removeList(@ModelAttribute("listToRemove") TodoList list) {
        todoListsJdbcDao.deleteList(list.getId());
        return getRedirection("get-lists");
    }

    private String getRedirection(String... parts) {
        return "redirect:/" + String.join("/", parts);
    }

    private void prepareModelMapForList(ModelMap map, int listId) {
        map.addAttribute("itemToAdd", new TodoItem().withListId(listId));
        map.addAttribute("itemToRemove", new TodoItem().withListId(listId));
        map.addAttribute("itemToChange", new TodoItem().withListId(listId));

        var list = new TodoListInfo(todoListsJdbcDao.getList(listId), todoListsJdbcDao.getItems(listId));
        map.addAttribute("list", list);
    }

    private void prepareModelMap(ModelMap map) {
        map.addAttribute("listToAdd", new TodoList());
        map.addAttribute("listToRemove", new TodoList());

        List<TodoList> todoLists = todoListsJdbcDao.getLists();

        var lists =
                todoLists
                        .stream()
                        .map(list -> new TodoListInfo(list, todoListsJdbcDao.getItems(list.getId())))
                        .collect(Collectors.toList());

        map.addAttribute("lists", lists);
    }
}