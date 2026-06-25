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

    public boolean createUser(User user) {
        return userRepository.save(user);
    }

    public String generateId() {
        return userRepository.generateId();
    }

    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id);
    }

}
