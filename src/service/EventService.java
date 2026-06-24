package service;

import repository.EventRepository;
import model.Event;
import java.util.List;
import exception.EventNotFoundException;

public class EventService {

    private EventRepository eventRepository;

    public EventService() {
        this.eventRepository = new EventRepository();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(String id) {

        Event event = eventRepository.findById(id);

        if (event == null) {
            throw new EventNotFoundException(
                    "Event dengan ID " + id + " tidak ditemukan.");
        }

        return event;
    }

    public boolean createEvent(Event event) {
        return eventRepository.save(event);
    }

    public boolean updateEvent(Event event) {

        if (eventRepository.findById(event.getId()) == null) {
            throw new EventNotFoundException(
                    "Event dengan ID " + event.getId() + " tidak ditemukan.");
        }

        return eventRepository.update(event);
    }
}