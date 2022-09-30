package com.api.fascinareventos.services;

import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.repositories.EventRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class EventService {

    private EventRepository repository;

    @Transactional
    public EventModel insertEvent(EventModel eventModel) {
        try {
            return repository.save(eventModel);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}
