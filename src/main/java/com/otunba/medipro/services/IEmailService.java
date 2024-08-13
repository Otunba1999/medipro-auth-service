package com.otunba.medipro.services;

import com.otunba.medipro.dtos.MailRequest;

public interface IEmailService {
    void sendVerificationEmail(MailRequest request);
    void resetPassword(MailRequest request);
    void sendTextEmail(MailRequest request);
}
