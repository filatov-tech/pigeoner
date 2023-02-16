package tech.filatov.pigeoner.auth;

import tech.filatov.pigeoner.model.User;

import java.util.Set;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), Set.of());
    }
}
