package org.food.chain.foodchainbackend.security;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.food.chain.foodchainbackend.domain.Producer;
import org.food.chain.foodchainbackend.repository.ProducerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class TokenAuthenticationService implements UserAuthenticationService {

    @NonNull
    TokenService tokens;
    @NonNull
    ProducerRepository producerRepository;
    @NonNull
    LogoutService logoutService;

    @Override
    public Optional<String> login(final String username, final String password) {
        return producerRepository
                .findProducerByUsername(username)
                .filter(user -> passwordEncoder().matches(password, producerRepository.findProducerByUsername(username).get().getPassword()))
                .map(user -> tokens.expiring(ImmutableMap.of("username", username)));
    }

    @Override
    public Optional<Producer> findByToken(final String token) {
        return Optional
                .of(tokens.verify(token))
                .map(map -> map.get("username"))
                .flatMap(producerRepository::findProducerByUsername);
    }

    @Override
    public void logout(final String token) {
        logoutService.getLogoutTokens().put(token, Calendar.getInstance());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}