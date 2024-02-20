package bs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComLog {
    static final Logger logger = LoggerFactory.getLogger(ComLog.class);

    //调试信息
    public static void debug(String msg) {
        logger.debug(msg);
    }

    //调试信息
    public static void info(String msg) {
        logger.info(msg);
    }        //调试信息

    public static void warn(String msg) {
        logger.warn(msg);
    }

    //调试信息
    public static void error(String msg) {
        logger.error(msg);
    }
}