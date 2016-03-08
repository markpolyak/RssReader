package ru.kupchinonews.rssreader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ru.kupchinonews.rssreader.NewsItem;

/**
 * Created by MeatBoy on 30.07.2015.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listView;
    private View view;
    private NewsActivity act;
    private NewsAdapter mAdapter;

    public NewsFragment(NewsActivity act) {
        this.act = act;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            listView = (ListView) view.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    boolean enable = false;
                    if (listView != null && listView.getChildCount() > 0) {
                        boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                        boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                        enable = firstItemVisible && topOfFirstItemVisible;
                    }
                    mSwipeRefreshLayout.setEnabled(enable);
                }
            });

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
        /*Intent i = new Intent(getActivity(), DetailsActivity.class);
        i.putExtra("pos", position);
        startActivity(i);*/
        act.switchFlag(position);
        mAdapter.notifyDataSetChanged();
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
                mAdapter = new NewsAdapter(getActivity(), items, act);
                listView.setAdapter(mAdapter);
            } else {
                Toast.makeText(getActivity(), "Возникла проблема при загрузке RSS ленты", Toast.LENGTH_LONG).show();
            }

            act.initFlags(items.size());

            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            getActivity().stopService(new Intent(getActivity(), RssService.class));
        }
    };

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (!act.hasInternet) {
            Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            startService();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }



}
