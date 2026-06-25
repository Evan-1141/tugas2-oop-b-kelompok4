package handler;

import server.Request;
import server.Response;
import service.UserService;
import model.User;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserHandler {

        private static UserService userService = new UserService();

        public static void getAllUsers(
                        Request req,
                        Response res) {

                res.sendSuccess(
                                userService.getAllUsers());
        }

        public static void getUserById(
                        Request req,
                        Response res) {

                String id = req.getPathParam("id");

                User user = userService.getUserById(id);

                if (user == null) {
                        res.sendError(
                                        404,
                                        "User tidak ditemukan");
                        return;
                }

                res.sendSuccess(user);
        }

        public static void createUser(
                        Request req,
                        Response res) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss");

                String createdAt = LocalDateTime.now().format(formatter);

                try {

                        Map<String, Object> body = req.getJSON();

                        if (body == null) {
                                res.sendError(
                                                400,
                                                "Body harus JSON");
                                return;
                        }

                        String name = (String) body.get("name");
                        String email = (String) body.get("email");
                        String phone = (String) body.get("phone");
                        String role = (String) body.get("role");

                        if (name == null || email == null || role == null) {
                                res.sendError(
                                                400,
                                                "Field name, email, dan role wajib diisi");
                                return;
                        }

                        User user = new User(
                                        userService.generateId(),
                                        name,
                                        email,
                                        phone,
                                        role,
                                        createdAt);

                        if (userService.createUser(user)) {
                                res.sendSuccess(user);
                        } else {
                                res.sendError(
                                                500,
                                                "Gagal menambahkan user");
                        }

                } catch (Exception e) {
                        res.sendError(
                                        400,
                                        e.getMessage());
                }

        }

        public static void updateUser(
                        Request req,
                        Response res) {

                try {

                        String id = req.getPathParam("id");

                        User oldUser = userService.getUserById(id);

                        if (oldUser == null) {
                                res.sendError(
                                                404,
                                                "User tidak ditemukan");
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
                        String email = (String) body.get("email");
                        String phone = (String) body.get("phone");
                        String role = (String) body.get("role");

                        User user = new User(
                                        id,
                                        name != null ? name : oldUser.getName(),
                                        email != null ? email : oldUser.getEmail(),
                                        phone != null ? phone : oldUser.getPhone(),
                                        role != null ? role : oldUser.getRole(),
                                        oldUser.getCreatedAt());

                        if (userService.updateUser(user)) {
                                res.sendSuccess(user);
                        } else {
                                res.sendError(
                                                500,
                                                "Gagal mengupdate user");
                        }

                } catch (Exception e) {
                        res.sendError(
                                        400,
                                        e.getMessage());
                }

        }

}