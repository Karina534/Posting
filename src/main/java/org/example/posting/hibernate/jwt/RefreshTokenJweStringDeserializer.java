package org.example.posting.hibernate.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
@Setter
public class RefreshTokenJweStringDeserializer implements Function<String, JwtToken> {
    /*
    Класс для превращения строки в refreshToken
     */

    private final JWEDecrypter jweDecrypter;
    @Override
    public JwtToken apply(String string) {
        try {
            var encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            var claimedSet = encryptedJWT.getJWTClaimsSet();
            log.trace("Refresh token decrypted");

            if (claimedSet.getExpirationTime().toInstant().isBefore(Instant.now())){
                log.debug("Refresh token expired");
                return null;
            }

            return new JwtToken(
                    UUID.fromString(claimedSet.getJWTID()),
                    claimedSet.getSubject(),
                    claimedSet.getStringListClaim("authorities"),
                    claimedSet.getIssueTime().toInstant(),
                    claimedSet.getExpirationTime().toInstant()
            );

        } catch (ParseException | JOSEException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
