package org.example.posting.hibernate.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class RefreshTokenJweStringSerialize implements Function<JwtToken, String> {
    /*
    Класс для превращения refreshToken в строку
     */

    private final JWEEncrypter jweEncrypter;

    @Setter
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    @Setter
    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    public RefreshTokenJweStringSerialize(JWEEncrypter jweEncrypter) {
        this.jweEncrypter = jweEncrypter;
    }

    @Override
    public String apply(JwtToken jwtToken) {
        var header = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .keyID(jwtToken.id().toString())
                .build();
        var claims = new JWTClaimsSet.Builder()
                .jwtID(jwtToken.id().toString())
                .subject(jwtToken.subject())
                .issueTime(Date.from(jwtToken.createdAt()))
                .expirationTime(Date.from(jwtToken.expiresAt()))
                .claim("authorities", jwtToken.authorities())
                .build();

        var encryptedJwt = new EncryptedJWT(header, claims);

        try {
            encryptedJwt.encrypt(this.jweEncrypter);
            log.trace("Refresh token made");

            return encryptedJwt.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
