package handler;

import server.Request;
import server.Response;
import service.EventService;
import model.Event;
import model.Refundable;
import model.Concert;
import model.Seminar;
import model.SportMatch;
import exception.EventNotFoundException;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class EventHandler {

        private static EventService eventService = new EventService();

        public static void getAllEvents(
                        Request req,
                        Response res) {

                res.sendSuccess(
                                eventService.getAllEvents());
        }

        public static void getEventById(
                        Request req,
                        Response res) {

                try {
                        String id = req.getPathParam("id");

                        Event event = eventService.getEventById(id);

                        Map<String, Object> data = new LinkedHashMap<>();

                        data.put("id", event.getId());
                        data.put("type", event.getEventType());
                        data.put("name", event.getName());

                        data.put("venue",
                                        eventService.getVenueInfo(event.getVenueId()));

                        data.put("organizer",
                                        eventService.getOrganizerInfo(event.getOrganizerId()));

                        data.put("date", event.getDate());
                        data.put("basePrice", event.getBasePrice());
                        Map<String, Object> priceList = new LinkedHashMap<>();

                        if (event instanceof Concert) {

                                priceList.put("vip", event.calculateTicketPrice("VIP"));
                                priceList.put("regular", event.calculateTicketPrice("Regular"));
                                priceList.put("festival", event.calculateTicketPrice("Festival"));

                        } else if (event instanceof Seminar) {

                                priceList.put("regular", event.calculateTicketPrice("Regular"));

                        } else if (event instanceof SportMatch) {

                                priceList.put("tribune", event.calculateTicketPrice("Tribune"));
                                priceList.put("vip", event.calculateTicketPrice("VIP"));
                                priceList.put("vvip", event.calculateTicketPrice("VVIP"));
                        }

                        data.put("priceList", priceList);
                        data.put(
                                        "remainingCapacity",
                                        eventService.getRemainingCapacity(event.getId()));
                        data.put("createdAt", event.getCreatedAt());
                        data.put("updatedAt", event.getUpdatedAt());

                        if (event instanceof Refundable) {

                                Refundable refundable = (Refundable) event;

                                data.put("refundable", refundable.isRefundable());

                                if (event instanceof Concert) {
                                        data.put("refundPolicy",
                                                        "100% if >14 days, 50% if 7-14 days, 0% if <7 days");
                                } else if (event instanceof Seminar) {
                                        data.put("refundPolicy",
                                                        "100% if >1 day, 0% on event day");
                                }
                        }

                        res.sendSuccess(data);

                } catch (EventNotFoundException e) {

                        res.sendError(
                                        404,
                                        e.getMessage());

                }
        }

        public static void createEvent(
                        Request req,
                        Response res) {

                try {

                        Map<String, Object> body = req.getJSON();

                        if (body == null) {
                                res.sendError(400, "Body harus JSON");
                                return;
                        }

                        String type = (String) body.get("type");
                        String name = (String) body.get("name");
                        String venueId = (String) body.get("venueId");
                        String organizerId = (String) body.get("organizerId");
                        String date = (String) body.get("date");

                        Number basePriceObj = (Number) body.get("basePrice");

                        if (type == null || name == null || venueId == null
                                        || organizerId == null || date == null
                                        || basePriceObj == null) {

                                res.sendError(
                                                400,
                                                "Field type, name, venueId, organizerId, date, dan basePrice wajib diisi");
                                return;
                        }

                        double basePrice = basePriceObj.doubleValue();

                        String createdAt = LocalDateTime.now().format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        String updatedAt = null;

                        Event event;

                        switch (type.toLowerCase()) {

                                case "concert":
                                        event = new Concert(
                                                        eventService.generateId(),
                                                        name,
                                                        venueId,
                                                        organizerId,
                                                        date,
                                                        basePrice,
                                                        createdAt,
                                                        updatedAt,
                                                        (String) body.get("artist"));
                                        break;

                                case "seminar":
                                        event = new Seminar(
                                                        eventService.generateId(),
                                                        name,
                                                        venueId,
                                                        organizerId,
                                                        date,
                                                        basePrice,
                                                        createdAt,
                                                        updatedAt,
                                                        (String) body.get("speaker"));
                                        break;

                                case "sport_match":
                                        event = new SportMatch(
                                                        eventService.generateId(),
                                                        name,
                                                        venueId,
                                                        organizerId,
                                                        date,
                                                        basePrice,
                                                        createdAt,
                                                        updatedAt,
                                                        (String) body.get("team"));
                                        break;

                                default:
                                        res.sendError(400, "Tipe event tidak valid");
                                        return;
                        }

                        if (eventService.createEvent(event)) {
                                res.sendCreated(event);
                        } else {
                                res.sendError(500, "Gagal membuat event");
                        }

                } catch (Exception e) {
                        res.sendError(400, e.getMessage());
                }
        }

        public static void updateEvent(
                        Request req,
                        Response res) {

                try {

                        String id = req.getPathParam("id");

                        Event oldEvent = eventService.getEventById(id);

                        Map<String, Object> body = req.getJSON();

                        if (body == null) {
                                res.sendError(400, "Body harus JSON");
                                return;
                        }

                        String name = (String) body.get("name");
                        String venueId = (String) body.get("venueId");
                        String organizerId = (String) body.get("organizerId");
                        String date = (String) body.get("date");

                        Number basePriceObj = (Number) body.get("basePrice");

                        String updatedAt = LocalDateTime.now().format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        Event event;

                        if (oldEvent instanceof Concert) {

                                event = new Concert(
                                                id,
                                                name != null ? name : oldEvent.getName(),
                                                venueId != null ? venueId : oldEvent.getVenueId(),
                                                organizerId != null ? organizerId : oldEvent.getOrganizerId(),
                                                date != null ? date : oldEvent.getDate(),
                                                basePriceObj != null ? basePriceObj.doubleValue()
                                                                : oldEvent.getBasePrice(),
                                                oldEvent.getCreatedAt(),
                                                updatedAt,
                                                (String) body.getOrDefault(
                                                                "artist",
                                                                ((Concert) oldEvent).getArtist()));

                        } else if (oldEvent instanceof Seminar) {

                                event = new Seminar(
                                                id,
                                                name != null ? name : oldEvent.getName(),
                                                venueId != null ? venueId : oldEvent.getVenueId(),
                                                organizerId != null ? organizerId : oldEvent.getOrganizerId(),
                                                date != null ? date : oldEvent.getDate(),
                                                basePriceObj != null ? basePriceObj.doubleValue()
                                                                : oldEvent.getBasePrice(),
                                                oldEvent.getCreatedAt(),
                                                updatedAt,
                                                (String) body.getOrDefault(
                                                                "speaker",
                                                                ((Seminar) oldEvent).getSpeaker()));

                        } else {

                                event = new SportMatch(
                                                id,
                                                name != null ? name : oldEvent.getName(),
                                                venueId != null ? venueId : oldEvent.getVenueId(),
                                                organizerId != null ? organizerId : oldEvent.getOrganizerId(),
                                                date != null ? date : oldEvent.getDate(),
                                                basePriceObj != null ? basePriceObj.doubleValue()
                                                                : oldEvent.getBasePrice(),
                                                oldEvent.getCreatedAt(),
                                                updatedAt,
                                                (String) body.getOrDefault(
                                                                "team",
                                                                ((SportMatch) oldEvent).getTeam()));
                        }

                        if (eventService.updateEvent(event)) {
                                res.sendSuccess(event);
                        } else {
                                res.sendError(500, "Gagal mengupdate event");
                        }

                } catch (Exception e) {
                        res.sendError(400, e.getMessage());
                }
        }

        public static void getPriceSummary(
                        Request req,
                        Response res) {

                res.sendSuccess(
                                eventService.getPriceSummary());
        }

        public static void getSalesReport(
                        Request req,
                        Response res) {

                String eventId = req.getQueryParam("eventId");

                if (eventId == null || eventId.isBlank()) {

                        res.sendError(
                                        400,
                                        "Parameter eventId wajib diisi.");

                        return;
                }

                try {

                        res.sendSuccess(
                                        eventService.getSalesReport(eventId));

                } catch (Exception e) {

                        res.sendError(
                                        400,
                                        e.getMessage());

                }
        }
}