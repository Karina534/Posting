package org.example.posting.hibernate.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;

@Slf4j
public class JwtAuthenticationConverter implements AuthenticationConverter {
    /*
    Класс для конвертации полученного из запроса Authentication токена в access или refresh
     */

    private static final String AUTHENTICATED_SCHEME_BEARER = "Bearer";
    private final Function<String, JwtToken> accessTokenStringDeserializer;
    private final Function<String, JwtToken> refreshTokenStringDeserializer;

    public JwtAuthenticationConverter(Function<String, JwtToken> accessTokenStringDeserializer, Function<String, JwtToken> refreshTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        log.trace("Start convert authentication token to access or refresh");
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null){
            log.trace("Authorization is null");
            return null;
        } else {
            if (!authorization.startsWith(AUTHENTICATED_SCHEME_BEARER)){
                log.debug("Authorization method is not Bearer");
                return null;
            } else if (authorization.equalsIgnoreCase(AUTHENTICATED_SCHEME_BEARER)){
                log.error("Authorization token is empty");
                throw new BadCredentialsException("Empty bearer authentication token");
            } else {
                var token = authorization.substring(7);
                if (token.split("\\.").length == 3) {

                    var accessToken = this.accessTokenStringDeserializer.apply(token);
                    if (accessToken != null) {
                        log.trace("Created access token from request");
                        return new PreAuthenticatedAuthenticationToken(accessToken, token);
                    }
                } else if (token.split("\\.").length == 5){
                    var refreshToken = this.refreshTokenStringDeserializer.apply(token);
                    if (refreshToken != null) {
                        log.trace("Created refresh token from request");
                        return new PreAuthenticatedAuthenticationToken(refreshToken, token);
                    }
                }
            }
        }

        return null;
    }


}
