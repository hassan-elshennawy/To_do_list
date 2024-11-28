package com.myProjects.to_do_list.repositories;

import com.myProjects.to_do_list.models.Entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {
    @Query(value = "Select * FROM role WHERE name = :name",nativeQuery = true)
    Optional<Role> findByName(String name);
}
