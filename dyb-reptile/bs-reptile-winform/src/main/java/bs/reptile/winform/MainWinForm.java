package bs.reptile.winform;

//import org.dom4j.Document;
//import org.dom4j.io.SAXReader;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWinForm {
    ReptileRobot reptile = new ReptileRobot();
    private JPanel Main;
    private JButton btn执行;
    private JTextArea ta配置;
    private JPanel bottom;
    private JPanel left;
    private JPanel right;
    private JPanel center;
    public MainWinForm() {
        btn执行.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                execute();
                super.mouseClicked(e);
            }
        });
    }

    public void execute() {
        reptile.loadConfigs(ta配置.getText());
        reptile.loadCache();
        reptile.execute();
        reptile.save();
    }

    public void run() {
        javax.swing.JFrame frame = new javax.swing.JFrame("test");
        frame.setContentPane(new MainWinForm().Main);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String aa = "00";

    }
}
