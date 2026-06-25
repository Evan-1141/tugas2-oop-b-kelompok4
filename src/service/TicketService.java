package service;

import repository.EventRepository;
import repository.TicketRepository;
import model.Ticket;
import model.Event;
import model.Refundable;
import exception.EventNotFoundException;
import exception.RefundNotAllowedException;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TicketService {

    private TicketRepository ticketRepository;
    private EventRepository eventRepository;

    public TicketService() {
        this.ticketRepository = new TicketRepository();
        this.eventRepository = new EventRepository();
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id);
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

        ticket.setId(ticketRepository.generateId());

        double unitPrice = event.calculateTicketPrice(ticket.getCategory());
        ticket.setUnitPrice(unitPrice);
        ticket.setTotalPrice(unitPrice * ticket.getQuantity());
        ticket.setPurchaseDate(LocalDate.now().toString());

        return ticketRepository.save(ticket);
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
                            + " tidak menerima refund");
        }

        int daysBeforeEvent = 0;
        try {
            LocalDate eventDate = LocalDate.parse(event.getDate());
            LocalDate today = LocalDate.now();
            daysBeforeEvent = (int) ChronoUnit.DAYS.between(today, eventDate);
        } catch (Exception e) {

        }

        double refundAmount = ((Refundable) event).calculateRefund(daysBeforeEvent);

        ticket.setRefundAmount(refundAmount);
        ticket.setStatus("REFUNDED");

        return ticketRepository.update(ticket);
    }
}