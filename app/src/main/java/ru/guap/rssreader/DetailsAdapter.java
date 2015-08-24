package ru.guap.rssreader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MeatBoy on 22.08.2015.
 */
public class DetailsAdapter extends ArrayAdapter<String> implements Serializable{

    public DetailsAdapter(Context context, ArrayList<String> news) {
        super(context, 0, news);
    }

    static class ViewHolder {
        TextView mTitle;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder mViewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_details, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.mTitle = (TextView) view.findViewById(R.id.textView);
            mViewHolder.mTitle.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf"));

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.mTitle.setText(getItem(position));

        return view;
    }

}
