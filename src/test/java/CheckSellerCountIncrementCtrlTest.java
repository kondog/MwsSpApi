package test.java;

import japacomo.CallMwsApi;
import japacomo.CheckSellerCountIncrementCtrl;
import japacomo.TakeSpecifiedProperty;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class CheckSellerCountIncrementCtrlTest {
    @Test
    public void testTakeLowestPricedOffersForASINS() {
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        CheckSellerCountIncrementCtrl checkIncr = new CheckSellerCountIncrementCtrl(prop);
        checkIncr.takeLowestPricedOffersForASINS();
        //mail send, then ok. for now.
    }

    @Test
    public void testOutputToResultOfTakeOfferCountInfo() {
        Path p = Paths.get("");
        System.out.println(p.toAbsolutePath().toString());
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        CheckSellerCountIncrementCtrl checkIncr = new CheckSellerCountIncrementCtrl(prop);

        Path p2 = Paths.get("");
        System.out.println(p2.toAbsolutePath().toString());
        checkIncr.outputToResultOfTakeOfferCountInfo(
                List.of("ASINAAAA,STATUS,10,MERCHANT,new,5",
                        "ASINAAAA,STATUS,5,MERCHANT,new,5",
                        "ASINAAAB,STATUS,10,MERCHANT,new,5"));
    }

    @Test
    public void testCallOfferCountApiWithAbnormalASINPattern() {
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        CheckSellerCountIncrementCtrl checkIncr = new CheckSellerCountIncrementCtrl(prop);
        checkIncr.takeOfferCountInfoFromApi(new String []{""});
    }

    @Test
    public void testCallOfferCountApiWithSpecifiedASIN(){
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        CheckSellerCountIncrementCtrl checkIncr = new CheckSellerCountIncrementCtrl(prop);
        List<String> result = checkIncr.takeOfferCountInfoFromApi(new String []{"B00AE1C1ZC"});
        System.out.println(result.toString());
    }


}