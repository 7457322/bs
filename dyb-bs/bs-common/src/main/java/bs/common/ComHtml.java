package bs.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ComHtml {
    //加载Html代码并返回文档对象
    public static Document Load(String html) {
        Document document = Jsoup.parse(html);
        return document;
    }
}
