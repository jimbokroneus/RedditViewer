package com.example.jaz020.redditviewer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {

    String subreddit;
    List<Post> posts;
    PostsHolder postsHolder;

    Handler handler;

    ListAdapter adapter;

    private ListView list_view;
    private TextView header_text_view;
    private int preLast;

    public Home(){
        handler=new Handler();
        posts=new ArrayList<Post>();
    }

    public static Fragment newInstance(String subreddit){
        Home home = new Home();
        home.subreddit = subreddit;
        home.postsHolder = new PostsHolder(home.subreddit);
        return home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void initializeList() {

        //if posts is empty, fetch data + create adatper in background
        if (posts.size() == 0) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    posts.addAll(postsHolder.fetchPosts());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setAdapter(posts);
                        }
                    });
                }
            }).start();

        }
        else{
            setAdapter(posts);
        }

    }

    private void setAdapter(List<Post> list){

        adapter = new ListAdapter(getActivity(), getFragmentManager(), list);

        list_view.setAdapter(adapter);


    }

    private void setOnScroll(){
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
                        Log.d("Last", "Last");
                        preLast = lastItem;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                posts.addAll(postsHolder.fetchMorePosts());

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).start();

                    }
                }
            }
        });
    }

    private void setOnClick(){
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }
    private void setHeader() {
        String title = subreddit;
        if (title.equals("home")){
            title = "Front Page";
        }

        header_text_view.setText(title);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        list_view = (ListView) view.findViewById(R.id.main_list);
        header_text_view = (TextView) view.findViewById(R.id.subreddit_title);

        initializeList();

        setHeader();

        setOnScroll();
        //populate list




    }

}
