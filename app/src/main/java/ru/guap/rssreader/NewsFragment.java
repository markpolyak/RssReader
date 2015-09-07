package ru.guap.rssreader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by MeatBoy on 30.07.2015.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ProgressBar progressBar;
    private ListView listView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            listView = (ListView) view.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            startService();
        } else {
            if (Build.VERSION.SDK_INT <= 10) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        i.putExtra("pos", position);
        startActivity(i);
    }

    private void startService() {
        Intent intent = new Intent(getActivity(), RssService.class);
        intent.putExtra(RssService.RECEIVER, resultReceiver);
        getActivity().startService(intent);
    }

    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            ArrayList<NewsItem> items = (ArrayList<NewsItem>) resultData.getSerializable(RssService.ITEMS);

            if (items != null) {
                News.get().setNews(items);
                NewsAdapter adapter = new NewsAdapter(getActivity(), items);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Возникла проблема при загрузке RSS ленты", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            getActivity().stopService(new Intent(getActivity(), RssService.class));
        }
    };

}
