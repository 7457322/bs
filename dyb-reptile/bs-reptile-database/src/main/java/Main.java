import bs.common.ComCfg;
import bs.reptile.database.ComDbCodeGenerator;

public class Main {
    public static void main(String[] args) {
        ComDbCodeGenerator.output(ComCfg.getCallPath().replaceFirst("[^\\\\]+\\\\$","")+"bs-reptile-database\\src\\main\\java");
    }
}