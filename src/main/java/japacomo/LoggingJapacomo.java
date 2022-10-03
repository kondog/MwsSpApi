package japacomo;
import java.util.logging.*;

public class LoggingJapacomo {
    public static Logger logger = Logger.getLogger("japacomo");
    LoggingJapacomo(){
        try {
            //TODO:logs should output not /tmp/ but local folder src/logs or buraburabura.
            Handler handler = new FileHandler("/tmp/japacomo.log",true);
            logger.addHandler(handler);
            Formatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        }catch(Exception e) {
        }
    }

}

