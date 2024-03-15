package com.usermanagement.core.domain.service;

import com.usermanagement.core.application.config.exceptions.BaseException;
import com.usermanagement.core.domain.dto.SignUpResponse;
import com.usermanagement.core.domain.dto.UserRequest;
import com.usermanagement.core.domain.mapper.UserMapper;
import com.usermanagement.core.infrastructure.repository.UserRepository;
import com.usermanagement.core.infrastructure.repository.entities.Phone;
import com.usermanagement.core.infrastructure.repository.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Value("${jwt.secret-key}")
    private String base64SecretKey;

    @Value("${jwt.exp-time}")
    private Long jwtExpirationTime;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Creates a new user based on the provided UserRequest. It checks if a user with the same email
     * already exists, encodes the user's password, generates a JWT token for the user, associates
     * any provided phones with the user, and saves the user to the repository. Finally, it returns
     * a SignUpResponse created from the saved user.
     *
     * @param userRequest the user request containing user details.
     * @return a SignUpResponse with details of the created user.
     * @throws BaseException if a user with the given email already exists.
     */
    public SignUpResponse create(UserRequest userRequest) {
        userRepository.findByEmail(userRequest.email())
                .ifPresent(u -> {
                    throw new BaseException("El usuario con este correo electrónico ya existe");
                });

        var user = userMapper.with(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(generateJwtToken(user));

        var phones = user.getPhones();
        if (Objects.nonNull(phones) && !phones.isEmpty()) {
            for (Phone phone : phones) {
                phone.setUser(user);
            }
        }

        var userSaved = userRepository.save(user);
        return userMapper.with(userSaved);
    }

    /**
     * Updates the JWT token for the currently authenticated user based on their email.
     * Retrieves the current user's details, generates a new JWT token, saves the user with the updated
     * token, and returns a SignUpResponse with the updated user details.
     *
     * @return a SignUpResponse containing the updated user details including the new token.
     */
    public SignUpResponse syncToken() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = (User) loadUserByUsername(email);
        user.setToken(generateJwtToken(user));
        var userSaved = userRepository.save(user);
        return userMapper.with(userSaved);
    }

    /**
     * Generates a JWT token for a given user. The token includes the user's email as the subject,
     * sets the issued date to the current time, and sets the expiration based on the configured
     * expiration time. The token is signed with a key derived from the base64SecretKey.
     *
     * @param user the user for whom the token is to be generated.
     * @return a signed JWT token as a String.
     */
    private String generateJwtToken(User user) {
        byte[] decodedKey = Decoders.BASE64.decode(base64SecretKey);
        var key = Keys.hmacShaKeyFor(decodedKey);

        var nowMillis = System.currentTimeMillis();
        var now = new Date(nowMillis);
        var expMillis = nowMillis + jwtExpirationTime;
        var exp = new Date(expMillis);

        return Jwts.builder()
                .subject(user.getEmail()).issuedAt(now).expiration(exp).signWith(key)
                .compact();
    }

    /**
     * Loads a user's details given their email. This method is intended for use by Spring Security
     * to authenticate a user. If a user with the specified email is found, it returns the user's
     * details; otherwise, it throws a UsernameNotFoundException.
     *
     * @param email the email of the user to load.
     * @return the UserDetails of the user identified by the email.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No se encontró usuario con el correo: " + email));
    }

}
