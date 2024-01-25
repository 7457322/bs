package bs.reptile.baidu;

import bs.common.ComHtml;
import bs.common.ComHttp;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.dom4j.Document;
//import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public class HttpClientTest {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextArea taText;
    private JButton 执行Button;
    private JButton 清空Button;

    public HttpClientTest() {
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

    public void getInfo() {
        try {

            String html = new ComHttp().getUrl("https://www.taobao.com/");
            Document htmDom = ComHtml.Load(html);
            Elements elements = htmDom.select(".J_Module");
            System.out.println("test");

            //获取title的内容
//            System.out.println(document.);
//获取DocumentBuilder

//            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            //需要解析html
//            org.w3c.dom.Document document = documentBuilder.parse(new ByteArrayInputStream(html.getBytes()));
//            System.out.println(document.getDocumentElement().get("span").item(0).getAttributes().getNamedItem("attendee-id").getNodeValue());
//            System.out.println(document.getElementsByTagName("div").item(0).getTextContent());
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
