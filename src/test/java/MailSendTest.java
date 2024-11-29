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
        ms.SendMailWithDirFromPropertiesFile("src/test/java/testresources/", "mail send test");
    }

    @Test
    void sendMailForCheckIncr(){
        MailSend ms = new MailSend(MailSend.MailType.COUNTINCR);
        ms.SendMailWithFileFromPropertiesFile("src/test/java/testresources/testFileForMailSend.txt", "chekIncrTest");
    }
}