package com.myProjects.to_do_list.services;

import com.myProjects.to_do_list.models.Dto.UserSignDto;

public interface AuthenticationService {

    Boolean Authenticate(UserSignDto user);
}
