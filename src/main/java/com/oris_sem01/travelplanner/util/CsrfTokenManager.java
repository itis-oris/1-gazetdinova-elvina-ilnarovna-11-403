package com.oris_sem01.travelplanner.util;

import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfTokenManager {
    private static final String CSRF_TOKEN_ATTR = "CSRF_TOKEN";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateToken(HttpSession session) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = encoder.encodeToString(randomBytes);
        session.setAttribute(CSRF_TOKEN_ATTR, token);
        return token;
    }

    public static String getToken(HttpSession session) {
        String token = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        if (token == null) {
            token = generateToken(session);
        }
        return token;
    }

    public static boolean validateToken(HttpSession session, String token) {
        String storedToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        return storedToken != null && storedToken.equals(token);
    }
}
