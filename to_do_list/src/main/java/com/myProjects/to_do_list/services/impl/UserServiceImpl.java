package com.myProjects.to_do_list.services.impl;

import com.myProjects.to_do_list.models.Dto.ToDoDto;
import com.myProjects.to_do_list.models.Dto.UserSignDto;
import com.myProjects.to_do_list.models.Entities.Role;
import com.myProjects.to_do_list.models.Entities.ToDo;
import com.myProjects.to_do_list.models.Entities.UserEntity;
import com.myProjects.to_do_list.repositories.RoleRepository;
import com.myProjects.to_do_list.repositories.ToDoRepository;
import com.myProjects.to_do_list.repositories.UserRepository;
import com.myProjects.to_do_list.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ToDoRepository toDoRepository;


    @Override
    public UserEntity createUser(UserSignDto user) {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("User Already Exist");
        }
        Role role = roleRepository.findByName("USER").orElseThrow(()->new NoSuchElementException("no such Role Exist"));
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setRoles(Collections.singletonList(role));
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("Username name do not exist"));
    }

    @Override
    public ToDo addToDo(ToDoDto toDoDto,int id) {
        UserEntity user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not Found"));
        ToDo toDo = new ToDo();
        toDo.setUser(user);
        toDo.setContent(toDoDto.getContent());
        toDoRepository.save(toDo);
        return toDo;
    }

    @Override
    public UserEntity findById(int id) {
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("user Not Found"));
    }

    @Override
    public List<ToDo> userToDos(int id) {
        return toDoRepository.userToDos(id);
    }

    public List<ToDo> getUsersToDos(int id){
        return toDoRepository.userToDos(id);
    }
}
