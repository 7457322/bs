package bs.reptile.baidu;

import bs.common.ComHtml;
import bs.common.ComHttp;
//import org.dom4j.Document;
//import org.dom4j.io.SAXReader;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HttpClientTest {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton 执行Button;
    private JButton 清空Button;
    private JTextArea taEditor;
    private JTextField tfEditor;

    public HttpClientTest() {
//        //设置自动换行
//        taText.setLineWrap(true);
////定义带滚动条的panel，并将JTextArea存入到panel中，使textarea具有滚动条显示功能。
//        JScrollPane scrollpane = new JScrollPane(taText);
////取消显示水平滚动条
//        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
////显示垂直滚动条
//        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        执行Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getInfo();
                super.mouseClicked(e);
            }
        });
        清空Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);
            }
        });
    }

    public  void clearEdit(){
        taEditor.setText("");
    }

    public void getInfo() {
        try {
            String html = ComHttp.getUrl("https://www.taobao.com/");
            taEditor.setText(html);
            Document htmDom = ComHtml.Load(html);
            Elements elements = htmDom.select(".J_Module");
            System.out.println("test");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        javax.swing.JFrame frame = new javax.swing.JFrame("test");
        frame.setContentPane(new HttpClientTest().panel1);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
