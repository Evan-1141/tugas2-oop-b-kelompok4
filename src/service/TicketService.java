package service;

import repository.EventRepository;
import repository.TicketRepository;
import repository.UserRepository;
import model.Ticket;
import model.Event;
import model.User;
import model.Refundable;
import exception.EventNotFoundException;
import exception.RefundNotAllowedException;
import exception.TicketSoldOutException;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TicketService {

    private TicketRepository ticketRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    public TicketService() {
        this.ticketRepository = new TicketRepository();
        this.eventRepository = new EventRepository();
        this.userRepository = new UserRepository();
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getAllTickets(
            String eventId,
            String userId,
            String status) {

        if (eventId != null && !eventId.isBlank()) {
            return ticketRepository.findByEventId(eventId);
        }

        if (userId != null && !userId.isBlank()) {
            return ticketRepository.findByUserId(userId);
        }

        if (status != null && !status.isBlank()) {
            return ticketRepository.findByStatus(status);
        }

        return ticketRepository.findAll();
    }

    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id);
    }

    public Event getEvent(String eventId) {
        return eventRepository.findById(eventId);
    }

    public User getUser(String userId) {
        return userRepository.findById(userId);
    }

    public boolean createTicket(Ticket ticket) {

        Event event = eventRepository.findById(
                ticket.getEventId());

        if (event == null) {
            throw new EventNotFoundException(
                    "Event dengan ID "
                            + ticket.getEventId()
                            + " tidak ditemukan.");
        }

        User user = userRepository.findById(
                ticket.getUserId());

        if (user == null) {
            throw new IllegalArgumentException(
                    "User dengan ID "
                            + ticket.getUserId()
                            + " tidak ditemukan.");
        }

        if (ticket.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Jumlah tiket harus lebih dari 0.");
        }

        int remaining = eventRepository.getRemainingCapacity(
                ticket.getEventId(), ticket.getCategory());

        if (remaining < ticket.getQuantity()) {
            throw new TicketSoldOutException(
                    "Kapasitas " + ticket.getCategory()
                            + " tidak mencukupi. Sisa: " + remaining
                            + ", diminta: " + ticket.getQuantity());
        }

        ticket.setId(ticketRepository.generateId());

        double unitPrice = event.calculateTicketPrice(ticket.getCategory());
        ticket.setUnitPrice(unitPrice);
        ticket.setTotalPrice(unitPrice * ticket.getQuantity());
        ticket.setPurchaseDate(LocalDate.now().toString());

        boolean saved = ticketRepository.save(ticket);

        if (saved) {
            eventRepository.incrementFilled(
                    ticket.getEventId(),
                    ticket.getCategory(),
                    ticket.getQuantity());
        }

        return saved;
    }

    public boolean refundTicket(Ticket ticket) {

        Event event = eventRepository.findById(
                ticket.getEventId());

        if (event == null) {
            throw new EventNotFoundException(
                    "Event dengan ID "
                            + ticket.getEventId()
                            + " tidak ditemukan.");
        }

        if (!(event instanceof Refundable)) {
            throw new RefundNotAllowedException(
                    "Event " + event.getName()
                            + " tidak menerima refund.");
        }

        int daysBeforeEvent = 0;
        try {
            LocalDate eventDate = LocalDate.parse(event.getDate());
            LocalDate today = LocalDate.now();
            daysBeforeEvent = (int) ChronoUnit.DAYS.between(today, eventDate);
        } catch (Exception e) {
            throw new RefundNotAllowedException(
                    "Format tanggal event tidak valid.");
        }

        if (daysBeforeEvent <= 0) {
            throw new RefundNotAllowedException(
                    "Refund tidak dapat dilakukan karena event sudah berlangsung atau hari ini.");
        }

        double refundPercentage = ((Refundable) event).calculateRefund(daysBeforeEvent);
        double refundAmount = ticket.getTotalPrice() * refundPercentage;

        ticket.setRefundAmount(refundAmount);
        ticket.setStatus("refunded");

        boolean updated = ticketRepository.update(ticket);

        if (updated) {
            eventRepository.decrementFilled(
                    ticket.getEventId(),
                    ticket.getCategory(),
                    ticket.getQuantity());
        }

        return updated;
    }
}