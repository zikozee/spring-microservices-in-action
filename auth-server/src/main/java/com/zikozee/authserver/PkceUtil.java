package com.zikozee.authserver;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class PkceUtil {
    static String generateCodeVerifier() throws UnsupportedEncodingException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    public static void main(String[] args) {

        try {
            String codeVerifier = generateCodeVerifier();
            System.out.println("Code verifier = " + codeVerifier);

            String codeChallenge = generateCodeChallenge(codeVerifier);
            System.out.println("Code challenge = " + codeChallenge);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            System.out.println(passwordEncoder.encode("12345"));

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
