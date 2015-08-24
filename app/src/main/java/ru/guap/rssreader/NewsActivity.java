package ru.guap.rssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    public final static int NO_INTERNET = 0;
    public final static int NEWS_FEED = 1;
    public final static int DETAIL_VIEW = 2;

    private Fragment mNewsFeed;
    private Fragment mDetailView;

    private boolean hasData = false;
    private int position;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        setContentView(R.layout.activity_news);
        getWindow().setBackgroundDrawable(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mConnReceiver);
    }

    public void changeFragment(int code) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch(code) {

            case NO_INTERNET:
                transaction.replace(R.id.activity_container, new NoInternetFragment()).commit();
                break;

            case NEWS_FEED:
                if (mNewsFeed == null)
                    mNewsFeed = new NewsFragment();
                transaction.replace(R.id.activity_container, mNewsFeed).commit();
                break;

            case DETAIL_VIEW:
                if (mDetailView == null)
                    mDetailView = new DetailNewsFragment();

                ArrayList<String> items = new ArrayList<>();

                String desc = adapter.getItem(position).getDescription();
                desc = StringEscapeUtils.unescapeHtml4(desc);
                desc = desc.replaceAll("\r", "");
                desc = desc.replaceAll("\n", "");
                desc = desc.replaceAll("\t", "");
                String s = desc.substring(desc.indexOf("alt=") + 5, desc.indexOf("\" width"));
                items.add(s);
                s = desc.substring(desc.indexOf("</div>") + 6, desc.length());
                items.add(s);
                items.add(adapter.getItem(position).getCreator());
                desc = adapter.getItem(position).getPubDate();
                desc = desc.substring(5, desc.length() - 6);
                items.add(desc);

                Bundle args = new Bundle();
                args.putInt("news_pos", position);
                args.putSerializable("details_adapter", new DetailsAdapter(this, items));
                mDetailView.setArguments(args);
                transaction.replace(R.id.activity_container, mDetailView, "details_view").commit();
                break;
        }
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                hasData = true;
                changeFragment(NEWS_FEED);
            } else {
                if (!hasData)
                    changeFragment(NO_INTERNET);
            }
        }
    };

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAdapter(NewsAdapter adapter) {
        this.adapter = adapter;
    }

    public NewsAdapter getAdapter() {
        return adapter;
    }

    public void onBackPressed() {
        DetailNewsFragment myFragment = (DetailNewsFragment) getSupportFragmentManager().findFragmentByTag("details_view");
        if (myFragment != null && myFragment.isVisible()) {
            changeFragment(NEWS_FEED);
        } else {
            finish();
        }
    }

}
