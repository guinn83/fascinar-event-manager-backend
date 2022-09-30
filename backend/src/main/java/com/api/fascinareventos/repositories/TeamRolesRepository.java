package com.api.fascinareventos.repositories;

import com.api.fascinareventos.models.TeamRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRolesRepository extends JpaRepository<TeamRoles, Long> {
}
