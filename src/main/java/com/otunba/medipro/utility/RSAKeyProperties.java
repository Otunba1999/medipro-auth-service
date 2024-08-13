package com.otunba.medipro.utility;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static com.otunba.medipro.utility.KeyGenerator.generateRSAKey;

@Getter
@Component
public class RSAKeyProperties {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public RSAKeyProperties() {
        KeyPair keyPair = generateRSAKey();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

}
