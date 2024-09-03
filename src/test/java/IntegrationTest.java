import japacomo.CheckSellerCountIncrementCtrl;
import japacomo.JapacomoCtrl;
import japacomo.TakeSpecifiedProperty;

import org.junit.jupiter.api.Test;

public class IntegrationTest {
    @Test
    public void TestAllImportantMethods() {
        TakeSpecifiedProperty prop_us = new
                TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        JapacomoCtrl.takeReportFromSpecifiedProperty(prop_us);

        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        CheckSellerCountIncrementCtrl checkIncr = new CheckSellerCountIncrementCtrl(prop);
        checkIncr.takeLowestPricedOffersForASINS();
        //mail send, then ok. for now.

    }
}
