package org.example.posting.hibernate.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Objects;
import java.util.function.Function;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    /*
    Класс конфигурации цепочки фильтров
     */
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());
    private Function<Authentication, JwtToken> refreshTokenFactory = new DefaultRefreshTokenFactory();
    private Function<JwtToken, JwtToken> accessTokenFactory = new DefaultAccessTokenFactory();
    private Function<JwtToken, String> refreshTokenStringSerializer = Objects::toString;
    private Function<JwtToken, String> accessTokenStringSerializer = Objects::toString;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Function<String, JwtToken> accessTokenStringDeserializer;
    private Function<String, JwtToken> refreshTokenStringDeserializer;
    private DaoAuthenticationProvider daoAuthenticationProvider;
    private JdbcTemplate jdbcTemplate;

    @Override
    public void init(HttpSecurity builder) {
        var csrfConfigure = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigure != null){
            csrfConfigure.ignoringRequestMatchers(new AntPathRequestMatcher("/api/auth/**", "POST"));
        }
    }

    @Override
    public void configure(HttpSecurity builder){
        // Настройка фильтра для login страницы
        var requestJwtTokenFilter = new RequestJwtTokenFilter();
        requestJwtTokenFilter.setRequestMatcher(this.requestMatcher);
        requestJwtTokenFilter.setRefreshTokenFactory(this.refreshTokenFactory);
        requestJwtTokenFilter.setAccessTokenFactory(this.accessTokenFactory);
        requestJwtTokenFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        requestJwtTokenFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        requestJwtTokenFilter.setObjectMapper(this.objectMapper);
        requestJwtTokenFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));


        // Настройка фильтра при обращении к защищенным страницам
        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer));
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new JwtTokenAuthenticationService(this.jdbcTemplate));
        jwtAuthenticationFilter.setSuccessHandler(((request, response, authentication) -> {CsrfFilter.skipRequest(request);}));
        jwtAuthenticationFilter.setFailureHandler(((request, response, exception) -> {response.sendRedirect("/api/auth/login");}));

        // Фильтр для обновления accessToken по refreshToken
        var refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);

        // Фильтр для выхода из профиля
        var jwtLogOutFilter = new JwtLogoutFilter(this.jdbcTemplate);

        // Создаем цепочку
        builder.authenticationProvider(daoAuthenticationProvider)
                .addFilterAfter(requestJwtTokenFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogOutFilter, ExceptionTranslationFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    public JwtAuthenticationConfigurer refreshTokenStringSerializer(Function<JwtToken, String> refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenStringSerializer(Function<JwtToken, String> accessTokenStringSerializer) {
        this.accessTokenStringSerializer = accessTokenStringSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer requestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenFactory(Function<Authentication, JwtToken> refreshTokenFactory) {
        this.refreshTokenFactory = refreshTokenFactory;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenFactory(Function<JwtToken, JwtToken> accessTokenFactory) {
        this.accessTokenFactory = accessTokenFactory;
        return this;
    }

    public JwtAuthenticationConfigurer objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenStringDeserializer(Function<String, JwtToken> accessTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenStringDeserializer(Function<String, JwtToken> refreshTokenStringDeserializer) {
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer daoAuthenticationProvider(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        return this;
    }

    public JwtAuthenticationConfigurer jdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }
}
