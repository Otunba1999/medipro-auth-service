package com.otunba.medipro;

import com.otunba.medipro.services.TwoFAAuthenticationService;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MediproApplication {

	public static void main(String[] args) throws CodeGenerationException {
		SpringApplication.run(MediproApplication.class, args);
//		TwoFAAuthenticationService service = new TwoFAAuthenticationService();
//		String secret = service.generateNewSecret();
//        log.info("Secret generated: {}", secret);
//		CodeGenerator codeGenerator = new DefaultCodeGenerator();
//		String expectedOtp = String.valueOf(codeGenerator.generate(secret, 300000));
//		log.info("OTP generated: {}", expectedOtp);
//		boolean isValid = service.isOtpValid(secret, expectedOtp);
//		log.info("OTP valid: {}", isValid);
	}

}
