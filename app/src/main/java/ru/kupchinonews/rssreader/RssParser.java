package ru.kupchinonews.rssreader;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.text.Html;
import android.util.Xml;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
    final String fDescription = "content:encoded";
    final String fPubDate = "pubDate";
    final String fCreator = "dc:creator";

    final String fImageStartTag = "src=\"";
    final String fDesriptionTagStart = "</div>\r\n\t<p>";
    final String fDescriptionTitleFinishTag = "\" alt";

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
        String pubDate = null;
        String creator = null;
        Drawable image = null;
        boolean lock = true;
        ArrayList<NewsItem> items = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (!lock) {
                switch (parser.getName()) {

                    case fTitle:
                        title = readTag(parser, fTitle);
                        break;

                    case fLink:
                        link = readTag(parser, fLink);
                        break;

                    case fDescription:
                        description = readTag(parser, fDescription);

                        int asd = description.indexOf(fImageStartTag);
                        if (asd != -1) {
                            String url = description.substring(description.indexOf(fImageStartTag) + fImageStartTag.length(),
                                    description.indexOf(fDescriptionTitleFinishTag));
                            image = LoadImageFromWebOperations(url);
                        } else {
                            image = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.booksas));
                        }

                        description = description.substring(description.indexOf(fDesriptionTagStart) + fDesriptionTagStart.length(), description.length());

                        //image = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.booksas));

                        break;

                    case fPubDate:
                        pubDate = readTag(parser, fPubDate);

                        Date d = null;
                        try {
                            d = fOldDateFormat.parse(pubDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        pubDate = fNewDateFormat.format(d);
                        break;

                    case fCreator:
                        creator = readTag(parser, fCreator);
                        break;
                }
            } else {
                if (parser.getName().equals("item"))
                    lock = false;
            }
            if (title != null && link != null && description != null && pubDate != null && creator != null) {
                items.add(new NewsItem(title, link, description, pubDate, creator, image));
                title = link = description = pubDate = creator = null;
                image = null;
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

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}