package service;

import repository.VenueRepository;
import model.Venue;
import java.util.List;

public class VenueService {

    private VenueRepository venueRepository;

    public VenueService() {
        this.venueRepository = new VenueRepository();
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(String id) {
        return venueRepository.findById(id);
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