package test.java;

import japacomo.MailSend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailSendTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void sendMail() {
        MailSend ms = new MailSend();
        ms.sendMailFromPropertiyFiles("src/test/java/testresources/");
    }
}