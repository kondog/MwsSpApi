import japacomo.DateCtrlSet;
import japacomo.FileCtrlSet;
import japacomo.TakeSpecifiedProperty;
import org.junit.jupiter.api.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import java.util.Date;

public class DateCtrlSetTest {
    @Test
    public void Test(){
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date testDate = cal.getTime();

        Date endDate = DateCtrlSet.getFirstDateOfMonth(testDate, prop.getProperty("timeZone"));
        Date startDate = DateCtrlSet.getFirstDateOfLastMonth(testDate, prop.getProperty("timeZone"));
        System.out.println(endDate);
        System.out.println(startDate);
    }
}
