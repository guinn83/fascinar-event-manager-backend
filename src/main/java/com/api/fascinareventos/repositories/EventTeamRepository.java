package com.api.fascinareventos.repositories;

import com.api.fascinareventos.models.EventTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTeamRepository extends JpaRepository<EventTeam, Long> {
}
