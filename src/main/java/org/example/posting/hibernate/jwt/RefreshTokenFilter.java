package org.example.posting.hibernate.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

@Setter
public class RefreshTokenFilter extends OncePerRequestFilter {
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/jwt/refresh", HttpMethod.PATCH.name());
    private Function<JwtToken, JwtToken> accessTokenFactory = new DefaultAccessTokenFactory();
    private Function<JwtToken, String> accessTokenStringSerializer = Objects::toString;
    private ObjectMapper objectMapper = new ObjectMapper();
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Мы в рефреш фильтре");
        if (this.requestMatcher.matches(request)){
            System.out.println("Request matched to /api/jwt/refresh");

            if (this.securityContextRepository.containsContext(request)){
                var context = securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                context.getAuthentication().getPrincipal() instanceof JwtUserDetails userDetails &&
                context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH"))){

                    var accessToken = this.accessTokenFactory.apply(userDetails.getJwtToken());

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new JwtResponseTokens(this.accessTokenStringSerializer.apply(accessToken),
                                    accessToken.expiresAt().toString(),
                                    null, null));
                    return;
                }
            }

            throw new AccessDeniedException("User must be authorized with jwt");
        }

        filterChain.doFilter(request, response);
    }
}
