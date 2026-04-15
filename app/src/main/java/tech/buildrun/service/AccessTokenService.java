package tech.buildrun.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import tech.buildrun.controller.dto.AccessTokenResponseDto;
import tech.buildrun.entity.UserEntity;
import tech.buildrun.exception.LoginException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class AccessTokenService {

    private static final long EXPIRES_IN = 60 * 5;

    public AccessTokenResponseDto getAccessToken(String username, String password) {
        var user = UserEntity.find("username = ?1", username)
                .firstResultOptional()
                .map(UserEntity.class::cast)
                .orElseThrow(() -> new LoginException("Invalid credentials", "Invalid username or password"));

        if (!user.password.equals(password)) {
            throw new LoginException("Invalid credentials", "Invalid username or password");
        }

        var accessToken = Jwt.issuer("ticketmaster")
                .upn(username)
                .subject(user.id.toString())
                .groups(new HashSet<>(List.of("User")))
                .claim("email", user.email)
                .expiresIn(EXPIRES_IN)
                .sign();

        return new AccessTokenResponseDto(accessToken, EXPIRES_IN);
    }
}
