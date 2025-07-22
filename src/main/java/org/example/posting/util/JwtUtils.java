//package org.example.posting.util;
//
//import io.jsonwebtoken.Claims;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import org.example.posting.hibernate.dto.JwtAuthentication;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class JwtUtils {
//    public static JwtAuthentication generate(Claims claims){
//        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
//        jwtInfoToken.setEmail(claims.getSubject());
//        jwtInfoToken.setId(claims.get("id", Long.class));
//        jwtInfoToken.setName(claims.get("name", String.class));
//        return jwtInfoToken;
//    }
//}
