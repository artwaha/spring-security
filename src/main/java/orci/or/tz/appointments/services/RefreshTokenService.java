package orci.or.tz.appointments.services;



import orci.or.tz.appointments.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);

    Optional<RefreshToken> findByToken(String token);
}
