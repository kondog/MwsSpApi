package main.java.japacomo;

import java.io.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSend {
    public void sendMailFromPropertiyFiles(){
        String propertyFilePath = "src/main/resources/conf/mailaddress.config.properties";
        File file = new File(propertyFilePath);
        if (!file.exists()) {
            System.out.print("ファイルが存在しません");
            return;
        }
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] arrayStr = line.split(",");
                this.sendMail(arrayStr[0], arrayStr[1]);
            }
        } catch (IOException e) {
                System.out.println(e);
        }
    }
    public void sendMail(String address, String password) {
        try {
            TakeSpecifiedProperty prop = new TakeSpecifiedProperty(
                    "/src/main/resources/conf/mailfixed.config.properties");
            Properties property = new Properties();
            property.put("mail.smtp.host", prop.getProperty("mail.smpt.host"));
            property.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
            property.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable"));
            property.put("mail.smtp.port", prop.getProperty("mail.smtp.port"));
            property.put("mail.smtp.debug", prop.getProperty("mail.smtp.debug"));

            Session session = Session.getInstance(
                    property,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    address, password
                            );
                        }
                    }
            );

            MimeMessage mimeMessage = new MimeMessage(session);
            InternetAddress toAddress = new InternetAddress(
                    prop.getProperty("receipt.mail.address"),
                    prop.getProperty("receipt.mail.username")
            );
            mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
            InternetAddress fromAddress = new InternetAddress(
                    prop.getProperty("send.mail.address"),
                    prop.getProperty("send.mail.username")
            );
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setSubject(prop.getProperty("title"), "ISO-2022-JP");
            mimeMessage.setText(prop.getProperty("message", "ISO-2022-JP"));
            Transport.send(mimeMessage);
            System.out.println("メール送信が完了しました。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


