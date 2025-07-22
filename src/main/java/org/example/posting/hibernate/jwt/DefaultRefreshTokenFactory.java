package org.example.posting.hibernate.jwt;

import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Function;

@Component
public class DefaultRefreshTokenFactory implements Function<Authentication, JwtToken> {
    /*
    Класс - создатель для refreshToken
    */
    @Setter
    private Duration tokenTime = Duration.ofDays(1);

    @Override
    public JwtToken apply(Authentication authentication) {
        var authorities = new LinkedList<String>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRANT_" + authority)
                .forEach(authorities::add);

        Instant now = Instant.now();
        return new JwtToken(UUID.randomUUID(), authentication.getName(),
                authorities,
                now,
                now.plus(this.tokenTime));
    }
}
