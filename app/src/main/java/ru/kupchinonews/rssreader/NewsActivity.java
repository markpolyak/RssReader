package ru.kupchinonews.rssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class NewsActivity extends BaseActivity {

    public final static int NO_INTERNET = 0;
    public final static int NEWS_FEED = 1;
    private boolean[] flags;

    private Fragment mNewsFeed;

    private boolean hasData = false;
    public boolean hasInternet;

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
                    mNewsFeed = new NewsFragment(this);
                transaction.replace(R.id.activity_container, mNewsFeed).commit();
                break;
        }
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                hasData = true;
                hasInternet = true;
                changeFragment(NEWS_FEED);
            } else {
                hasInternet = false;
                if (!hasData)
                    changeFragment(NO_INTERNET);
            }
        }
    };

    public boolean hasInt() {
        return hasInternet;
    }

    public void initFlags(int size) {
        flags = new boolean[size];
        flags[0] = true;
    }

    public void switchFlag(int pos) {
        flags[pos] = !flags[pos];
    }

    public boolean getFlag(int pos) {
        return flags[pos];
    }

}
