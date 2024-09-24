package com.otunba.medipro.services.impl;

import com.otunba.medipro.dtos.MailRequest;
import com.otunba.medipro.exceptions.AuthException;
import com.otunba.medipro.services.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final RestTemplate restTemplate;
    private  final  static  String SERVICE_URL = System.getenv("MAIL_URL");
    @Override
    public void sendVerificationEmail(MailRequest request) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(SERVICE_URL, request, String.class);
            log.info("response: {}", response.getBody());
        }catch (RestClientException e) {
            log.error("Unable to connect to medipro-email server. Error: {}", e.getMessage());
            throw new AuthException("Unable to connect to email server, check your email address, connection and try again");
        }

    }

    @Override
    public void resetPassword(MailRequest request) {

    }

    @Override
    public void sendTextEmail(MailRequest request) {

    }
}
