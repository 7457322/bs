import bs.ComDbCodeGenerator;
import bs.common.ComCfg;

public class Main {
    public static void main(String[] args) {
        String projectPath = ComCfg.getCallPath()//.replaceFirst("[^\\\\]+\\\\$", "")
                + "bs-reptile-database\\src\\main\\";
        ComDbCodeGenerator
                .setPrefix("bs")
                .outputDatabase(projectPath);
    }
}