package com.hackbulgaria.antoan.swipegallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;

/**
 * Created by Antoan on 22-Nov-14.
 */
public class ScreenSlidePageFragment extends Fragment {

    public static final String PICTURE = "picture";

    public static ScreenSlidePageFragment getInstance(String picture) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle arguments = new Bundle();
        arguments.putString(PICTURE, picture);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        File image = new File(getArguments().getString(PICTURE));

        if(image.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            ImageView iv = (ImageView) rootView.findViewById(R.id.image);
            iv.setImageBitmap(bitmap);
        }

        return rootView;
    }
}
