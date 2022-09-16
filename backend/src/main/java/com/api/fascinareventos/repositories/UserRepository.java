package com.api.fascinareventos.repositories;

import com.api.fascinareventos.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    UserDetails findByUsername(String username);
}
