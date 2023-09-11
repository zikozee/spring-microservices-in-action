package com.zikozee.authserver.repository;

import com.zikozee.authserver.entity.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("""
        SELECT c FROM Client c WHERE c.clientId = :clientId
""")
    Optional<Client> findByClientId(String clientId);
}
