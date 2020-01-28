package ma.moqf.moqf.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ma.moqf.moqf.models.Article;

public class myXmlParser {
    private static final String feedUrl = "http://www.moqf.ma/index.php?page=search&sFeed=rss";

    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

    private static Document getDoc(String content) throws ParserConfigurationException, SAXException, IOException{
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(content));
            doc = db.parse(is);

        return doc;
    }


    public static ArrayList<Article> parsefeed(String content) throws Exception {
        ArrayList<Article> articles = new ArrayList<Article>();
        NodeList nodelist;
        Document doc = getDoc(content);

        doc.getDocumentElement().normalize();
        // Locate the Tag Name

        nodelist = doc.getElementsByTagName("item");

        for (int temp = 0; temp < nodelist.getLength(); temp++) {
            Node nNode = nodelist.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                // Get the image
                Element e = (Element) eElement.getElementsByTagName("description").item(0);
                Node n = (Node) e;

                String s = e.getElementsByTagName("image").item(0).getTextContent();
                Article article = new Article(getNode("title", eElement),
                        getNode("link", eElement), s);
                article.setCategory(getNode("category", eElement));
                article.setDescription(n.getLastChild().getTextContent());
                article.setCity(getNode("city", eElement));
                String author = ((Node)eElement.getElementsByTagName("author").item(0)).getTextContent();

//                article.setAuthor(getNode("author", eElement));
                article.setAuthor(author);
                articles.add(article);

            }
        }
        return articles;
    }
}
