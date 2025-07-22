package org.example.posting.hibernate.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.posting.hibernate.dto.LoginDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Setter
public class RequestJwtTokenFilter extends OncePerRequestFilter {
    /*
    Фильтр для аутентификации пользователя по логину и паролю и выдачи токенов по пути /api/auth/login
     */

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());
    private Function<Authentication, JwtToken> refreshTokenFactory = new DefaultRefreshTokenFactory();
    private Function<JwtToken, JwtToken> accessTokenFactory = new DefaultAccessTokenFactory();
    private Function<JwtToken, String> refreshTokenStringSerializer = Objects::toString;
    private Function<JwtToken, String> accessTokenStringSerializer = Objects::toString;
    private ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.trace("Entered in requestJwtTokenFilter");

        if (this.requestMatcher.matches(request)){
            log.debug("Request matched path /api/auth/login");

            log.trace("Start reading json from request");
            LoginDto dto;
            try{
                dto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            } catch (IOException e) {
                log.error("Bad JSON request", e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат JSON");
                return;
            }

            log.trace("Creating UsernamePasswordAuthenticationToken for authentication");
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

            log.trace("Sending Authentication token to authenticationManager");
            Authentication authResult;
            try {
                authResult = authenticationManager.authenticate(authRequest);
            } catch (AuthenticationException e){
                log.error("AuthenticationException ", e.getMessage());
                response.sendRedirect("/api/auth/login");
                return;
            }

            log.info("User authenticated successfully, save him in context");
            SecurityContextHolder.getContext().setAuthentication(authResult);


            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !(auth instanceof PreAuthenticatedAuthenticationToken)){
                log.trace("Start generating tokens");
                var refreshToken = this.refreshTokenFactory.apply(auth);
                var accessToken = this.accessTokenFactory.apply(refreshToken);
                log.debug("Tokens generated");
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                this.objectMapper.writeValue(response.getWriter(),
                        new JwtResponseTokens(this.accessTokenStringSerializer.apply(accessToken),
                                accessToken.expiresAt().toString(),
                                refreshTokenStringSerializer.apply(refreshToken),
                                refreshToken.expiresAt().toString()));

                log.info("Jwt tokens authentication successfully finished");
                return;
            }

            throw new AccessDeniedException("User must be authorized");
        }
        log.trace("Skip RequestJwtTokenFilter, as it's not matches to path /api/auth/login");
        filterChain.doFilter(request, response);
    }
}
