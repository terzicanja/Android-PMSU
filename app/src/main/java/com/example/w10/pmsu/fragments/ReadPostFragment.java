package com.example.w10.pmsu.fragments;

import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.w10.pmsu.R;
import com.example.w10.pmsu.service.ServiceUtils;
import com.example.w10.pmsu.service.TagService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Post;
import model.Tag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostFragment extends Fragment {

    View view;

    private TagService tagService;
    private List<Tag> tags;
    private LinearLayout linearLayout;
    private LinearLayout newLinearLayout;

    private TextView tag;

    public ReadPostFragment(){

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .addSharedElement(R.id.image_view, "img_transition")
//                .replace(R.id.container, details)
//                .addToBackStack(null)
//                .commit();

//        view = inflater.inflate(R.layout.read_post_fragment,container,false);
//        return view;

        return inflater.inflate(R.layout.read_post_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
        Post post = new Gson().fromJson(jsonMyObject, Post.class);

        TextView location = view.findViewById(R.id.location);
        TextView date = view.findViewById(R.id.date);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        TextView post_rating = view.findViewById(R.id.post_rating);
        tag = view.findViewById(R.id.tags);
        TextView vest = view.findViewById(R.id.vest);

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());

        tagService = ServiceUtils.tagService;
        Call<List<Tag>> call = tagService.getTagsByPost(post.getId());
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                tags = response.body();
                newLinearLayout = new LinearLayout(getContext());
                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                String tt = "";
                for(Tag t:tags) {
                    tt += ("#" + t.getName() + "  ");
                    tag.setText(tt);
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });



        date.setText(newDate);
        title.setText(post.getTitle());
        vest.setText(post.getDescription());
    }

}
