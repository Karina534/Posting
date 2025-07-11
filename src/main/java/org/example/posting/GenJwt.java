package org.example.posting;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

public class GenJwt {
    public static void main(String[] args) {
        SecretKey accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SecretKey refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String accessBase64 = Base64.getEncoder().encodeToString(accessKey.getEncoded());
        String refreshBase64 = Base64.getEncoder().encodeToString(refreshKey.getEncoded());

        System.out.println(accessBase64);
        System.out.println(refreshBase64);
    }
}
