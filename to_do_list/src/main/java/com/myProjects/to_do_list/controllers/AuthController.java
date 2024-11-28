package com.myProjects.to_do_list.controllers;

import com.myProjects.to_do_list.models.Dto.ToDoDto;
import com.myProjects.to_do_list.models.Dto.UserSignDto;
import com.myProjects.to_do_list.models.Entities.ToDo;
import com.myProjects.to_do_list.models.Entities.UserEntity;
import com.myProjects.to_do_list.repositories.ToDoRepository;
import com.myProjects.to_do_list.security.JwtGenerator;
import com.myProjects.to_do_list.services.AuthenticationService;
import com.myProjects.to_do_list.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private JwtGenerator jwtGenerator;
    private AuthenticationManager authenticationManager;
    private final ToDoRepository toDoRepository;

    @Autowired
    public AuthController(UserService userService, AuthenticationService authenticationService, JwtGenerator jwtGenerator, AuthenticationManager authenticationManager, ToDoRepository toDoRepository){
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
        this.toDoRepository = toDoRepository;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserEntity> signup (@RequestBody UserSignDto user){
       return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED) ;
    }

    @PostMapping("/auth/login")
    ResponseEntity<String> login(@RequestBody UserSignDto user){
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(), SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            return new ResponseEntity<>("LoginIn Successfully  " + jwtGenerator.generateToken(authentication) ,HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{id}/addToDo")
    ResponseEntity<ToDo> addToDo(@RequestBody ToDoDto toDoDto, @PathVariable int id){
        return new ResponseEntity<>(userService.addToDo(toDoDto,id),HttpStatus.CREATED);
    }
    @GetMapping("/getToDos")
    ResponseEntity<List<ToDo>> getToDos(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userService.findByUsername(username);
        return new ResponseEntity<>(userService.userToDos(userEntity.getId()),HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    ResponseEntity<String> deleteTask(@PathVariable int id){
        ToDo toDo = toDoRepository.findById(id).orElseThrow();
        toDoRepository.delete(toDo);
        return new ResponseEntity<>("task deleted",HttpStatus.OK);
    }

    @PostMapping("/{id}/completed")
    ResponseEntity<String> completeTask(@PathVariable int id){
        ToDo toDo = toDoRepository.findById(id).orElseThrow();
        toDo.setStatus(true);
        return new ResponseEntity<>("task " + toDo.getContent() + "completed",HttpStatus.OK);
    }

    @GetMapping("/{id}/getToDos/completed")
    ResponseEntity<List<ToDo>> getCompletedToDos(@PathVariable int id){
        UserEntity user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(toDoRepository.getCertainTodos(true,user.getId()),HttpStatus.OK);
    }

    @GetMapping("/getToDos/pending")
    ResponseEntity<List<ToDo>> getPendingToDos(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userService.findByUsername(username);
        return new ResponseEntity<>(toDoRepository.getCertainTodos(false,user.getId()),HttpStatus.OK);
    }

}
