package orci.or.tz.appointments.services;



import orci.or.tz.appointments.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    public RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    public int deleteByUserId(Long userId);

    public Optional<RefreshToken> findByToken(String token);
}
