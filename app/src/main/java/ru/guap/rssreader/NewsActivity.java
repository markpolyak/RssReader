package ru.guap.rssreader;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class NewsActivity extends FragmentActivity {

    private final static int NO_INTERNET = 1;
    private final static int NEWS_FEED = 2;

    private boolean hasData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        setContentView(R.layout.activity_main);
    }

    public void changeFragment (int code) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;

        switch (code) {

            case NO_INTERNET:
                fragment = fm.findFragmentById(R.id.text);
                if (fragment == null) {
                    fragment = new NoInternetFragment();
                    fm.beginTransaction()
                            .replace(R.id.activity_container, fragment)
                            .commit();
                }
                break;

            case NEWS_FEED:
                fragment = fm.findFragmentById(R.id.main);
                if (fragment == null) {
                    fragment = new NewsFragment();
                    fm.beginTransaction()
                            .replace(R.id.activity_container, fragment)
                            .commit();
                }
                break;
        }
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {
                hasData = true;
                changeFragment(NEWS_FEED);
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            } else {
                if (!hasData)
                    changeFragment(NO_INTERNET);
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    };

}
