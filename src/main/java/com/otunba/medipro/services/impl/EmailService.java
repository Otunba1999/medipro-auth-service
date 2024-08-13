package com.otunba.medipro.services.impl;

import com.otunba.medipro.dtos.MailRequest;
import com.otunba.medipro.services.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final RestTemplate restTemplate;
    private  final  static  String SERVICE_URL = "http://localhost:8282/api/medipro/send-html-message";
    @Override
    public void sendVerificationEmail(MailRequest request) {
       ResponseEntity<String> response = restTemplate.postForEntity(SERVICE_URL, request, String.class);
       log.info("response: {}", response.getBody());
    }

    @Override
    public void resetPassword(MailRequest request) {

    }

    @Override
    public void sendTextEmail(MailRequest request) {

    }
}
