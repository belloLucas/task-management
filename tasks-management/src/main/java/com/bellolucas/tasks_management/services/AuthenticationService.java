package com.bellolucas.tasks_management.services;

import com.bellolucas.tasks_management.config.JwtService;
import com.bellolucas.tasks_management.dto.auth.AuthRequestDTO;
import com.bellolucas.tasks_management.dto.auth.AuthResponseDTO;
import com.bellolucas.tasks_management.entities.user.User;
import com.bellolucas.tasks_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.expiration}")
    private Long jwtExpirationInMillis;

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
            String jwtToken = jwtService.generateToken(userDetails);

            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));
            LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(jwtExpirationInMillis / 1000);

            return new AuthResponseDTO(
                    jwtToken,
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    expirationTime
            );
        } catch (
                BadCredentialsException e) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }
    }
}