package handler;

import server.Request;
import server.Response;
import service.VenueService;
import model.Venue;

public class VenueHandler {

    private static VenueService venueService =
            new VenueService();

    public static void getAllVenues(
            Request req,
            Response res) {

        res.sendSuccess(
                venueService.getAllVenues()
        );
    }

    public static void getVenueById(
            Request req,
            Response res) {

        String id =
                req.getPathParam("id");

        Venue venue =
                venueService.getVenueById(id);

        if(venue == null){
            res.sendError(
                    404,
                    "Venue tidak ditemukan"
            );
            return;
        }

        res.sendSuccess(venue);
    }
}