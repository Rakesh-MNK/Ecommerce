package com.ecommerce.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            io.jsonwebtoken.io.Decoders.BASE64.decode(
                    "VGhpc0lzQVN0cm9uZ0RldmVsb3BtZW50U2VjcmV0S2V5Rm9ySldUMjAyNg=="));

    @Test
    void shouldParseValidToken() {
        String token = Jwts.builder()
                .subject("user-101")
                .claim("role", "CUSTOMER")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(KEY)
                .compact();

        JwtService service = new JwtService(Encoders.BASE64.encode(KEY.getEncoded()));

        assertEquals("user-101", service.parse(token).getSubject());
        assertEquals("CUSTOMER", service.parse(token).get("role", String.class));
    }
}
