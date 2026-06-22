package handler;

import server.Request;
import server.Response;
import service.UserService;
import model.User;

public class UserHandler {

    private static UserService userService =
            new UserService();

    public static void getAllUsers(
            Request req,
            Response res) {

        res.sendSuccess(
                userService.getAllUsers()
        );
    }

    public static void getUserById(
            Request req,
            Response res) {

        String id =
                req.getPathParam("id");

        User user =
                userService.getUserById(id);

        if(user == null){
            res.sendError(
                    404,
                    "User tidak ditemukan"
            );
            return;
        }

        res.sendSuccess(user);
    }
}