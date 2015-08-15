package ru.guap.rssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {

    private final static int NO_INTERNET = 1;
    private final static int NEWS_FEED = 2;

    private boolean hasData = false;

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
                transaction.replace(R.id.activity_container, new NewsFragment()).commit();
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

}
