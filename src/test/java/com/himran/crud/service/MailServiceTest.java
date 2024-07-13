package com.himran.crud.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @Test
    public void sendEmail(){
        mailService.sendMail("developerhimran@gmail.com","Test Mail Using JavaMail","Hello I Am From Imran Biswas");
    }
}
