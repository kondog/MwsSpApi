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
    static final String TAKE_REPORT = "TakeReport";
    static final String TAKE_REPORT_MONTH = "TakeReport_Month";
    static final String CHECK_SELLER_COUNT_INCR = "CheckSellerCountIncrement";

    public static void main(String[] args){
        logger.log(Level.INFO, "/////   START:" + args[0] +"   /////");
        TakeSpecifiedProperty prop_us = new
                TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        TakeSpecifiedProperty prop_uk = new
                TakeSpecifiedProperty("src/main/resources/conf/uk.config.properties");
        TakeSpecifiedProperty prop_cn = new
                TakeSpecifiedProperty("src/main/resources/conf/cn.config.properties");

        String argument = args[0];

        switch (argument) {
            //TODO:it should be common interface class among takereport and check seller count increment.
            case TAKE_REPORT: {
                //TODO:takeReport classes should devide another package.
                takeReportFromSpecifiedProperty(prop_us, argument);
                takeReportFromSpecifiedProperty(prop_uk, argument);
                takeReportFromSpecifiedProperty(prop_cn, argument);
                break;
            }
            case TAKE_REPORT_MONTH: {
                //TODO:takeReport classes should devide another package.
                takeReportFromSpecifiedProperty(prop_us, argument);
                takeReportFromSpecifiedProperty(prop_uk, argument);
                takeReportFromSpecifiedProperty(prop_cn, argument);
                break;
            }
            case CHECK_SELLER_COUNT_INCR: {
                CheckSellerCountIncrement(prop_us);
                break;
            }
            case "Test":{
                //todo: want to implement check configuration process.
                //for example, check config file exists, check directory exists, etc.
                break;
            }
        }
        logger.log(Level.INFO, "/////   END   /////");
    }

    public static void takeReportFromSpecifiedProperty(
            TakeSpecifiedProperty prop, String specifiedArgument){
        //TODO:make class DownloadReport and move implementation to there.

        Date targetDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        String targetDir = "";
        String mailTitlePrefix = "";

        try{
            switch (specifiedArgument) {
                case TAKE_REPORT: {
                    String types[] = prop.getPropertyAsArray("downloadReportTypes", ",");
                    targetDir = prop.getProperty("downloadDir") + dateFormat.format(targetDate) + "/";
                    for (String type : types) {
                        Date startDate = DateCtrlSet.getYesterday(new Date(), -2, prop.getProperty("timeZone"));
                        Date endDate = DateCtrlSet.getToday(new Date(), prop.getProperty("timeZone"));
                        takeReport(type, startDate, endDate, targetDate, targetDir, prop, 1);
                    }
                    break;
                }
                case TAKE_REPORT_MONTH: {
                    String types[] = prop.getPropertyAsArray("downloadReportTypes_MONTH", ",");
                    targetDir = prop.getProperty("downloadDir") +
                            dateFormat.format(targetDate) + "_MONTH" + "/";
                    mailTitlePrefix = "_MONTH";
                    for (String type : types) {
                        Date startDate = DateCtrlSet.getFirstDateOfLastMonth(new Date(), prop.getProperty("timeZone"));
                        Date endDate = DateCtrlSet.getLastDateOfLastMonth(new Date(), prop.getProperty("timeZone"));
                        takeReport(type, startDate, endDate, targetDate, targetDir, prop, 10);
                    }
                    break;
                }
                default:{
                    logger.log(Level.SEVERE, "no argument specied to takeReportFromSpecifiedProperty.");
                    return;
                }
            }
        }catch(Exception e) {
            logger.log(Level.WARNING, e.toString());
        }

        MailSend mail = new MailSend(MailSend.MailType.REPORT);
        mail.SendMailWithDirFromPropertiesFile(targetDir, prop.getProperty("confIdentifier") + mailTitlePrefix);
    }
    public static Boolean takeReport(String reportType,
                                     Date start,
                                     Date end,
                                     Date targetDate,
                                     String targetDir,
                                     TakeSpecifiedProperty prop,
                                     int waitMinute){
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
        String reportDocumentID = api.waitUntilReportCompleted(reportID, waitMinute);
        if(reportDocumentID.equals("FATAL") | reportDocumentID.equals("CANCELLED")){
            logger.log(Level.WARNING, "mws returns unexpected report status:" + reportDocumentID);
            return false;
        }

        logger.log(Level.INFO, "*****030_takeReportAccessURL*****" + reportDocumentID);
        String reportURL = api.takeReportAccessURL(reportDocumentID);

        String fileNameBasis = prop.getProperty("confIdentifier") + "_" + reportType;
        logger.log(Level.INFO, "*****040_takeReportFromURL*****" + reportURL);
        String dlFile = targetDir + fileNameBasis;
        api.takeReportFromURL(dlFile, reportURL);

        logger.log(Level.INFO, "*****050_unzipFile*****" + dlFile);
        FileCtrlSet.setExtentionToFile(dlFile, true);

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
        logger.log(Level.INFO, "***CheckSellerCountIncrement Start***," + prop.getProperty("confIdentifier"));

        CheckSellerCountIncrementCtrl checkIncr =
                new CheckSellerCountIncrementCtrl(prop);
        checkIncr.takeLowestPricedOffersForASINS(prop.getProperty("confIdentifier"));

        logger.log(Level.INFO, "***CheckSellerCountIncrement End***," + prop.getProperty("confIdentifier"));
    }


}
