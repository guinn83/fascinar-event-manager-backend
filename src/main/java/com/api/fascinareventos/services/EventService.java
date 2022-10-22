package com.api.fascinareventos.services;

import com.api.fascinareventos.dtos.BillInfo;
import com.api.fascinareventos.dtos.EventInfo;
import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.models.enums.EventStatus;
import com.api.fascinareventos.repositories.EventRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import com.api.fascinareventos.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
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
    public EventModel updateEvent(Long id, EventModel eventModel) {
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

    public Page<EventInfo> findAllInfo(Pageable pageable) {
        List<EventModel> events = new ArrayList<>(repository.findAll());
        List<EventInfo> eventInfos = new ArrayList<>();
        for (EventModel e : events) {
            EventInfo info = new EventInfo();
            BeanUtils.copyProperties(e, info);
            info.setBillInfo(new BillInfo(e.getBillList()));
            eventInfos.add(info);
        }
        return new PageImpl<>(eventInfos, pageable, eventInfos.size());
    }

    public Optional<EventModel> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteEvent(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id, "Event not found");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
