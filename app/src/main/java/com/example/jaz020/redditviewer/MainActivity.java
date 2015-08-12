package com.example.jaz020.redditviewer;

import android.app.ActionBar;
import android.app.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.net.HttpURLConnection;


public class MainActivity extends Activity {

    // The login API URL
    private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

    // The Reddit cookie string
    // This should be used by other methods after a successful login.
    private String redditCookie = "";

    private Context context;
    private Handler handler;
    private ActionBar actionBar;
    private Menu menu;
    public boolean isUserLoggedIn;
    SharedPreferences preferences;

    private AlertDialog loginDialog;
    private AlertDialog sortDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        handler = new Handler();
        actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        preferences = getSharedPreferences("SmartCharter", 0);
        //check for login
        redditCookie = preferences.getString("redditCookie", "");


        //Tools.replaceFragment(R.id.header_fragment_container, HeaderFragment.newInstance("home"), getFragmentManager(), true);

        Tools.replaceFragment(R.id.main_fragment_container, Home.newInstance("home"), getFragmentManager(), true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        if(!redditCookie.equals("")){
            menu.findItem(R.id.action_login).setTitle("Sign Out");
            isUserLoggedIn = true;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:

                Tools.replaceFragment(R.id.main_fragment_container, new Settings(), getFragmentManager(), true);
                return true;

            case R.id.action_login:

                if(isUserLoggedIn){
                    logout();
                }
                else{
                    showLoginDialog();
                }
                return true;

            case R.id.action_sort:

                showSortDialog();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    public void showLoginDialog(){

        //start login dialog

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        final EditText username_entry = (EditText) dialogView.findViewById(R.id.username_entry);
        final EditText password_entry = (EditText) dialogView.findViewById(R.id.password_entry);

        Button login_button = (Button) dialogView.findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        isUserLoggedIn = login(username_entry.getText().toString(), password_entry.getText().toString());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (isUserLoggedIn) {
                                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.action_login).setTitle("Log Out");
                                    preferences.edit().putString("redditCookie", redditCookie).commit();
                                    alertDialog.dismiss();
                                } else {
                                    username_entry.setError("Incorrect Username or Password");
                                    Log.d("Login", "Incorrect Username or Password");
                                }
                            }
                        });


                    }
                }).start();
            }
        });

    }

    public void showSortDialog(){

        //start Sort dialog

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sort_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Spinner sort_spinner = (Spinner) alertDialog.findViewById(R.id.sort_spinner);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.sortable, android.R.layout.simple_spinner_item);

        sort_spinner.setAdapter(dataAdapter);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       //do dialog logic


    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            Tools.replaceFragment(R.id.main_fragment_container, Home.newInstance(query), getFragmentManager(), true);
        }
    }



    // This method lets you log in to Reddit.
    // It fetches the cookie which can be used in subsequent calls
    // to the Reddit API.
    public boolean login(String username, String password){
        HttpURLConnection connection = Network.getConnection(REDDIT_LOGIN_URL);

        if(connection == null)
            return false;

        //Parameters that the API needs
        String data="user="+username+"&passwd="+password;

        if(!Network.writeToConnection(connection, data))
            return false;

        String cookie=connection.getHeaderField("set-cookie");

        if(cookie==null)
            return false;

        cookie=cookie.split(";")[0];
        if(cookie.startsWith("reddit_first")){
            // Login failed
            Log.d("Error", "Unable to login.");
            return false;
        }else if(cookie.startsWith("reddit_session")){
            // Login success
            Log.d("Success", cookie);
            redditCookie = cookie;
            return true;
        }
        return false;
    }

    public void logout(){

        redditCookie = "";
        isUserLoggedIn = false;
        menu.findItem(R.id.action_login).setTitle("Sign In");
        preferences.edit().remove("redditCookie").commit();
    }
}
