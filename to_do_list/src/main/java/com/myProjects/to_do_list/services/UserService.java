package com.myProjects.to_do_list.services;

import com.myProjects.to_do_list.models.Dto.ToDoDto;
import com.myProjects.to_do_list.models.Dto.UserSignDto;
import com.myProjects.to_do_list.models.Entities.ToDo;
import com.myProjects.to_do_list.models.Entities.UserEntity;

import java.util.List;


public interface UserService {
    UserEntity createUser(UserSignDto user);
    UserEntity findByUsername(String username);
    ToDo addToDo(ToDoDto toDoDto,int id);
    UserEntity findById(int id);
    List<ToDo> userToDos(int id);


}
