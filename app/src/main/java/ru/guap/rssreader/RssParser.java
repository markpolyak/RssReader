package ru.guap.rssreader;

import android.util.Xml;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    final DateFormat fOldDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss", Locale.ENGLISH);
    final DateFormat fNewDateFormat = new SimpleDateFormat("kk:mm  dd.MM.yyyy", Locale.ENGLISH);

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
        String descriptionTitle = null;
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

                        description = StringEscapeUtils.unescapeHtml4(description);
                        description = description.replaceAll("\r", "");
                        description = description.replaceAll("\n", "");
                        description = description.replaceAll("\t", "");
                        descriptionTitle = description.substring(description.indexOf("alt=") + 5, description.indexOf("\" width"));
                        description = description.substring(description.indexOf("</div>") + 6, description.length());
                        break;

                    case "pubDate":
                        pubDate = readTag(parser, fPubDate);

                        Date d = null;
                        try {
                            d = fOldDateFormat.parse(pubDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        pubDate = fNewDateFormat.format(d);
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
                items.add(new NewsItem(title, link, descriptionTitle, description, pubDate, creator));
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