package com.zikozee.authserver.repository;

import com.zikozee.authserver.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("""
        SELECT u FROM User u WHERE u.username = :username
           """)
    Optional<User> findByUsername(String username);
}
