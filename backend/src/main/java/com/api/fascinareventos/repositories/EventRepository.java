package com.api.fascinareventos.repositories;

import com.api.fascinareventos.models.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {
}
