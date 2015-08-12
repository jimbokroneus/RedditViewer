package com.example.jaz020.redditviewer;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MediaViewer extends Fragment {

    ImageView imageView;

    String imageUrl;

    WebView webView;


    public MediaViewer() {
        // Required empty public constructor
    }

    public static MediaViewer newInstance(String url){
        MediaViewer mediaViewer = new MediaViewer();
        mediaViewer.imageUrl = url;
        return mediaViewer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) view.findViewById(R.id.image_view);

        webView = (WebView) view.findViewById(R.id.web_view);

        webView.loadUrl(imageUrl);
        Picasso.with(getActivity()).load(imageUrl).into(imageView);

    }
}
