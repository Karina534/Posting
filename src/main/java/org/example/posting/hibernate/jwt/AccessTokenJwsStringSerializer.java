package org.example.posting.hibernate.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class AccessTokenJwsStringSerializer implements Function<JwtToken, String> {
    /*
    Класс для превращения accessToken в строку
     */

    private final JWSSigner jwsSigner;

    @Setter
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    @Override
    public String apply(JwtToken jwtToken) {
        var jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(jwtToken.id().toString()).build();
        var claimSet = new JWTClaimsSet.Builder()
                .jwtID(jwtToken.id().toString())
                .subject(jwtToken.subject())
                .issueTime(Date.from(jwtToken.createdAt()))
                .expirationTime(Date.from(jwtToken.expiresAt()))
                .claim("authorities", jwtToken.authorities())
                .build();
        var signedJwt = new SignedJWT(jwsHeader, claimSet);

        try {
            signedJwt.sign(this.jwsSigner);
            log.trace("Access token made");

            return signedJwt.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
