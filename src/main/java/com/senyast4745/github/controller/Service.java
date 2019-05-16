package com.senyast4745.github.controller;


import com.senyast4745.github.dao.ToDoDAO;
import com.senyast4745.github.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/todo")
@RestController
public class Service {

    //@Autowired may (dependency injection)
    private ToDoDAO dao;

    //Read about Autowired

    @Autowired
    public Service(ToDoDAO dao) {
        this.dao = dao;
    }

    //TODO
    //create (POST)
    //read(id) (GET)
    //delete(id) (POST)
    //update(id) (POST)

    // application сканирует весь путь
    // и по callback вызывется нужный метод

    //http://localhost:8080/create?description=12 -
    // запрос  + (?key=value&key2=value ... )
    // запрос по GET (нельзя изменять структуру сервера, небезопасно)
   /* @RequestMapping ("/create")
    public @ResponseBody
    ToDo create(@RequestParam String description){
        return dao.create(description);
    }*/

    //http по POST
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ToDo> create(@RequestParam String description, @RequestBody String userName) {
        if (description.trim().length() == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(dao.create(description, userName));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ToDo> read(@PathVariable long id, @RequestBody String userName) {
        ToDo toDo = dao.read(id, userName);
        if (toDo != null)
            return ResponseEntity.ok(toDo);
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<Void> delete(@RequestParam long id, @RequestBody String userName) {
        if (!dao.delete(id, userName)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity<ToDo> update(@PathVariable long id, @RequestBody String userName, @RequestParam String description, @RequestParam boolean checked) {
        ToDo toDo = dao.update(id, userName,description, checked);
        if (toDo != null)
            return ResponseEntity.ok(toDo);
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<ToDo>> showAll(@RequestBody String userName) {
        if (dao.showAll(userName).size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dao.showAll(userName));
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Void> clearAll(@RequestBody String userName) {
        if (dao.clearList(userName))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}
