package com.HotelBookingSystem.BookingBro.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // guaranteed ≥ 512 bits
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Your secure JWT secret key (Base64):");
        System.out.println(base64Key);
    }
}
