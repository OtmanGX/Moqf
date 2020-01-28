package ma.moqf.moqf.models;

import java.util.Map;

public class theCategory {
    String title;
    Map<String, String> link;
    int icon;

    public theCategory(String title, Map link, int icon) {
        this.title = title;
        this.link = link;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getLink() {
        return link;
    }

    public void setLink(Map<String, String> link) {
        this.link = link;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
