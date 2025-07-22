package org.example.posting.hibernate.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class JwtUserDetails extends User {
    /*
    Класс обертка над токеном, чтобы Provider мог с ним работать и аутентифицировать
     */

    private final JwtToken jwtToken;

    public JwtUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, JwtToken token) {
        super(username, password, authorities);
        this.jwtToken = token;
    }

    public JwtUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, JwtToken token) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.jwtToken = token;
    }
}
