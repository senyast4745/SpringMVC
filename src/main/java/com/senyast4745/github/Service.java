package com.senyast4745.github;


import com.senyast4745.github.dao.ToDoDOA;
import com.senyast4745.github.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    //http://localhost:8080/create?description=12 -
    // запрос  + (?key=value&key2=value ... )
    // запрос по GET (нельзя изменять структуру сервера, небезопасно)
   /* @RequestMapping ("/create")
    public @ResponseBody
    ToDo create(@RequestParam String description){
        return dao.create(description);
    }*/

    //http по POST
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ToDo> create(@RequestParam String description) {
        if (description.trim().length() == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(dao.create(description));
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ToDo> read(@RequestParam long id) {
        ToDo toDo = dao.read(id);
        if (toDo != null)
            return ResponseEntity.ok(toDo);
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Void> delete(@RequestParam long id) {
        if (!dao.delete(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ToDo> update(@RequestParam long id, @RequestParam String description) {
        ToDo toDo = dao.update(id, description);
        if (toDo != null)
            return ResponseEntity.ok(toDo);
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/showAll", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<ToDo>> showAll() {
        if(dao.showAll().size() == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dao.showAll());
    }

    @RequestMapping(value = "/clearAll", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Void> clearAll() {
        if (dao.clearList())
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}
