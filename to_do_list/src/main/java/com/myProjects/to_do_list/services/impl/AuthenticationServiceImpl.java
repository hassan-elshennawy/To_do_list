package com.myProjects.to_do_list.services.impl;


import com.myProjects.to_do_list.models.Dto.UserSignDto;
import com.myProjects.to_do_list.models.Entities.UserEntity;
import com.myProjects.to_do_list.repositories.UserRepository;
import com.myProjects.to_do_list.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Override
    public Boolean Authenticate(UserSignDto user) {
        UserEntity existngUser = userRepository.findByUsername(user.getUsername()).orElseThrow(()->new UsernameNotFoundException("username not found"));
        return passwordEncoder.matches(user.getPassword(), existngUser.getPassword());
    }
}
