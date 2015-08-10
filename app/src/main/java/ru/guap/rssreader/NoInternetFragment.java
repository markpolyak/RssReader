package ru.guap.rssreader;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by MeatBoy on 30.07.2015.
 */
public class NoInternetFragment extends Fragment {

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_internet, container, false);
        textView = (TextView) rootView.findViewById(R.id.no_internet_text_view);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        textView.setTypeface(custom_font);
        return rootView;
    }

}
