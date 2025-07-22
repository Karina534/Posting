package org.example.posting.hibernate.jwt;

public record JwtResponseTokens (String accessToken, String accessTokenExpiry,
                                 String refreshToken, String refreshTokenExpiry) {
}
