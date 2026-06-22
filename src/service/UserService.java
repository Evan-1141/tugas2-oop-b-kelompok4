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

    public boolean updateUser(User user) {
    return userRepository.update(user);
    }

    public User getUserById(String id) {
    return userRepository.findById(id);
    }

}

