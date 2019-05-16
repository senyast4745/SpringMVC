package com.senyast4745.github.controller;

import com.senyast4745.github.dao.ToDoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private ToDoDAO toDo;

    @Autowired
    public MainController(ToDoDAO toDo) {
        this.toDo = toDo;
    }


    @RequestMapping("/")
    public String index(Model model, Authentication auth){
        model.addAttribute("data", toDo.showAll(auth.getName()).toArray());
        return "finalToDo";
    }
}
