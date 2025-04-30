package japacomo;
import java.util.logging.*;

public class LoggingJapacomo {
    private static LoggingJapacomo instance;
    public static Logger logger = Logger.getLogger("japacomo");
    
    static {
        instance = new LoggingJapacomo();
    }
    
    private LoggingJapacomo() {
        try {
            //TODO:logs should output not /tmp/ but local folder src/logs or buraburabura.
//            Handler handler = new FileHandler("/tmp/japacomo.log",true);
            Handler handler = new FileHandler("logs/japacomo.log", true);
            logger.addHandler(handler);
            Formatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static LoggingJapacomo getInstance() {
        return instance;
    }
}

