package tech.filatov.pigeoner.service;

import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.repository.UserRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User get(long userId) {
        return repository.findById(userId).orElseThrow(NotFoundException.withIdInfo(userId));
    }
}
