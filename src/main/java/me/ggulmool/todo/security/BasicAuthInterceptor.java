package me.ggulmool.todo.security;

import lombok.extern.slf4j.Slf4j;
import me.ggulmool.todo.domain.exception.UnAuthenticationException;
import me.ggulmool.todo.domain.User;
import me.ggulmool.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

@Slf4j
public class BasicAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api")) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        log.info("Authorization : {}", authorization);
        if (authorization == null || !authorization.startsWith("Basic")) {
            throw new UnAuthenticationException("인증되지 않은 사용자 입니다.");
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        log.info("username : {}", values[0]);
        log.info("password : {}", values[1]);

        User user = userService.authenticate(values[0], values[1]);
        log.info("Login Success : {}", user);
        request.setAttribute("user", user);
        return true;
    }
}
