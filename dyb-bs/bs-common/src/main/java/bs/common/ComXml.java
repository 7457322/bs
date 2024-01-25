package bs.common;

public class ComXml {
    public static Document loadFile(String file) {
        SAXReader sr = new SAXReader();
        Document doc = null;
        try {
            doc = sr.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document load(String xml) {
        return loadHtml(html, "UTF-8");
    }

    public static Document load(String html, String encode) {
        SAXReader sr = new SAXReader();
        Document doc = null;
        try {
            doc = sr.read(new ByteArrayInputStream(html.getBytes(encode)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
}
