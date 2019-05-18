package com.senyast4745.github.dao;


import com.senyast4745.github.model.ToDo;
import com.senyast4745.github.repository.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Create only one exemplar (like singleton) так что нужно обратиться к этому компоненте  использовать Autowired
@Component
//@Scope("singleton")
public class ToDoDAO {
    //private static final String template = "ToDo #%d";
    private final AtomicLong counter = new AtomicLong();

    private static final Logger LOGGER = LoggerFactory.getLogger(ToDoDAO.class);
    private static final String LOG_TAG = ToDoDAO.class.getSimpleName();

    private final
    ToDoRepository toDoRepository;

    public ToDoDAO(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
        this.toDoRepository.save(new ToDo(counter.getAndIncrement(), "first", "Java", false));
        this.toDoRepository.save(new ToDo(counter.getAndIncrement(), "second", "one", false));
        this.toDoRepository.save(new ToDo(counter.getAndIncrement(), "second", "Love", false));
    }

    public ToDo create(String description, String userName) {
        checkDescription(description);
        long id = counter.incrementAndGet();
        ToDo toDo = new ToDo(id, userName, description, false);
        LOGGER.info("Creating todo " + toDo.getUserName() + " " + toDo.getDescription());
        return toDoRepository.save(toDo);
    }


    public ToDo read(long id, String userName) {
        return toDoRepository.findByIdAndUserName(id, userName).orElse(new ToDo(counter.getAndIncrement(), userName, "", false));

    }

    public boolean delete(long id, String userName) {
        return toDoRepository.deleteByIdAndUserName(id, userName).orElseThrow(() -> new IllegalArgumentException("todo with id " + id + " not found")) != null;

    }

    public ToDo update(long id, String userName, String description, boolean checked) {
        checkDescription(description);
        ToDo tmpTodo =  toDoRepository.findByIdAndUserName(id, userName).orElse(create("", userName));
        tmpTodo.setDescription(description);
        tmpTodo.setChecked(checked);
        toDoRepository.deleteByIdAndUserName(id, userName);
        return toDoRepository.save(tmpTodo);
    }

    public List<ToDo> showAll(String userName) {
        return toDoRepository.findAllByUserName(userName).orElseThrow(() -> new IllegalArgumentException("your todos list is empty"));
    }

    public boolean clearList(String userName) {
        return toDoRepository.deleteAllByUserName(userName).orElseThrow(() -> new IllegalArgumentException("your todos list is already empty")) != null;
    }

    private void checkDescription(String description){
        Pattern incorrectSymbols = Pattern.compile("[-+<>=*@#$%^&]");
        Matcher matcher = incorrectSymbols.matcher(description);
        if(matcher.find() || description.isEmpty() || description.length() > 50){

            throw new IllegalArgumentException("your description doesn't satisfy the specific parameters: " +
                    "Description mustn't be empty, " +
                    "length of description mustn't be more then 50 symbols a" +
                    "nd descriptions mustn't contains symbols: \"+\", \"-\", \"<\"," +
                    " \">\", \"=\", \"*\", \"@\", \"#\", \"$\", \"%\", \"^\", \"&\".");
        }
    }
}
