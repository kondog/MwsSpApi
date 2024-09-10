package japacomo;

import json.GetItemOffers.GetItemOffersJson;
import json.GetItemOffers.NumberOfOffer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CheckSellerCountIncrementCtrl {
    private static final Log log = LogFactory.getLog(CheckSellerCountIncrementCtrl.class);
    private final TakeSpecifiedProperty prop;
    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;
    static DateCtrlSet dateCtrl;

    public CheckSellerCountIncrementCtrl(TakeSpecifiedProperty prop){this.prop = prop;}
    public List<String> takeLowestPricedOffersForASINS(){
        logger.log(Level.INFO, Thread.currentThread().getStackTrace()[1].getMethodName());
        String asinListFilePath = prop.getProperty("asinListFilePathForCheckSellerCountIncr");
        TakeSpecifiedProperty asin_list = new TakeSpecifiedProperty(asinListFilePath);
        String asins[] = asin_list.getPropertyAsArray("asinList", ",");

        List<String> result = takeOfferCountInfoFromApi(asins);
        outputToResultOfTakeOfferCountInfo(result);
        String resultFilePath = compareResultTodayAndYesterdayThenMakeResultFile(asins);
        mailCheckSellerCountIncrement(resultFilePath);
        return result;
    }
    public List<String> takeOfferCountInfoFromApi(String[] asins) {
        logger.log(Level.INFO, Thread.currentThread().getStackTrace()[1].getMethodName());
        List<String> result = new ArrayList<String>();

        for (String asin : asins) {
            try {
                CallMwsApi mwsApi = new CallMwsApi(this.prop);
                String condition = prop.getProperty("itemConditionForCheckSellerCountIncr");
                String lowestPricedOffersForAsin = mwsApi.takeLowestPricedOffersForASIN(asin, condition);

                JsonCtrl jsonC = new JsonCtrl();
                GetItemOffersJson getItemOffersJson = jsonC.makeGsonObj(lowestPricedOffersForAsin);
                String jsonAsin = getItemOffersJson.getPayload().getAsin();
                String jsonStatus = getItemOffersJson.getPayload().getStatus();
                int jsonTotalOfferCount = getItemOffersJson.getPayload().getSummary().getTotalOfferCount();
                List<NumberOfOffer> offers = getItemOffersJson.getPayload().
                        getSummary().
                        getNumberOfOffers();
                for (int i = 0; i < offers.size(); i++) {
                    String jsonFulfillmentChannel = offers.get(i).getFulfillmentChannel();
                    String jsonCondition = offers.get(i).getCondition();
                    int jsonOfferCount = offers.get(i).getOfferCount();
                    result.add(jsonAsin + ","
                            + jsonStatus + ","
                            + String.valueOf(jsonTotalOfferCount) + ","
                            + jsonFulfillmentChannel + ","
                            + jsonCondition + ","
                            + jsonOfferCount
                    );
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, String.format("ASIN:%s is something wrong.", asin));
                logger.log(Level.WARNING, e.toString());
            }
        }
        return result;
    }
    public void outputToResultOfTakeOfferCountInfo(List<String> results){
        logger.log(Level.INFO, Thread.currentThread().getStackTrace()[1].getMethodName());

        String outputPath = prop.getProperty("itemOfferReulstFilePathForCheckSellerCountIncr");

        String yyyymmddFilePath = outputPath +
                dateCtrl.takeTodayAsYYYYMMDD(prop.getProperty("timeZone")) + ".csv";

        Path p = Paths.get("");
        logger.log(Level.INFO, p.toAbsolutePath().toString()+"/"+yyyymmddFilePath);

        try {
            File file = new File(yyyymmddFilePath);
            if(file.exists()){logger.log(Level.WARNING, yyyymmddFilePath + " is exits, overwrite!");}
            if (file.createNewFile()) {
                FileWriter filew = new FileWriter(yyyymmddFilePath, true);
                PrintWriter pw = new PrintWriter(new BufferedWriter(filew));
                for (String result : results) {pw.println(result);}
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String compareResultTodayAndYesterdayThenMakeResultFile(String[] asins){
        logger.log(Level.INFO, Thread.currentThread().getStackTrace()[1].getMethodName());
        String outputPath = prop.getProperty("itemOfferReulstFilePathForCheckSellerCountIncr");

        String itemOfferResultFileToday = outputPath +
                        dateCtrl.takeTodayAsYYYYMMDD(prop.getProperty("timeZone")) + ".csv";
        String itemOfferResultFileYesterday = outputPath +
                dateCtrl.takeYesterdayAsYYYYMMDD(-1, prop.getProperty("timeZone")) + ".csv";

        String resultOutputPath = prop.getProperty("compareReulstFilePathForCheckSellerCountIncr");
        String compareResultFile = resultOutputPath +
                dateCtrl.takeTodayAsYYYYMMDD(prop.getProperty("timeZone")) + ".csv";

        logger.log(Level.INFO, String.format("try to compare files,%s,%s then output %s",
                itemOfferResultFileToday, itemOfferResultFileYesterday, compareResultFile));
        for (String asin : asins) {
            String compareResult = compareResultTodayAndYesterday(
                    asin, itemOfferResultFileToday, itemOfferResultFileYesterday);

            if(!compareResult.equals((""))) {
                try {
                    File file = new File(compareResultFile);
                    if (!file.exists()) {file.createNewFile();}
                    FileWriter filew = new FileWriter(compareResultFile, true);
                    PrintWriter pw = new PrintWriter(new BufferedWriter(filew));
                    pw.println(compareResult);
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return compareResultFile;
    }
    private String compareResultTodayAndYesterday(String asin,
                                                  String todayFilePath, String yesterdayFilePath)
    {
        List<String> fromToday = takeTargetAsinLineFromFile(asin, todayFilePath);
        List<String> fromYesterday = takeTargetAsinLineFromFile(asin, yesterdayFilePath);
        logger.log(Level.INFO, String.format("ASIN,%s,today,%s,yesterday,%s",
                asin, fromToday, fromYesterday
                )
        );
        return compareResultList(asin, fromToday, fromYesterday);
    }

    private List<String> takeTargetAsinLineFromFile(String asin, String filePath){
        List<String> results = new ArrayList<>();

        try{
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);

            String str;
            while ((str = bf.readLine()) != null) {
                if (str.contains(asin)){
                    results.add(str);
                }
            }
            bf.close();
        } catch (FileNotFoundException e){
            logger.log(Level.SEVERE, e.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return results;
    }

    private String compareResultList(String asin,
                                     List<String> today, List<String> yesterday) {
        int offerCountToday = takeOfferCount(today);
        int offerCountYesterday = takeOfferCount(yesterday);
        if (offerCountToday > offerCountYesterday) {
            return String.format("Num of Sale is incremented.,ASIN,%s,Yesterday,%d,Today,%d,Link,%s",
                    asin,
                    offerCountYesterday,
                    offerCountToday,
                    prop.getProperty("linkPrefixForCheckSellerCountIncr") + asin
            );
        }
        return "";
    }

    private int takeOfferCount(List<String> offerCountSrc){
        try {
            Reader in = new StringReader((offerCountSrc.get(0)));
            CSVParser parser = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : parser) {
                return Integer.parseInt(record.get(2));
            }
        }catch( Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    private void mailCheckSellerCountIncrement(String attacheFilePath){
        logger.log(Level.INFO, Thread.currentThread().getStackTrace()[1].getMethodName());
        MailSend ms = new MailSend(MailSend.MailType.COUNTINCR);
        ms.SendMailWithFileFromPropertiesFile(attacheFilePath);
    }
}
