package japacomo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JapacomoCtrl {

    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;
    static DateCtrlSet dateCtrl;

    public static void main(String[] args){
        logger.log(Level.INFO, "/////   START:" + args[0] +"   /////");
            TakeSpecifiedProperty prop_us = new
                    TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
            TakeSpecifiedProperty prop_uk = new
                    TakeSpecifiedProperty("src/main/resources/conf/uk.config.properties");
            TakeSpecifiedProperty prop_cn = new
                    TakeSpecifiedProperty("src/main/resources/conf/cn.config.properties");

        switch (args[0]) {
            //TODO:it should be common interface class among takereport and check seller count increment.
            case "TakeReport": {
                //TODO:takeReport classes should devide another package.
                takeReportFromSpecifiedProperty(prop_us);
                takeReportFromSpecifiedProperty(prop_uk);
                takeReportFromSpecifiedProperty(prop_cn);
            }
            case "CheckSellerCountIncrement": {
                CheckSellerCountIncrement(prop_us);
            }
        }
        logger.log(Level.INFO, "/////   END   /////");
    }

    public static void takeReportFromSpecifiedProperty(TakeSpecifiedProperty prop){
        //TODO:make class DownloadReport and move implementation to there.
        String types[] = prop.getPropertyAsArray("downloadReportTypes", ",");

        Date targetDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String targetDir = prop.getProperty("downloadDir") + dateFormat.format(targetDate) + "/";

        for(String type :types) {
            try {
                Date startDate = dateCtrl.getYesterday(new Date(), -2, prop.getProperty("timeZone"));
                Date endDate = dateCtrl.getToday(new Date(), prop.getProperty("timeZone"));
                takeReport(type, startDate, endDate, targetDate, targetDir, prop);
            }catch(Exception e){
                logger.log(Level.WARNING, e.toString());
            }
        }

        MailSend mail = new MailSend(MailSend.MailType.REPROT);
        mail.SendMailWithDirFromPropertiesFile(targetDir);
    }
    public static Boolean takeReport(String reportType,
                                     Date start,
                                     Date end,
                                     Date targetDate,
                                     String targetDir,
                                     TakeSpecifiedProperty prop){
        logger.log(Level.INFO, "***010_takeReportID***," + prop.getProperty("confIdentifier") +
                               "," + reportType +
                               "," + start.toString() +
                               "," + end.toString() +
                               "," + targetDir);
        setupForDownloadReport(targetDir);
        CallMwsApi api = new CallMwsApi(prop);
        String reportID = api.takeReportID(
                reportType,
                start.toInstant().toString(),
                end.toInstant().toString());

        logger.log(Level.INFO, "*****020_waitUntilReportCompleted*****" + reportID);
        String reportDocumentID = api.waitUntilReportCompleted(reportID);
        if(reportDocumentID.equals("FATAL")){return false;}

        logger.log(Level.INFO, "*****030_takeReportAccessURL*****" + reportDocumentID);
        String reportURL = api.takeReportAccessURL(reportDocumentID);

        String fileNameBasis = prop.getProperty("confIdentifier") + "_" + reportType;
        logger.log(Level.INFO, "*****040_takeReportFromURL*****" + reportURL);
        String dlFile = targetDir + fileNameBasis + ".gz";
        api.takeReportFromURL(dlFile, reportURL);

        String unzipFile = targetDir + fileNameBasis + ".tsv";
        logger.log(Level.INFO, "*****050_unzipFile*****" + dlFile + "," + unzipFile);
        api.unzipFile(dlFile, unzipFile);

        return true;
    }
    private static void setupForDownloadReport(String setupDir){
        try {
            Path path1 = Paths.get(setupDir);
            if(!Files.exists(path1)){Files.createDirectories(path1);}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CheckSellerCountIncrement(TakeSpecifiedProperty prop) {
        CheckSellerCountIncrementCtrl checkIncr =
            new CheckSellerCountIncrementCtrl(prop);
        String asinListFilePath = prop.getProperty("asinListFilePathForCheckSellerCountIncr");
        TakeSpecifiedProperty asin_list = new TakeSpecifiedProperty(asinListFilePath);
        String asins[] = prop.getPropertyAsArray("asinList", ",");
    }


}
