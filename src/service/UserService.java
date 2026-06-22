package service;

import repository.UserRepository;
import model.User;
import java.util.List;

public class UserService {

    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}