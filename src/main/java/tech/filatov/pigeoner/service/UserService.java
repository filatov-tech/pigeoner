package tech.filatov.pigeoner.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.KeeperDto;
import tech.filatov.pigeoner.dto.auth.RegisterRequest;
import tech.filatov.pigeoner.model.Role;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.repository.UserRepository;
import tech.filatov.pigeoner.util.exception.NotFoundException;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository repository;
    private final KeeperService keeperService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, @Lazy KeeperService keeperService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.keeperService = keeperService;
        this.passwordEncoder = passwordEncoder;
    }

    public User get(long userId) {
        return repository.findById(userId).orElseThrow(NotFoundException.withIdInfo(userId));
    }

    public User getRef(long userId) {
        return repository.getReferenceById(userId);
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        return repository.findByEmailIgnoreCase(email);
    }

    @Transactional
    public User create(RegisterRequest newUserData) {
        User user = User.builder()
                .email(newUserData.getEmail())
                .password(passwordEncoder.encode(newUserData.getPassword()))
                .roles(Set.of(Role.USER))
                .build();
        user = repository.save(user);

        long keeperId = keeperService.createOrUpdate(
                KeeperDto.builder()
                        .name(newUserData.getKeeperName())
                        .build(),
                user.getId()
        ).getId();
        user.setKeeper(keeperService.get(keeperId, user.getId()));

        return user;
    }
}
