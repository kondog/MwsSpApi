package japacomo;
import java.util.logging.*;

public class LoggingJapacomo {
    private static LoggingJapacomo instance;
    public static Logger logger = Logger.getLogger("japacomo");
    
    static {
        instance = new LoggingJapacomo();
        logger.setUseParentHandlers(false);
        
        try {
            Handler handler = new FileHandler("logs/japacomo.log", true);
            Formatter formatter = new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%1$tF %1$tT %2$s%n",
                            record.getMillis(),
                            record.getMessage());
                }
            };
            handler.setFormatter(formatter);
            logger.addHandler(handler);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private LoggingJapacomo() {
        // コンストラクタは空に
    }
    
    public static LoggingJapacomo getInstance() {
        return instance;
    }
}

