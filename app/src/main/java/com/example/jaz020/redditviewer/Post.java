package com.example.jaz020.redditviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

/**
 * This is a class that holds the data of the JSON objects
 * returned by the Reddit API.
 *
 * @author James Ziglinski
 */
public class Post {

    @SerializedName("subreddit")
    private String subreddit;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("score")
    private int score;

    @SerializedName("num_comments")
    private int numComments;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("url")
    private String url;

    @SerializedName("domain")
    private String domain;

    @SerializedName("subreddit_id")
    private String id;

    @SerializedName("thumbnail")
    private String thumbnail;

    String getDetails(){
        String details=author
                +" posted this and got "
                +score
                +" points";
        return details;
    }

    String getTitle(){
        return title;
    }

    String getScore(){
        return Integer.toString(score);
    }

    String getUrl(){
        return url;
    }

    String getThumbnail(){
        return thumbnail;
    }
}
