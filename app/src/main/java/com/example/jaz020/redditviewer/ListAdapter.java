package com.example.jaz020.redditviewer;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;
import com.squareup.picasso.Picasso;

/**
 * Created by jaz020 on 8/10/2015.
 */
public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Post> posts;
    private Context context;
    private FragmentManager manager;

    public ListAdapter(Context context, FragmentManager manager, List<Post> posts) {
        mInflater = LayoutInflater.from(context);
        this.posts = posts;
        this.context = context;
        this.manager = manager;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.text_1 = (TextView)view.findViewById(R.id.text1);
            holder.text_2 = (TextView)view.findViewById(R.id.text2);
            holder.text_3 = (TextView)view.findViewById(R.id.text3);
            holder.imageView = (ImageView) view.findViewById(R.id.image_view);
            holder.score = (TextView) view.findViewById(R.id.score_text_view);


            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Post curr = posts.get(position);

        holder.url = curr.getUrl();

        setImageClick(holder);

        holder.text_1.setText(curr.getTitle());
        holder.text_2.setText(curr.getDetails());
        holder.score.setText(curr.getScore());


        String image_url = curr.getThumbnail();

        if(image_url.contains("http")) {
            Picasso.with(context).load(curr.getThumbnail()).into(holder.imageView);
        }
        else{
            Picasso.with(context).load(R.drawable.holder).into(holder.imageView);
        }

        return view;
    }

    private class ViewHolder {
        public TextView text_1, text_2, text_3, score;
        public ImageView imageView;
        public String url;
    }

    private void setImageClick(final ViewHolder holder){

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tools.replaceFragment(R.id.main_fragment_container, MediaViewer.newInstance(holder.url), manager, true);

            }
        });

    }
}
