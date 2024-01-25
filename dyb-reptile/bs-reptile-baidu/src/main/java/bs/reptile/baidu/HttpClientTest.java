package bs.reptile.baidu;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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
//        String html = getUrl("https://www.taobao.com/");
//        taText.setText(html);
        Document document = loadFile("E:\\projects\\Doc\\test.html");

    }

    public String getUrl(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        String content = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public Document loadFile(String file) {
        SAXReader sr = new SAXReader();
        Document doc = null;
        try {
            doc = sr.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public Document loadHtml(String html) {
        return loadHtml(html, "UTF-8");
    }

    public Document loadHtml(String html, String encode) {
        SAXReader sr = new SAXReader();
        Document doc = null;
        try {
            doc = sr.read(new ByteArrayInputStream(html.getBytes(encode)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
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
