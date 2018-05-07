package com.example.w10.pmsu.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.w10.pmsu.R;
import com.example.w10.pmsu.adapters.CommentsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import model.Comment;
import model.User;

public class CommentsFragment extends Fragment {
    View view;

    Comment c1 = new Comment();
    User u1 = new User();
    Comment c2 = new Comment();
    User u2 = new User();
    Comment c3 = new Comment();
    User u3 = new User();
    ArrayList<Comment> comments = new ArrayList<>();
    private CommentsAdapter commentsAdapter;
    private SharedPreferences sharedPreferences;

    public CommentsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.comments_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        c1.setTitle("ajdee");
        u1.setUsername("pera");
        c1.setAuthor(u1);
        c1.setDate(new Date(2018-1900, 4, 23, 5, 32));
        c1.setLikes(33);

        c2.setTitle("nestoo");
        u2.setUsername("jovica");
        c2.setAuthor(u2);
        c2.setDate(new Date(2017-1900, 5, 12, 3, 11));
        c2.setLikes(11);

        c3.setTitle("omgg");
        u3.setUsername("neko");
        c3.setAuthor(u3);
        c3.setDate(new Date(2014-1900, 8, 30, 12, 15));
        c3.setLikes(78);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        commentsAdapter = new CommentsAdapter(getContext(), comments);
        ListView listView = view.findViewById(R.id.comments_list);
        listView.setAdapter(commentsAdapter);

//        consultPreferences();
//        commentsAdapter.notifyDataSetChanged();
    }

//    private void update(){
//        consultPreferences();
//        commentsAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onResume(){
        super.onResume();
        consultPreferences();
    }

    private void consultPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortComments = sharedPreferences.getString("pref_sortComments", "");

        if (sortComments.equals("oldest")){
            commentsByDate();
        }

        if (sortComments.equals("newest")){
            commentsByDateOld();
        }

        if (sortComments.equals("rating")){
            commentsByRating();
        }

        commentsAdapter.notifyDataSetChanged();
    }

    private void commentsByDate(){
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment com, Comment t1) {
                return com.getDate().compareTo(t1.getDate());
            }
        });
//        commentsAdapter.notifyDataSetChanged();
    }

    private void commentsByDateOld(){
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment com, Comment t1) {
                return t1.getDate().compareTo(com.getDate());
            }
        });
//        commentsAdapter.notifyDataSetChanged();
    }

    public void commentsByRating(){
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment com, Comment t1) {
                return Integer.valueOf(t1.getLikes()).compareTo(com.getLikes());
            }
        });
//        commentsAdapter.notifyDataSetChanged();
    }

}
