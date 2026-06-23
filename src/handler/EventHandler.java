package handler;

import server.Request;
import server.Response;
import service.EventService;
import model.Event;

public class EventHandler {

    private static EventService eventService =
            new EventService();

    public static void getAllEvents(
            Request req,
            Response res) {

        res.sendSuccess(
                eventService.getAllEvents()
        );
    }

    public static void getEventById(
            Request req,
            Response res) {

        String id =
                req.getPathParam("id");

        Event event =
                eventService.getEventById(id);

        if(event == null){
            res.sendError(
                    404,
                    "Event tidak ditemukan"
            );
            return;
        }

        res.sendSuccess(event);
    }
}