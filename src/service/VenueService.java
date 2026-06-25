package service;

import repository.VenueRepository;
import model.Venue;
import java.util.List;
import repository.EventRepository;
import model.Event;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class VenueService {

    private VenueRepository venueRepository;
    private EventRepository eventRepository;

    public VenueService() {
        this.venueRepository = new VenueRepository();
        this.eventRepository = new EventRepository();
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(String id) {
        return venueRepository.findById(id);
    }

    public List<Map<String, Object>> getVenueEvents(String venueId) {

        List<Map<String, Object>> events = new ArrayList<>();

        for (Event event : eventRepository.findAll()) {

            if (event.getVenueId().equals(venueId)) {

                Map<String, Object> data = new LinkedHashMap<>();

                data.put("id", event.getId());
                data.put("name", event.getName());
                data.put("date", event.getDate());

                events.add(data);
            }
        }

        return events;
    }

    public boolean createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public String generateId() {
        return venueRepository.generateId();
    }

    public boolean updateVenue(Venue venue) {
        return venueRepository.update(venue);
    }
}