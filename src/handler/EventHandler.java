package handler;

import server.Request;
import server.Response;
import service.EventService;
import model.Event;
import exception.EventNotFoundException;

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
}