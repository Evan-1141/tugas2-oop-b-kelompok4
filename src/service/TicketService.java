package service;

import repository.TicketRepository;
import model.Ticket;
import java.util.List;

public class TicketService {

    private TicketRepository ticketRepository;

    public TicketService() {
        this.ticketRepository = new TicketRepository();
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
        return ticketRepository.update(ticket);
    }
}