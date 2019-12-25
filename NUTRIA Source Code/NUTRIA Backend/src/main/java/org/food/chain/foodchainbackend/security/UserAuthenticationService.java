package org.food.chain.foodchainbackend.security;

import org.food.chain.foodchainbackend.domain.Producer;

import java.util.Optional;

public interface UserAuthenticationService {

    Optional<String> login(final String username, final String password);
    Optional<Producer> findByToken(final String token);
    void logout(final String token);

}
