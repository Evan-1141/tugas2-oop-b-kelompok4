package handler;

import server.Request;
import server.Response;
import service.TicketService;
import model.Ticket;

public class TicketHandler {

    private static TicketService ticketService =
            new TicketService();

    public static void getAllTickets(
            Request req,
            Response res) {

        res.sendSuccess(
                ticketService.getAllTickets()
        );
    }

    public static void getTicketById(
            Request req,
            Response res) {

        String id =
                req.getPathParam("id");

        Ticket ticket =
                ticketService.getTicketById(id);

        if(ticket == null){
            res.sendError(
                    404,
                    "Tiket tidak ditemukan"
            );
            return;
        }

        res.sendSuccess(ticket);
    }
}