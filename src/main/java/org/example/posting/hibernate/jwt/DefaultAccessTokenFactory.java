package org.example.posting.hibernate.jwt;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

@Component
public class DefaultAccessTokenFactory implements Function<JwtToken, JwtToken> {
    /*
    Класс - создатель для accessToken
     */

    @Setter
    private Duration tokenTime = Duration.ofMinutes(5);

    @Override
    public JwtToken apply(JwtToken jwtToken) {
        Instant now = Instant.now();

        return new JwtToken(jwtToken.id(),
                jwtToken.subject(),
                jwtToken.authorities().stream()
                        .filter(auth -> auth.startsWith("GRANT_"))
                        .map(auth -> auth.substring(6))
                        .toList(),
                now,
                now.plus(this.tokenTime));
    }
}
