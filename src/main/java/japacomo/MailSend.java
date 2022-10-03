package japacomo;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSend {
    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;
    public void sendMailFromPropertiyFiles(String targetDir){
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
                this.sendMail(arrayStr[0], arrayStr[1], takeAttachmentFiles(targetDir));
            }
        } catch (IOException e) {
                System.out.println(e);
        }
    }
    private List<String> takeAttachmentFiles(String targetDir){
        File f = new File(targetDir);
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
        List<String> files = new ArrayList<String>();
        for(String name : names){
            String ext = name.substring(name.lastIndexOf(".") + 1);
            if(ext.equals("tsv")){files.add(targetDir + name);}
        }
        return files;
    }
    public void sendMail(String address, String password, List<String> files) {
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
            mimeMessage.setRecipient(Message.RecipientType.TO,
                    InternetAddress.parse(prop.getProperty("receiptmailaddress"));

            InternetAddress fromAddress = new InternetAddress(
                    prop.getProperty("sendmailaddress"),
                    prop.getProperty("sendmailusername")
            );
            mimeMessage.setFrom(fromAddress);

            this.setBodyData(mimeMessage, prop, files);
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

    private void setBodyData(MimeMessage mimeMessage,
                             TakeSpecifiedProperty configProp,
                             List<String> files
    ) throws MessagingException {
        mimeMessage.setSubject(configProp.getProperty("title"), "ISO-2022-JP");
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(configProp.getProperty("message", "ISO-2022-JP"));

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        for(String file : files){
            BodyPart attachmentBodyPart = makeAttachedBodyPart(file);
            multipart.addBodyPart(attachmentBodyPart);
        }

        mimeMessage.setContent(multipart);
    }
    private BodyPart makeAttachedBodyPart(String attachFile) throws MessagingException{
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        String filename = attachFile;
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        return messageBodyPart;
    }
}


