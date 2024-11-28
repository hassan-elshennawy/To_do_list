package com.myProjects.to_do_list.repositories;

import com.myProjects.to_do_list.models.Entities.ToDo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToDoRepository extends CrudRepository<ToDo,Integer> {
    @Query(value = "SELECT * FROM To_do where user_id = :user_id " +
            "ORDER BY created_at DESC",
            nativeQuery = true)
    List<ToDo> userToDos(@Param("user_id") int id);

    @Query(value = "SELECT * FROM to_do WHERE status = :status and user_id = :id",nativeQuery = true)
    List<ToDo> getCertainTodos(@Param("status") boolean status, @Param("id") int id);
}
