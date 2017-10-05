package ga.skif.sprsec.services;

import ga.skif.sprsec.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService() {}

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

}

