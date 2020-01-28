package ma.moqf.moqf.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
    private String title;
    private String url;
    private String imageUrl;
    private String[] otherimages;
    private String date;
    private String city;
    private String category;
    private String description;
    private String tel;
    private String author;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String price;

    public Article(String title, String url, String imageUrl) {
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String[] getOtherimages() {
        return otherimages;
    }

    public void setOtherimages(String[] otherimages) {
        this.otherimages = otherimages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    //getters & setters

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeStringArray(this.otherimages);
        dest.writeString(this.date);
        dest.writeString(this.city);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeString(this.tel);
        dest.writeString(this.author);
        dest.writeString(this.email);
        dest.writeString(this.price);
    }

    protected Article(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.otherimages = in.createStringArray();
        this.date = in.readString();
        this.city = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.tel = in.readString();
        this.author = in.readString();
        this.email = in.readString();
        this.price = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}