package ru.guap.rssreader;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by MeatBoy on 30.07.2015.
 */
public class RssParser {

    final String ns = null;
    final String fTitle = "title";
    final String fLink = "link";
    final String fDescription = "description";
    final String fPubDate = "pubDate";
    final String fCreator = "dc:creator";

    public ArrayList<NewsItem> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private ArrayList<NewsItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "rss");
        String title = null;
        String link = null;
        String description = null;
        String pubDate = null;
        String creator = null;
        boolean lock = true;
        ArrayList<NewsItem> items = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (!lock) {
                switch (parser.getName()) {
                    case "title":
                        title = readTag(parser, fTitle);
                        break;
                    case "link":
                        link = readTag(parser, fLink);
                        break;
                    case "description":
                        description = readTag(parser, fDescription);
                        break;
                    case "pubDate":
                        pubDate = readTag(parser, fPubDate);
                        break;
                    case "dc:creator":
                        creator = readTag(parser, fCreator);
                        break;
                }
            } else {
                String name = parser.getName();
                if (name.equals("item"))
                    lock = false;
            }
            if (title != null && link != null && description != null && pubDate != null && creator != null) {
                items.add(new NewsItem(title, link, description, pubDate, creator));
                title = link = description = pubDate = creator = null;
            }
        }
        return items;
    }

    private String readTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}