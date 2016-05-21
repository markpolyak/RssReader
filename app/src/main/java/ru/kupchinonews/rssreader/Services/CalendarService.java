package ru.kupchinonews.rssreader.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CalendarService extends IntentService {

    private static final String CALENDAR_LINK = "http://kupchinonews.ru/?plugin=all-in-one-event-calendar&controller=ai1ec_exporter_controller&action=export_events&no_html=true";
    public static final String CALENDAR = "calendar";
    public static final String RECEIVER = "receiver1";

    public CalendarService() {
        super("CalendarService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = null;
        try {
           calendar = builder.build(getInputStream(CALENDAR_LINK));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(CALENDAR, calendar);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        receiver.send(0, bundle);
    }

    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}