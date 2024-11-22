import japacomo.CheckSellerCountIncrementCtrl;
import japacomo.JapacomoCtrl;
import japacomo.TakeSpecifiedProperty;

import org.junit.jupiter.api.Test;

public class IntegrationTest {

    @Test
    public void TestAllMethods(){
        TestAllImportantMethods_ShortTerm();
        TestAllImportantMethods_NeedLogTerm();
    }

    @Test
    public void TestAllImportantMethods_ShortTerm() {
        TakeSpecifiedProperty prop_us = new
                TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        JapacomoCtrl.takeReportFromSpecifiedProperty(prop_us, "TakeReport");

        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        CheckSellerCountIncrementCtrl checkIncr = new CheckSellerCountIncrementCtrl(prop);
//        checkIncr.takeLowestPricedOffersForASINS();
        //mail send, then ok. for now.
    }

    @Test
    public void TestAllImportantMethods_NeedLogTerm(){

        TakeSpecifiedProperty prop_us = new
                TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        //Need 30 minutes to output monthly report.
        JapacomoCtrl.takeReportFromSpecifiedProperty(prop_us, "TakeReport_Month");

    }
}
