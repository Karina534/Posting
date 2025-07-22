package org.example.posting.hibernate.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
@Setter
public class AccessTokenJwsStringDeserialize implements Function<String, JwtToken> {
    /*
    Класс для превращения строки в accessToken
     */

    private final JWSVerifier jwsVerifier;

    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsStringDeserialize(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public JwtToken apply(String string) {
        try {
            var signedJwt = SignedJWT.parse(string);
            log.info("Access token parsed");
            if (signedJwt.verify(this.jwsVerifier)){

                log.info("Access token verified");
                var claimSet = signedJwt.getJWTClaimsSet();

                if (claimSet.getExpirationTime().toInstant().isBefore(Instant.now())){
                    log.debug("Access token expired");
                    throw new InsufficientAuthenticationException("Access token expired");
                }

                return new JwtToken(UUID.fromString(claimSet.getJWTID()),
                        claimSet.getSubject(),
                        claimSet.getStringListClaim("authorities"),
                        claimSet.getIssueTime().toInstant(),
                        claimSet.getExpirationTime().toInstant());
            }
            log.info("Access token not verified");

        } catch (ParseException | JOSEException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
