package ru.guap.rssreader;

/**
 * Created by MeatBoy on 29.07.2015.
 */
public class NewsItem {

    private String mTitle;
    private String mLink;

    public NewsItem(String title, String link) {
        mTitle = title;
        mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

}
