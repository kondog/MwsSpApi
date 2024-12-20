import japacomo.CallMwsApi;
import japacomo.CheckSellerCountIncrementCtrl;
import japacomo.JapacomoCtrl;
import japacomo.TakeSpecifiedProperty;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class TakeReportTest {
    @Test
    public void takeMonthlyReportTest() {
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.forShortTime.properties");
        JapacomoCtrl.takeReportFromSpecifiedProperty(prop, "TakeReport_Month");
    }
}
