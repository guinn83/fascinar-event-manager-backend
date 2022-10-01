package com.api.fascinareventos.services;

import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.models.EventStatus;
import com.api.fascinareventos.repositories.EventRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private EventRepository repository;

    @Transactional
    public EventModel insertEvent(EventModel eventModel) {
        try {
            if (eventModel.getStatus() == null) {
                eventModel.setStatus(EventStatus.A_REALIZAR);
            }
            return repository.save(eventModel);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public Object updateEvent(Long id, EventModel eventModel) {
        Optional<EventModel> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, "Event not found");
        }
        var newEvent = new EventModel();
        BeanUtils.copyProperties(eventModel, newEvent);
        newEvent.setId(obj.get().getId());
        return repository.save(newEvent);
    }

    public Page<EventModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<EventModel> findById(Long id) {
        return repository.findById(id);
    }
}
