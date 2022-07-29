package main.java.japacomo;
import java.util.logging.*;

public class LoggingJapacomo {
    public static Logger logger = Logger.getLogger("japacomo");
    LoggingJapacomo(){
        try {
            Handler handler = new FileHandler("/tmp/japacomo.log",true);
            logger.addHandler(handler);
            Formatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        }catch(Exception e) {
        }
    }

}

