package main.java.japacomo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JapacomoCtrl {

    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;

    public static void main(String[] args){
        logger.log(Level.INFO, "/////   START   /////");
        String types[] = {
                "GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL", //通る
//                "GET_FLAT_FILE_OPEN_LISTINGS_DATA", //通る
//                "GET_RESERVED_INVENTORY_DATA", //FATAL
//                "GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA", //通らない
//                "GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA", //通らない
//                "GET_RESERVED_INVENTORY_DATA", //通らない
        };

        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");

        for(String type :types) {
            try {
//                Date startDate = getFirstDate(new Date());
//                Date endDate = getLastDate(new Date());
                Date startDate = getYesterday(new Date());
                Date endDate = getToday(new Date());
                takeReport(type, startDate, endDate, prop);
                //TODO:takeReport then send mail.
            }catch(Exception e){
                logger.log(Level.WARNING, e.toString());
            }
        }
        logger.log(Level.INFO, "/////   END   /////");
    }

    public static Boolean takeReport(String reportType, Date start, Date end,TakeSpecifiedProperty prop){
        logger.log(Level.INFO, "***takeReportID***," + reportType +
                               "," + start.toString() +
                               "," + end.toString());
        CallMwsApi api = new CallMwsApi(prop);
        String reportID = api.takeReportID(
                reportType,
                start.toInstant().toString(),
                end.toInstant().toString());

        logger.log(Level.INFO, "*****waitUntilReportCompleted*****" + reportID);
        String reportDocumentID = api.waitUntilReportCompleted(reportID);
        if(reportDocumentID.equals("FATAL")){
            return false;
        }

        logger.log(Level.INFO, "*****takeReportAccessURL*****" + reportDocumentID);
        String reportURL = api.takeReportAccessURL(reportDocumentID);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        logger.log(Level.INFO, "*****takeReportFromURL*****" + reportURL);
        String dlFile = "/tmp/" + reportType + dateFormat.format(date) + ".gz";
        api.takeReportFromURL(dlFile, reportURL);

        String unzipFile = "/tmp/" + reportType + dateFormat.format(date) + ".tsv";
        logger.log(Level.INFO, "*****unzipFile*****" + dlFile + unzipFile);
        api.unzipFile(dlFile, unzipFile);

        return true;
    }
    public static Date getFirstDate(Date date) {

        if (date==null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int first = calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE, first);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }
    public static Date getLastDate(Date date) {

        if (date == null) return null;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        int last = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, last);

//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 999);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }
    public static Date getToday(Date date) {
        if (date==null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        int first = calendar.getActualMinimum(Calendar.DATE);
//        calendar.set(Calendar.DATE, first);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }
    public static Date getYesterday(Date date) {
        if (date==null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        int first = calendar.getActualMinimum(Calendar.DATE);
//        calendar.set(Calendar.DATE, first);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }

}
