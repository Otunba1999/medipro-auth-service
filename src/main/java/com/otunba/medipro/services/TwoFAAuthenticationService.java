package com.otunba.medipro.services;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Slf4j
@Service
public class TwoFAAuthenticationService {

    public String generateNewSecret(){
        return new DefaultSecretGenerator().generate();
    }
    public String generateQRCodeImageUri(String secret){
        QrData data = new QrData.Builder()
                .label("Medipro")
                .secret(secret)
                .issuer("medipro service")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageDate = new byte[0];
        try {
            imageDate = generator.generate(data);
        }catch (QrGenerationException e){
            e.printStackTrace();
            log.error("Error generating QR code", e);
        }
        return Utils.getDataUriForImage(imageDate, generator.getImageMimeType());
    }
    public  boolean isOtpValid(String secret, String code) {
        log.info("Validating OTP. Secret: {}, Code: {}", secret, code);
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        DefaultCodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        codeVerifier.setTimePeriod(60);
        codeVerifier.setAllowedTimePeriodDiscrepancy(2);
        boolean validCode = codeVerifier.isValidCode(secret, code);
        log.info("Is OTP valid? {}", validCode);
        return validCode;
    }
    public boolean isOtpNotValid(String secret, String code) {
        return !this.isOtpValid(secret, code);
    }
}
