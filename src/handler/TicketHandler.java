package handler;

import server.Request;
import server.Response;
import service.TicketService;
import model.Ticket;
import model.Event;
import model.Refundable;
import model.User;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TicketHandler {

    private static TicketService ticketService = new TicketService();

    public static void getAllTickets(
            Request req,
            Response res) {

        String eventId = req.getQueryParam("eventId");
        String userId = req.getQueryParam("userId");
        String status = req.getQueryParam("status");

        res.sendSuccess(
                ticketService.getAllTickets(
                        eventId,
                        userId,
                        status));
    }

    public static void getTicketById(
            Request req,
            Response res) {

        String id = req.getPathParam("id");

        Ticket ticket = ticketService.getTicketById(id);

        if (ticket == null) {
            res.sendError(
                    404,
                    "Tiket tidak ditemukan");
            return;
        }

        res.sendSuccess(ticket);
    }

    public static void purchaseTicket(Request req, Response res) {
        java.util.Map<String, Object> body;
        try {
            body = req.getJSON();
        } catch (Exception e) {
            res.sendError(400, "Gagal memproses JSON: " + e.getMessage());
            return;
        }

        if (body == null) {
            res.sendError(400, "Request body harus berformat JSON");
            return;
        }

        String eventId = (String) body.get("eventId");
        String userId = (String) body.get("userId");
        String category = (String) body.get("category");
        Number quantityObj = (Number) body.get("quantity");

        if (eventId == null || userId == null || category == null || quantityObj == null) {
            res.sendError(400, "Field eventId, userId, category, dan quantity wajib diisi");
            return;
        }

        int quantity = quantityObj.intValue();

        Ticket ticket = new Ticket(
                null, eventId, userId, category, quantity,
                0, 0, null, "active", 0);

        try {
            boolean success = ticketService.createTicket(ticket);

            if (success) {

                Event event = ticketService.getEvent(ticket.getEventId());

                User buyer = ticketService.getUser(ticket.getUserId());

                Map<String, Object> data = new LinkedHashMap<>();

                data.put("id", ticket.getId());

                data.put("event", event.getName());

                data.put("eventType", event.getEventType());

                Map<String, Object> buyerData = new LinkedHashMap<>();

                buyerData.put("id", buyer.getId());
                buyerData.put("name", buyer.getName());

                data.put("buyer", buyerData);

                data.put("category", ticket.getCategory());

                data.put("quantity", ticket.getQuantity());

                data.put("unitPrice", ticket.getUnitPrice());

                data.put("totalPrice", ticket.getTotalPrice());

                data.put("purchaseDate",
                        ticket.getPurchaseDate());

                data.put("status",
                        ticket.getStatus());

                res.sendCreated(data);

            } else {

                res.sendError(
                        500,
                        "Gagal membuat tiket");

            }

        } catch (Exception e) {
            res.sendError(400, e.getMessage());
        }
    }

    public static void processRefund(Request req, Response res) {

        String id = req.getPathParam("id");

        Ticket ticket = ticketService.getTicketById(id);

        if (ticket == null) {
            res.sendError(404, "Tiket tidak ditemukan");
            return;
        }

        if ("refunded".equals(ticket.getStatus())) {
            res.sendError(400, "Tiket sudah di-refund sebelumnya");
            return;
        }

        try {

            boolean success = ticketService.refundTicket(ticket);

            if (success) {

                Event event = ticketService.getEvent(ticket.getEventId());

                Map<String, Object> data = new LinkedHashMap<>();

                data.put("id", ticket.getId());
                data.put("event", event.getName());
                data.put("totalPaid", ticket.getTotalPrice());

                int daysBeforeEvent = (int) ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        LocalDate.parse(event.getDate()));

                int refundPercentage = (int) (((Refundable) event)
                        .calculateRefund(daysBeforeEvent) * 100);

                data.put("refundPercentage", refundPercentage);
                data.put("refundAmount", ticket.getRefundAmount());
                data.put("status", ticket.getStatus());

                res.sendSuccess(data);

            } else {

                res.sendError(
                        500,
                        "Gagal memproses refund tiket");
            }

        } catch (Exception e) {

            res.sendError(
                    400,
                    e.getMessage());
        }
    }
}