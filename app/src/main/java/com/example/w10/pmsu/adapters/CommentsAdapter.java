package com.example.w10.pmsu.adapters;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w10.pmsu.LoginActivity;
import com.example.w10.pmsu.R;
import com.example.w10.pmsu.service.CommentService;
import com.example.w10.pmsu.service.ServiceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Comment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    private SharedPreferences sharedPreferences;

    private RadioButton btn_like;
    private RadioButton btn_dislike;
    private String ulogovani;

    private boolean liked;
    private boolean disliked;

    private CommentService commentService;

    public CommentsAdapter(Context context, List<Comment> comments){
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        final Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, viewGroup, false);
        }

        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        ulogovani = sharedPreferences.getString("User", "");
        Button delete_comment = view.findViewById(R.id.delete_comment);

//        Toast.makeText(getContext(), "user" + comment.getAuthor().getName(),Toast.LENGTH_SHORT).show();

        if (!ulogovani.equals(comment.getAuthor().getUsername())){
            delete_comment.setVisibility(View.INVISIBLE);
        }

        TextView korisnicko = view.findViewById(R.id.korisnicko);
        TextView komentar_sadrzaj = view.findViewById(R.id.komentar_sadrzaj);
        TextView komentar_datum = view.findViewById(R.id.komentar_datum);
        TextView lokacija = view.findViewById(R.id.lokacija);
        TextView comment_rating = view.findViewById(R.id.comment_rating);

        String d = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(comment.getDate());
        String likes = Integer.toString(comment.getLikes() - comment.getDislikes());

        korisnicko.setText(comment.getAuthor().getUsername());
        komentar_sadrzaj.setText(comment.getDescription());
        komentar_datum.setText(d);
//        lokacija.setText();
        comment_rating.setText(likes);

        commentService = ServiceUtils.commentService;

//        Button delete_comment = view.findViewById(R.id.delete_comment);
        delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Comment> call = commentService.deleteComment(comment.getId());
                call.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        Toast.makeText(getContext(),"Comment is deleted",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {

                    }
                });
            }
        });


        btn_like = view.findViewById(R.id.btn_like);
        btn_dislike = view.findViewById(R.id.btn_dislike);

        liked = false;
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ulogovani.equals(comment.getAuthor().getUsername())){
                    btn_like.setChecked(false);
                    Toast.makeText(getContext(),"ne mozes lajkovati svoju objavu",Toast.LENGTH_SHORT).show();
                }else{
                    if (liked == false){
                        comment.setLikes(comment.getLikes() + 1);
                        liked = true;

                        updateComment(comment);
                    } else {
                        comment.setLikes(comment.getLikes() - 1);
                        liked = false;
                        btn_like.setChecked(false);

                        updateComment(comment);
                    }
                }
            }
        });


        disliked = false;
        btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ulogovani.equals(comment.getAuthor().getUsername())){
                    btn_dislike.setChecked(false);
                    Toast.makeText(getContext(),"ne mozes lajkovati svoju objavu",Toast.LENGTH_SHORT).show();
                }else{
                    if (disliked == false){
                        comment.setDislikes(comment.getDislikes() + 1);
                        disliked = true;

                        updateComment(comment);
                    } else {
                        comment.setDislikes(comment.getDislikes() - 1);
                        disliked = false;
                        btn_dislike.setChecked(false);

                        updateComment(comment);
                    }
                }
            }
        });


        return view;

    }


    public void updateComment(Comment comment){
        Call<Comment> call = commentService.updateComment(comment, comment.getId());
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }

}
