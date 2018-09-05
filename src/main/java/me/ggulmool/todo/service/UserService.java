package me.ggulmool.todo.service;

import me.ggulmool.todo.domain.UnAuthenticationException;
import me.ggulmool.todo.domain.User;
import me.ggulmool.todo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String userId, String password) {
        Optional<User> optUser = userRepository.findByUserId(userId);
        User dbUser = optUser.orElseThrow(() -> new UnAuthenticationException("user not found"));
        if (!dbUser.matchPassword(password)) {
            throw new UnAuthenticationException("user password not matched!");
        }
        return dbUser;
    }
}
