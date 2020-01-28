package ma.moqf.moqf.parsers;

import ma.moqf.moqf.models.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class myHtmlParser {
    public static void parseArticle(String content, Article article) throws Exception {
        Document doc = Jsoup.parse(content);
        String price = doc.select("div.price").first().text();
        article.setPrice(price);
        article.setTel(doc.getElementById("phone-N").ownText());
        article.setDate(doc.getElementById("date-Pn").text());
        article.setEmail(doc.getElementById("email-TT")!=null?doc.getElementById("email-TT").text():"");

        Elements imagesList = null;
        try {
            imagesList = doc.getElementById("photos-M").select("a");
            String[] images = new String[imagesList.size()];
            article.setOtherimages(images);
            int temp = 0;
            for (Element img:
                    imagesList) {
                images[temp] = img.attr("href");
                temp++;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
