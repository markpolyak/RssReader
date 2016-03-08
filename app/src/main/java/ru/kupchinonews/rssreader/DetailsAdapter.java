package ru.kupchinonews.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MeatBoy on 22.08.2015.
 */
public class DetailsAdapter extends ArrayAdapter<String> implements Serializable{

    public DetailsAdapter(Context context, ArrayList<String> news) {
        super(context, 0, news);
        this.news = news;
    }

    private ArrayList<String> news;

    static class ViewHolder {
        TextView mTitle;
        TextView mDescription;
        TextView mInfo;
        Button mLink;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder mViewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_news, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.mTitle = (TextView) view.findViewById(R.id.title);
            mViewHolder.mTitle.setTypeface(BaseActivity.getDefaultFont());
            mViewHolder.mDescription = (TextView) view.findViewById(R.id.description);
            mViewHolder.mDescription.setTypeface(BaseActivity.getDefaultFont());
            mViewHolder.mInfo = (TextView) view.findViewById(R.id.date_author);
            mViewHolder.mInfo.setTypeface(BaseActivity.getDefaultFont());
            mViewHolder.mLink = (Button) view.findViewById(R.id.button);
            mViewHolder.mLink.setTypeface(BaseActivity.getDefaultFont());

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.mTitle.setText(news.get(0));
        mViewHolder.mDescription.setText(news.get(1));
        mViewHolder.mInfo.setText(news.get(2));

        return view;
    }

}
