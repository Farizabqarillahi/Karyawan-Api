package com.testcompany.fariz.karyawan.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}") // 1 jam
    private long expirationTime;

    public String generateToken(String username) throws JOSEException {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        JWSSigner signer = new MACSigner(signingKey.getEncoded());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(now)
                .expirationTime(expiryDate)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public String validateToken(String token) {
        try {
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKey signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

            SignedJWT signedJWT = SignedJWT.parse(token);

            // Periksa apakah token sudah kedaluwarsa
            if (new Date().after(signedJWT.getJWTClaimsSet().getExpirationTime())) {
                return null; // Token sudah kedaluwarsa
            }

            JWSVerifier verifier = new MACVerifier(signingKey.getEncoded());

            if (signedJWT.verify(verifier)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                return null;
            }
        } catch (ParseException | JOSEException e) {
            return null;
        }
    }
}
