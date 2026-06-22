package service;

import repository.EventRepository;
import model.Event;
import java.util.List;

public class EventService {

    private EventRepository eventRepository;

    public EventService() {
        this.eventRepository = new EventRepository();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(String id) {
        return eventRepository.findById(id);
    }

    public boolean createEvent(Event event) {
        return eventRepository.save(event);
    }

    public boolean updateEvent(Event event) {
        return eventRepository.update(event);
    }
}