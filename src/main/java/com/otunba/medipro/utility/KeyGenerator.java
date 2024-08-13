package com.otunba.medipro.utility;

import com.otunba.medipro.exceptions.AuthException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGenerator {
    public static KeyPair generateRSAKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new AuthException("RSA key generation failed " + e.getMessage());
        }
        return keyPair;
    }
}
