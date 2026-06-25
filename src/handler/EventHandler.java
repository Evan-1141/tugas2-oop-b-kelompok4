package handler;

import server.Request;
import server.Response;
import service.EventService;
import model.Event;
import exception.EventNotFoundException;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Concert;
import model.Seminar;
import model.SportMatch;

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

                        res.sendSuccess(event);

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
}