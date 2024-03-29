package com.atwaha.springsecurity.repository;

import com.atwaha.springsecurity.model.Token;
import com.atwaha.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUserAndRevokedFalse(User user);

    boolean existsByTokenAndRevokedTrue(String token);

    Token findByToken(String token);
}