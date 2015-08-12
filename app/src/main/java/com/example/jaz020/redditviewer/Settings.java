package com.example.jaz020.redditviewer;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    private TextView username_text_view;
    private TextView details_text_view;

    private Button login_button;

    MainActivity m = (MainActivity) getActivity();



    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields(view);
    }

    private void initializeFields(View view){
        username_text_view = (TextView) view.findViewById(R.id.username);
        details_text_view = (TextView) view.findViewById(R.id.details);
        login_button = (Button) view.findViewById(R.id.login_button);
        m = (MainActivity) getActivity();

        if(m.isUserLoggedIn){
            login_button.setText("Sign Out");
        }
        else{
            login_button.setText("Sign In");
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(m.isUserLoggedIn){
                    m.logout();
                }
                else{
                    m.showLoginDialog();
                }

            }
        });
    }

}
