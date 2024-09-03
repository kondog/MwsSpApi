package test.java;

import japacomo.MailSend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MailSendTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void sendMail() {
        MailSend ms = new MailSend(MailSend.MailType.REPORT);
        ms.SendMailWithDirFromPropertiesFile("src/test/java/testresources/");
    }
}