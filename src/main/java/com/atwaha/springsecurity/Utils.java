package com.atwaha.springsecurity;

import com.atwaha.springsecurity.model.Token;
import com.atwaha.springsecurity.model.User;
import com.atwaha.springsecurity.model.dto.AuthenticatedUserDetails;
import com.atwaha.springsecurity.model.dto.ErrorResponseDTO;
import com.atwaha.springsecurity.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Utils {
    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;

    public Map<String, Object> getExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        AuthenticatedUserDetails authenticatedUserDetails = modelMapper.map(user, AuthenticatedUserDetails.class);
        extraClaims.put("user", authenticatedUserDetails);
        return extraClaims;
    }

    public void saveUserToken(String token, User user) {
        Token newToken = Token.builder().token(token).revoked(false).user(user).build();
        tokenRepository.save(newToken);
    }

    public void revokeAllUserTokens(User user) {
        List<Token> allTokensByUser = tokenRepository.findByUserAndRevokedFalse(user);
        allTokensByUser.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(allTokensByUser);
    }

    public ErrorResponseDTO generateErrorResponse(HttpStatus status, String path, String message, Map<String, String> details) {
        return ErrorResponseDTO
                .builder()
                .path(path)
                .status(status.value())
                .message(message)
                .details(details)
                .build();
    }
}
