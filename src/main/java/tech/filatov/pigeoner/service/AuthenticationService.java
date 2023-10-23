package tech.filatov.pigeoner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import tech.filatov.pigeoner.config.JwtService;
import tech.filatov.pigeoner.dto.auth.AuthenticationResponse;
import tech.filatov.pigeoner.dto.auth.RegisterRequest;
import tech.filatov.pigeoner.model.User;
import tech.filatov.pigeoner.util.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = service.create(request);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(RegisterRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = service.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(NotFoundException.userWithEmail(request.getEmail()));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
