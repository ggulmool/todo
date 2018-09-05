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
        User selectedUser = optUser.orElseThrow(() -> new UnAuthenticationException("존재하지 않는 사용자 입니다."));
        if (!selectedUser.matchPassword(password)) {
            throw new UnAuthenticationException("패스워드가 틀립니다.");
        }
        return selectedUser;
    }
}
