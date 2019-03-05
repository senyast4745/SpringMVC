package ru.Technopolis.model;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component /*Кладем в контейнер */
public class ToDoDAO {
    private static AtomicLong counter = new AtomicLong();

    public ToDo create(String description){
        long id = counter.incrementAndGet();
        return new ToDo(id,description);
    }
}
