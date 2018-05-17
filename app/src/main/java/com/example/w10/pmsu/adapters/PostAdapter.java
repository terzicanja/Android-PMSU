package com.example.w10.pmsu.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w10.pmsu.PostsActivity;
import com.example.w10.pmsu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Post;

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context context, ArrayList<Post> posts){
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Post post = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.post_item, viewGroup, false);

        }

        TextView date_view = view.findViewById(R.id.date_view);
        TextView title_view = view.findViewById(R.id.title_view);
        TextView likes_view = view.findViewById(R.id.likes_view);
        ImageView image_view = view.findViewById(R.id.image_view);
        TextView description = view.findViewById(R.id.description);

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());
        String likes = Integer.toString(post.getLikes());
//        String image = post.getPhoto().toString();

        date_view.setText(newDate);
        title_view.setText(post.getTitle());
        likes_view.setText(likes);
//        image_view.setImageBitmap(post.getPhoto());
//        image_view.setScaleType(ImageView.ScaleType.FIT_XY);
        description.setText(post.getDescription());

        return view;


    }

}
