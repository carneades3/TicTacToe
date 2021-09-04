
import java.util.logging.Logger;

public class GlobalLogger {
   public static final Logger GLOBAL_LOGGER = Logger.getGlobal();
   
   public static void logInfo(String firstIntegerInfo, int firstInteger, String secondIntegerInfo, int secondInteger) {
      final String MESSAGE = String.format("%s %d             %s %d", 
                              firstIntegerInfo, firstInteger, secondIntegerInfo, secondInteger);
                              
      GLOBAL_LOGGER.info(MESSAGE);
   }
}
