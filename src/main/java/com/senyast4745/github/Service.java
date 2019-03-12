package com.senyast4745.github;


import com.senyast4745.github.dao.ToDoDOA;
import com.senyast4745.github.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ConcurrentLinkedDeque;

@Controller
public class Service {

    //@Autowired may (dependency injection)
    private ToDoDOA dao;

    //Read about Autowired

    @Autowired
    public Service(ToDoDOA dao) {
        this.dao = dao;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    //TODO
    //create (POST)
    //read(id) (GET)
    //delete(id) (POST)
    //update(id) (POST)

    // application сканирует весь путь
    // и по callback вызывется нужный метод

    @RequestMapping("/hw2")
    public String indexSecond() {
        return "index2";
    }
    //http://localhost:8080/create?description=12 -
    // запрос  + (?key=value&key2=value ... )
    // запрос по GET (нельзя изменять структуру сервера, небезопасно)
   /* @RequestMapping ("/create")
    public @ResponseBody
    oDo create(@RequestParam String description){
        return dao.create(description);
    }*/

    //http по POST
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    ToDo create(@RequestParam String description) {
        return dao.create(description);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public @ResponseBody
    ToDo read(@RequestParam long id) {
        ToDo toDo = dao.read(id);
        if (toDo != null)
            return toDo;
        throw new IllegalArgumentException("toDo with id=" + " not exists");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    String delete(@RequestParam long id) {
        if (!dao.delete(id)) {
            throw new IllegalArgumentException("can't delete toDo with id=" + id);
        }
        return "200 OK";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    ToDo update(@RequestParam long id, @RequestParam String description) {
        ToDo toDo = dao.update(id, description);
        if (toDo != null)
            return toDo;
        throw new IllegalArgumentException("toDo with id=" + id + " can't update");

    }

    @RequestMapping(value = "/showAll", method = RequestMethod.GET)
    public @ResponseBody
    ConcurrentLinkedDeque<ToDo> showAll() {
        return dao.showAll();
    }

    @RequestMapping(value = "/clearAll", method = RequestMethod.POST)
    public @ResponseBody
    String clearAll() {
        if (dao.clearList())
            return "200 OK";
        throw new IllegalArgumentException("toDosList can't clear");
    }

}
