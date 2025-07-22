package org.example.posting.hibernate.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Date;

@Setter
public class JwtLogoutFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/logout", HttpMethod.POST.name());
    private final JdbcTemplate jdbcTemplate;
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    public JwtLogoutFilter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Мы в лог аут фильтре");
        if (this.requestMatcher.matches(request)){
            System.out.println("Запрос мэтчится");

            if (this.securityContextRepository.containsContext(request)){
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken
                && context.getAuthentication().getPrincipal() instanceof JwtUserDetails userDetails &&
                context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_LOGOUT"))){

                    System.out.println("Контекст прошел все проверки");
                    this.jdbcTemplate.update("""
                    insert into t_token_denied (id, keep_time)
                    values (?, ?)
                    """, userDetails.getJwtToken().id(), Date.from(userDetails.getJwtToken().expiresAt()));

                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
            }

            throw new AccessDeniedException("User must be authorized with jwt");
        }

        filterChain.doFilter(request, response);
    }
}
