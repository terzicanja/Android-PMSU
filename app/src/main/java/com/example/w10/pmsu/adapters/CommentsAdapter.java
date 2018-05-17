package com.example.w10.pmsu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.w10.pmsu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Comment;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    public CommentsAdapter(Context context, List<Comment> comments){
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, viewGroup, false);
        }

        TextView korisnicko = view.findViewById(R.id.korisnicko);
        TextView komentar_datum = view.findViewById(R.id.komentar_datum);
//        TextView lajk = view.findViewById(R.id.lajk);

        String d = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(comment.getDate());
        String likes = Integer.toString(comment.getLikes());

        korisnicko.setText(comment.getAuthor().getUsername());
        komentar_datum.setText(d);
//        lajk.setText(likes);

        return view;

    }

}
