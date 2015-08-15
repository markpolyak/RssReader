package ru.guap.rssreader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MeatBoy on 29.07.2015.
 */
public class NewsAdapter extends ArrayAdapter<NewsItem> {

    public NewsAdapter(Context context, ArrayList<NewsItem> news) {
        super(context, 0, news);
    }

    static class ViewHolderItem {
        TextView mTextView;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolderItem mViewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);

            mViewHolder = new ViewHolderItem();
            mViewHolder.mTextView = (TextView) view.findViewById(R.id.item_text_view);
            mViewHolder.mTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf"));

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolderItem) view.getTag();
        }

        mViewHolder.mTextView.setText(getItem(position).getTitle());

        return view;
    }
}
