package com.senyast4745.github.repository;

import com.senyast4745.github.model.ToDo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    Optional<ToDo> findByIdAndUserName(long id, String userName);
    Optional<ToDo> deleteByIdAndUserName(long id, String userName);

    Optional<List <ToDo>> findAllByUserName(String userName);
    Optional<List<ToDo>> deleteAllByUserName(String userName);
    
}
