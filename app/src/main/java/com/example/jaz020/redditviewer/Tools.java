package com.example.jaz020.redditviewer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by jaz020 on 6/25/2015.
 */
public class Tools {

    public static void replaceFragment(int container_id, Fragment fragment, FragmentManager fManager,
                                       boolean addToBackStack) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
       // fTransaction.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
        fTransaction.replace(container_id, fragment);

        if (addToBackStack) fTransaction.addToBackStack(null);

        fTransaction.commit();
    }
}