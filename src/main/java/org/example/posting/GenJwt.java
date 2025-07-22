package org.example.posting;

//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class GenJwt {
    public static void main(String[] args) {
//        SecretKey accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        SecretKey refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

//        String accessBase64 = Base64.getEncoder().encodeToString(accessKey.getEncoded());
//        String refreshBase64 = Base64.getEncoder().encodeToString(refreshKey.getEncoded());

        String creds = Base64.getEncoder()
                .encodeToString("mail@mail.ru:123456".getBytes(StandardCharsets.UTF_8));

        System.out.println(creds);
    }
}
