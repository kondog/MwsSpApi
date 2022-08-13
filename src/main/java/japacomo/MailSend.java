package main.java.japacomo;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSend {
    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;
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
            logger.log(Level.INFO, "MailTo:" + address);
            TakeSpecifiedProperty prop = new TakeSpecifiedProperty(
                    "src/main/resources/conf/mailfixed.config.properties");
            Properties gmailProperty = this.takeGmailInstance(prop);

            Session session = Session.getInstance(
                    gmailProperty,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(address, password);
                        }
                    }
            );

            MimeMessage mimeMessage = new MimeMessage(session);
            InternetAddress toAddress = new InternetAddress(
                    prop.getProperty("receiptmailaddress"),
                    prop.getProperty("receiptmailusername")
            );
            mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);

            InternetAddress fromAddress = new InternetAddress(
                    prop.getProperty("sendmailaddress"),
                    prop.getProperty("sendmailusername")
            );
            mimeMessage.setFrom(fromAddress);

            this.setMessageData(mimeMessage, prop);
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Properties takeGmailInstance(TakeSpecifiedProperty configProp){
        Properties gmailProperty = new Properties();
        gmailProperty.put("mail.smtp.host", configProp.getProperty("mailsmtphost"));
        gmailProperty.put("mail.smtp.auth", configProp.getProperty("mailsmtpauth"));
        gmailProperty.put("mail.smtp.starttls.enable", configProp.getProperty("mailsmtpstarttlsenable"));
        gmailProperty.put("mail.smtp.port", configProp.getProperty("mailsmtpport"));
        gmailProperty.put("mail.smtp.debug", configProp.getProperty("mailsmtpdebug"));
        return gmailProperty;
    }

    private void setMessageData(MimeMessage mimeMessage, TakeSpecifiedProperty configProp) throws MessagingException {
        //TODO:need to implement attachment.
        mimeMessage.setSubject(configProp.getProperty("title"), "ISO-2022-JP");
        mimeMessage.setText(configProp.getProperty("message", "ISO-2022-JP"));
    }
}


