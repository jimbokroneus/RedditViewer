package com.example.jaz020.redditviewer;


import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeaderFragment extends Fragment {

    private String subreddit;

    private Button search_button;
    private EditText search_entry;


    public HeaderFragment() {
        // Required empty public constructor
    }

    public static HeaderFragment newInstance(String subreddit){
        HeaderFragment headerFragment = new HeaderFragment();
        headerFragment.subreddit = subreddit;
        return headerFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        search_button = (Button) view.findViewById(R.id.search_button);
        search_entry = (EditText) view.findViewById(R.id.search_entry);


        setListeners();

    }

    private void setListeners(){
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = search_entry.getText().toString();

                Tools.replaceFragment(R.id.main_fragment_container, Home.newInstance(temp), getFragmentManager(), true);

            }
        });
    }

}
