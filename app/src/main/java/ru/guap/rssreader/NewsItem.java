package ru.guap.rssreader;

/**
 * Created by MeatBoy on 29.07.2015.
 */
public class NewsItem {

    private String mTitle;
    private String mLink;
    private String mDescriptionTitle;
    private String mDescription;
    private String mPubDate;
    private String mCreator;

    public NewsItem(String title, String link, String descriptionTitle, String description, String pubDate, String creator) {
        mTitle = title;
        mLink = link;
        mDescriptionTitle = descriptionTitle;
        mDescription = description;
        mPubDate = pubDate;
        mCreator = creator;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getDescriptionTitle() {
        return mDescriptionTitle;
    }
}
