package handler;

import server.Request;
import server.Response;
import service.VenueService;
import model.Venue;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VenueHandler {

        private static VenueService venueService = new VenueService();

        public static void getAllVenues(
                        Request req,
                        Response res) {

                res.sendSuccess(
                                venueService.getAllVenues());
        }

        public static void getVenueById(
                        Request req,
                        Response res) {

                String id = req.getPathParam("id");

                Venue venue = venueService.getVenueById(id);

                if (venue == null) {
                        res.sendError(
                                        404,
                                        "Venue tidak ditemukan");
                        return;
                }

                res.sendSuccess(venue);
        }

        public static void createVenue(
                        Request req,
                        Response res) {

                try {

                        Map<String, Object> body = req.getJSON();

                        if (body == null) {
                                res.sendError(
                                                400,
                                                "Body harus JSON");
                                return;
                        }

                        String name = (String) body.get("name");

                        String address = (String) body.get("address");

                        Integer maxCapacity = (Integer) body.get("maxCapacity");

                        if (name == null || address == null || maxCapacity == null) {
                                res.sendError(
                                                400,
                                                "Field name, address, dan maxCapacity wajib diisi");
                                return;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss");

                        String createdAt = LocalDateTime.now().format(formatter);

                        Venue venue = new Venue(
                                        venueService.generateId(),
                                        name,
                                        address,
                                        maxCapacity,
                                        createdAt);

                        if (venueService.createVenue(venue)) {
                                res.sendSuccess(venue);
                        } else {
                                res.sendError(
                                                500,
                                                "Gagal menambahkan venue");
                        }

                } catch (Exception e) {
                        res.sendError(
                                        400,
                                        e.getMessage());
                }
        }

        public static void updateVenue(
                        Request req,
                        Response res) {

                try {

                        String id = req.getPathParam("id");

                        Venue oldVenue = venueService.getVenueById(id);

                        if (oldVenue == null) {
                                res.sendError(
                                                404,
                                                "Venue tidak ditemukan");
                                return;
                        }

                        Map<String, Object> body = req.getJSON();

                        if (body == null) {
                                res.sendError(
                                                400,
                                                "Body harus JSON");
                                return;
                        }

                        String name = (String) body.get("name");

                        String address = (String) body.get("address");

                        Integer maxCapacity = (Integer) body.get("maxCapacity");

                        Venue venue = new Venue(
                                        id,
                                        name != null ? name : oldVenue.getName(),
                                        address != null ? address : oldVenue.getAddress(),
                                        maxCapacity != null ? maxCapacity : oldVenue.getMaxCapacity(),
                                        oldVenue.getCreatedAt());

                        if (venueService.updateVenue(venue)) {
                                res.sendSuccess(venue);
                        } else {
                                res.sendError(
                                                500,
                                                "Gagal mengupdate venue");
                        }

                } catch (Exception e) {
                        res.sendError(
                                        400,
                                        e.getMessage());
                }
        }
}