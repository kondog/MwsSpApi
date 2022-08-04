package main.java.japacomo;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSend {
    public void sendMail() {
        try {
            Properties property = new Properties();
            property.put("mail.smtp.host", "smtp.gmail.com");
            property.put("mail.smtp.auth", "true");
            property.put("mail.smtp.starttls.enable", "true");
            property.put("mail.smtp.host", "smtp.gmail.com");
            property.put("mail.smtp.port", "587");
            property.put("mail.smtp.debug", "true");

            Session session = Session.getInstance(property, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("Gmailのアカウント", "Gmailのパスワード");
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            InternetAddress toAddress = new InternetAddress("受信者メールアドレス", "受信者名");
            mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
            InternetAddress fromAddress = new InternetAddress("送信者メールアドレス", "送信者名");
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setSubject("title", "ISO-2022-JP");
            mimeMessage.setText("message", "ISO-2022-JP");
            Transport.send(mimeMessage);
            System.out.println("メール送信が完了しました。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


