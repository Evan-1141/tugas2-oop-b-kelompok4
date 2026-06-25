package service;

import repository.UserRepository;
import repository.EventRepository;
import repository.TicketRepository;
import model.User;
import model.Event;
import model.Ticket;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class UserService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;

    public UserService() {
        this.userRepository = new UserRepository();
        this.eventRepository = new EventRepository();
        this.ticketRepository = new TicketRepository();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean createUser(User user) {
        return userRepository.save(user);
    }

    public String generateId() {
        return userRepository.generateId();
    }

    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    public Map<String, Object> getUserSummary(User user) {

        Map<String, Object> summary = new LinkedHashMap<>();

        if (user.getRole().equalsIgnoreCase("buyer")) {

            int totalTicketsPurchased = 0;
            double totalSpending = 0;

            for (Ticket ticket : ticketRepository.findAll()) {

                if (ticket.getUserId().equals(user.getId())) {

                    totalTicketsPurchased += ticket.getQuantity();
                    totalSpending += ticket.getTotalPrice();

                }

            }

            summary.put("totalTicketsPurchased", totalTicketsPurchased);
            summary.put("totalSpending", totalSpending);

        } else if (user.getRole().equalsIgnoreCase("organizer")) {

            int totalEventsCreated = 0;
            double totalRevenue = 0;

            for (Event event : eventRepository.findAll()) {

                if (event.getOrganizerId().equals(user.getId())) {

                    totalEventsCreated++;

                    for (Ticket ticket : ticketRepository.findAll()) {

                        if (ticket.getEventId().equals(event.getId())) {
                            totalRevenue += ticket.getTotalPrice();
                        }

                    }

                }

            }

            summary.put("totalEventsCreated", totalEventsCreated);
            summary.put("totalRevenue", totalRevenue);

        }

        return summary;
    }
}
