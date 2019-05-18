package com.senyast4745.github.controller;


import com.senyast4745.github.dao.ToDoDAO;
import com.senyast4745.github.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/todo")
@RestController
//@Transactional
@javax.transaction.Transactional
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
    ResponseEntity create(@RequestParam String description, Authentication auth) {
        if (description.trim().length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dao.create(description, auth.getName()));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity read(@PathVariable long id, Authentication auth) {
        ToDo toDo = dao.read(id, auth.getName());
        if (toDo != null)
            return ResponseEntity.ok(toDo);
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity delete(@RequestParam long id, Authentication auth) {
        if (!dao.delete(id, auth.getName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity update(@PathVariable long id, @RequestParam boolean checked, Authentication auth/*, @RequestBody HashMap<String, Object> formData*/) {
        //ToDo toDo = dao.update(id, auth.getName(), (String) formData.get("description") ,(Boolean) formData.get("checked"));
        ToDo toDo = dao.update(id, auth.getName(), checked);
        if (toDo != null)
            return ResponseEntity.ok(toDo);
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity showAll(Authentication auth) {
        if (dao.showAll(auth.getName()).size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dao.showAll(auth.getName()));
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity clearAll(Authentication auth) {
        if (dao.clearList(auth.getName()))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}
