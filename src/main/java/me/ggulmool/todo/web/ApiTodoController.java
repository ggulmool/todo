package me.ggulmool.todo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTodoController {

    @RequestMapping("/todos")
    public String todos() {
        return "todos";
    }
}
