package service;

import repository.EventRepository;
import repository.TicketRepository;
import model.Ticket;
import model.Event;
import model.Refundable;
import exception.EventNotFoundException;
import exception.RefundNotAllowedException;
import java.util.List;

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
                    event.getClass().getSimpleName()
                            + "event tidak menerima refund");
        }

        return ticketRepository.update(ticket);
    }
}