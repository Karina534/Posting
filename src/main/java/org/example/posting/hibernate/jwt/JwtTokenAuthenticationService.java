package org.example.posting.hibernate.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtTokenAuthenticationService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    /*
    Возвращает UserDetails из сырого токена, так как именно с ним должен работать провайдер для аутентификации
     */

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof JwtToken token) {
            return new JwtUserDetails(token.subject(), "nopassword", true, true,
                    !this.jdbcTemplate.queryForObject("""
                            select exists(select id from t_token_denied where id = ?)
                            """, Boolean.class, token.id()) &&
                    token.expiresAt().isAfter(Instant.now()), true,
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new).toList(),
                    token);
        }
        throw new UsernameNotFoundException("Principal must be of type token");
    }
}
