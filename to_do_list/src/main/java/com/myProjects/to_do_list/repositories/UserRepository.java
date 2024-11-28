package com.myProjects.to_do_list.repositories;

import com.myProjects.to_do_list.models.Entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Integer> {

    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
