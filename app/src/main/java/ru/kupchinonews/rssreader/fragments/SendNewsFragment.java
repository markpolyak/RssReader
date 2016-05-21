package ru.kupchinonews.rssreader.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ru.kupchinonews.rssreader.R;
import ru.kupchinonews.rssreader.activity.BaseActivity;
import ru.kupchinonews.rssreader.activity.MainActivity;

public class SendNewsFragment extends Fragment implements View.OnClickListener {
    private View view;

    private TextView mNewsDescriptionTitle;
    private EditText mNewsDescription;
    private ImageView mNewsDescriptionIcon;
    private TextView mUserPhotoTitle;
    private ImageView mAddPhotoIcon;
    private ImageButton mAddPhotoButton;
    private ImageView mUserPhoto;
    private Button mSendNews;
    private ImageView mGeoImageView;
    private TextView mGeoTextView;

    private MainActivity mAct;

    public SendNewsFragment() {
    }

    @Override
    public void onAttach(Activity myActivity) {
        super.onAttach(myActivity);
        mAct = (MainActivity) myActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_send_news, container, false);

            mNewsDescription = (EditText) view.findViewById(R.id.edit_text);
            mNewsDescription.setTypeface(BaseActivity.getDefaultFont());
            mNewsDescriptionTitle = (TextView) view.findViewById(R.id.edit_text_title);
            mNewsDescriptionTitle.setTypeface(BaseActivity.getDefaultFont());
            mNewsDescriptionIcon = (ImageView) view.findViewById(R.id.edit_text_icon);
            mNewsDescriptionIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_700), PorterDuff.Mode.SRC_ATOP);
            mUserPhotoTitle = (TextView) view.findViewById(R.id.image_view_title);
            mUserPhotoTitle.setTypeface(BaseActivity.getDefaultFont());
            mAddPhotoIcon = (ImageView) view.findViewById(R.id.image_view_icon);
            mAddPhotoIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_700), PorterDuff.Mode.SRC_ATOP);
            mAddPhotoButton = (ImageButton) view.findViewById(R.id.image_button);
            mAddPhotoButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            mAddPhotoButton.setOnClickListener(this);
            mUserPhoto = (ImageView) view.findViewById(R.id.image_view);
            mSendNews = (Button) view.findViewById(R.id.send_news_button);
            mSendNews.setOnClickListener(this);
            mSendNews.setBackgroundResource(0);
            mGeoTextView = (TextView) view.findViewById(R.id.geo_text_view);
            mGeoTextView.setTypeface(BaseActivity.getDefaultFont());
            mGeoImageView = (ImageView) view.findViewById(R.id.image_view_geo);

        } else {
            if (Build.VERSION.SDK_INT <= 10) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.send_news_button:
                boolean lock = false;
                if(mNewsDescription.getText().length() == 0) {
                    lock = true;
                    mNewsDescriptionIcon.setVisibility(View.VISIBLE);
                } else {
                    mNewsDescriptionIcon.setVisibility(View.GONE);
                }
                if(mUserPhoto.getDrawable() == null) {
                    lock = true;
                    mAddPhotoIcon.setVisibility(View.VISIBLE);
                } else {
                    mAddPhotoIcon.setVisibility(View.GONE);
                }
                if(!lock)
                    sendNews();
                break;
            case R.id.image_button:
                takePhoto();
                break;
        }
    }

    public void setImage(Bitmap bmp) {
        mUserPhoto.setImageBitmap(bmp);
        mGeoTextView.setVisibility(View.VISIBLE);
        //mGeoTextView.setText("Test");
        mGeoImageView.setVisibility(View.VISIBLE);
    }

    private void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        getActivity().startActivityFromFragment(SendNewsFragment.this, intent, 100);
    }

    private void sendNews() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    setImage(bitmap);
                    mAddPhotoIcon.setVisibility(View.GONE);
                    mGeoTextView.setText(String.valueOf(mAct.getLat() + ", " + mAct.getLng()));
                }
        }
    }
}
