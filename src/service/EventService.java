package service;

import repository.EventRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.VenueRepository;
import model.Event;
import model.Venue;
import model.User;
import model.Ticket;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import exception.EventNotFoundException;

public class EventService {

    private EventRepository eventRepository;
    private VenueRepository venueRepository;
    private UserRepository userRepository;
    private TicketRepository ticketRepository;

    public EventService() {
        this.eventRepository = new EventRepository();
        this.venueRepository = new VenueRepository();
        this.userRepository = new UserRepository();
        this.ticketRepository = new TicketRepository();
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

    public Map<String, Object> getVenueInfo(String venueId) {

        Venue venue = venueRepository.findById(venueId);

        if (venue == null) {
            return null;
        }

        Map<String, Object> data = new LinkedHashMap<>();

        data.put("id", venue.getId());
        data.put("name", venue.getName());

        return data;
    }

    public Map<String, Object> getOrganizerInfo(String organizerId) {

        User user = userRepository.findById(organizerId);

        if (user == null) {
            return null;
        }

        Map<String, Object> data = new LinkedHashMap<>();

        data.put("id", user.getId());
        data.put("name", user.getName());

        return data;
    }

    public boolean createEvent(Event event) {
        return eventRepository.save(event);
    }

    public String generateId() {
        return eventRepository.generateId();
    }

    public boolean updateEvent(Event event) {

        if (eventRepository.findById(event.getId()) == null) {
            throw new EventNotFoundException(
                    "Event dengan ID " + event.getId() + " tidak ditemukan.");
        }

        return eventRepository.update(event);
    }

    public List<Map<String, Object>> getPriceSummary() {

        List<Event> events = eventRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Event event : events) {

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("id", event.getId());
            item.put("name", event.getName());
            item.put("type", event.getEventType());

            Map<String, Object> prices = new LinkedHashMap<>();

            switch (event.getEventType()) {

                case "concert":

                    prices.put("vip",
                            event.calculateTicketPrice("VIP"));

                    prices.put("regular",
                            event.calculateTicketPrice("Regular"));

                    prices.put("festival",
                            event.calculateTicketPrice("Festival"));

                    break;

                case "seminar":

                    prices.put("general",
                            event.calculateTicketPrice("General"));

                    break;

                case "sport_match":

                    prices.put("tribune",
                            event.calculateTicketPrice("Tribune"));

                    prices.put("vip",
                            event.calculateTicketPrice("VIP"));

                    prices.put("vvip",
                            event.calculateTicketPrice("VVIP"));

                    break;
            }

            item.put("prices", prices);

            result.add(item);
        }

        return result;
    }

    public Map<String, Object> getSalesReport(String eventId) {

        Event event = eventRepository.findById(eventId);

        if (event == null) {
            throw new EventNotFoundException(
                    "Event dengan ID " + eventId + " tidak ditemukan.");
        }

        List<Ticket> tickets = ticketRepository.findByEventId(eventId);

        int ticketsSold = 0;
        double revenue = 0;
        int refunds = 0;
        double refundAmount = 0;

        for (Ticket ticket : tickets) {

            ticketsSold += ticket.getQuantity();

            revenue += ticket.getTotalPrice();

            if ("refunded".equalsIgnoreCase(ticket.getStatus())) {

                refunds++;

                refundAmount += ticket.getRefundAmount();

            }
        }

        Map<String, Object> data = new LinkedHashMap<>();

        data.put("event", event.getName());
        data.put("ticketsSold", ticketsSold);
        data.put("revenue", revenue);
        data.put("refunds", refunds);
        data.put("refundAmount", refundAmount);

        return data;
    }

}