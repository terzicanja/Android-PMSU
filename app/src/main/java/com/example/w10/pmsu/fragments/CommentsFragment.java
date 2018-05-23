package com.example.w10.pmsu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.w10.pmsu.R;
import com.example.w10.pmsu.adapters.CommentsAdapter;
import com.example.w10.pmsu.service.CommentService;
import com.example.w10.pmsu.service.ServiceUtils;
import com.example.w10.pmsu.service.UserService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import model.Comment;
import model.Post;
import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {
    View view;

    Comment c1 = new Comment();
    User u1 = new User();
    Comment c2 = new Comment();
    User u2 = new User();
    Comment c3 = new Comment();
    User u3 = new User();
    List<Comment> comments = new ArrayList<>();
    private CommentsAdapter commentsAdapter;
    private SharedPreferences sharedPreferences;
    private CommentService commentService;
    private UserService userService;
    private Post post;
    private static User user;

    private RadioButton btn_like;
    private RadioButton btn_dislike;

    private EditText write_comment_title;
    private EditText write_comment;
    private ListView listView;

    public CommentsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

//        comments.add(c1);
//        comments.add(c2);
//        comments.add(c3);


        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }

        post = new Gson().fromJson(jsonMyObject, Post.class);
        listView = view.findViewById(R.id.comments_list);

        commentService = ServiceUtils.commentService;
        Call<List<Comment>> call = commentService.getCommentsByPost(post.getId());

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                commentsAdapter = new CommentsAdapter(getContext(),comments);
                listView.setAdapter(commentsAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });






//        commentsAdapter = new CommentsAdapter(getContext(), comments);
//        final ListView listView = view.findViewById(R.id.comments_list);
//        commentService = ServiceUtils.commentService;
//        listView.setAdapter(commentsAdapter);
//
//        Call call = commentService.getComments();
//
//        call.enqueue(new Callback<List<Comment>>() {
//            @Override
//            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//
//                comments = response.body();
//                commentsAdapter = new CommentsAdapter(getContext(), comments);
//                listView.setAdapter(commentsAdapter);
//
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });

        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String ulogovani = sharedPreferences.getString("User", "");

        userService = ServiceUtils.userService;

        Call<User> callUser = userService.getUserByUsername(ulogovani);

        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                Toast.makeText(getContext(), "Pronadjen je user sa imenom " + user.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        Button post_comment = view.findViewById(R.id.comment_post_btn);
        write_comment_title = view.findViewById(R.id.write_comment_title);
        write_comment = view.findViewById(R.id.write_comment);


        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();

                String comment_title = write_comment_title.getText().toString();
                String txtComment = write_comment.getText().toString();

                comment.setTitle(comment_title);
                comment.setDescription(txtComment);
                Date date = Calendar.getInstance().getTime();
                comment.setDate(date);
                comment.setAuthor(user);
                comment.setPost(post);
                comment.setLikes(0);
                comment.setDislikes(0);

                Toast.makeText(getContext(), txtComment,Toast.LENGTH_SHORT).show();

                Call<ResponseBody> call = commentService.addComment(comment);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getContext(),"Added comment",Toast.LENGTH_SHORT).show();

                        commentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });


//        Button delete_comment = view.findViewById(R.id.delete_comment);
//        delete_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Call<Comment> call = commentService.deleteComment(comment.getId());
//                call.enqueue(new Callback<Comment>() {
//                    @Override
//                    public void onResponse(Call<Comment> call, Response<Comment> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Comment> call, Throwable t) {
//
//                    }
//                });
//            }
//        });



//        btn_like = view.findViewById(R.id.btn_like);
//        btn_dislike = view.findViewById(R.id.btn_dislike);
//
//        btn_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if(ulogovani.equals())
//            }
//        });


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
//        consultPreferences();

//        RadioGroup comments_radio_group = (RadioGroup)view.findViewById(R.id.comments_radio_group);
//
//        if(comments_radio_group.getCheckedRadioButtonId()==-1)
//        {
//            Toast.makeText(getContext(), "nesto se desilo",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
////            // get selected radio button from radioGroup
////            int selectedId = gender.getCheckedRadioButtonId();
////            // find the radiobutton by returned id
////            selectedRadioButton = (RadioButton)findViewById(selectedId);
////            Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
//        }

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

//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.btn_like:
//                if (checked)
//                    Toast.makeText(getContext(), "nesto se desilo",Toast.LENGTH_SHORT).show();
//                    // Pirates are the best
//                    break;
//            case R.id.btn_dislike:
//                if (checked)
//                    Toast.makeText(getContext(), "nestoooooooooo",Toast.LENGTH_SHORT).show();
//                    // Ninjas rule
//                    break;
//        }
//    }

    public void createComment(){
        Toast.makeText(getContext(), "pravljenje komentara",Toast.LENGTH_SHORT).show();
    }


//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        RadioButton rb = (RadioButton) view.findViewById(view.getId());
//        boolean checked = ((RadioButton) view).isChecked();
//
//
//        if (checked)
//
//            // Check which radio button was clicked
//            switch(view.getId()) {
//                case R.id.btn_like:
//                    if (checked)
//                        Toast.makeText(getContext(), "nesto se desilo",Toast.LENGTH_SHORT).show();
//                    // Pirates are the best
//                    break;
//                case R.id.btn_dislike:
//                    if (checked)
//                        Toast.makeText(getContext(), "nestoooooooooo",Toast.LENGTH_SHORT).show();
//                    // Ninjas rule
//                    break;
//            }
//    }

}
