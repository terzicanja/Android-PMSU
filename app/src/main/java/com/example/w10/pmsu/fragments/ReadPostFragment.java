package com.example.w10.pmsu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w10.pmsu.R;
import com.example.w10.pmsu.service.PostService;
import com.example.w10.pmsu.service.ServiceUtils;
import com.example.w10.pmsu.service.TagService;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
    private PostService postService;
    private boolean liked;
    private boolean disliked;
    private TextView location;

    private SharedPreferences sharedPreferences;

    private RadioButton post_like;
    private RadioButton post_dislike;
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
        final Post post = new Gson().fromJson(jsonMyObject, Post.class);

        location = view.findViewById(R.id.location);
        TextView date = view.findViewById(R.id.date);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        TextView post_rating = view.findViewById(R.id.post_rating);
        tag = view.findViewById(R.id.tags);
        TextView vest = view.findViewById(R.id.vest);

        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String ulogovani = sharedPreferences.getString("User", "");

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());

        tagService = ServiceUtils.tagService;
        postService = ServiceUtils.postService;
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

        getAddress(post.getLatitude(), post.getLongitude());

        String rating = Integer.toString(post.getLikes() - post.getDislikes());

        date.setText(newDate);
        title.setText(post.getTitle());
        vest.setText(post.getDescription());
        post_rating.setText(rating);



        post_like = view.findViewById(R.id.post_like);
        post_dislike = view.findViewById(R.id.post_dislike);

        liked = false;
        post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ulogovani.equals(post.getAuthor().getUsername())){
                    post_like.setChecked(false);
                    Toast.makeText(getContext(),"ne mozes lajkovati svoju objavu",Toast.LENGTH_SHORT).show();
                }else{
                    if (liked == false){
                        post.setLikes(post.getLikes() + 1);
                        liked = true;

                        updatePost(post);
                    } else {
                        post.setLikes(post.getLikes() - 1);
                        liked = false;
                        post_like.setChecked(false);

                        updatePost(post);
                    }
                }
            }
        });

        disliked = false;
        post_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ulogovani.equals(post.getAuthor().getUsername())){
                    post_dislike.setChecked(false);
                    Toast.makeText(getContext(),"ne mozes lajkovati svoju objavu",Toast.LENGTH_SHORT).show();
                }else{
                    if (disliked == false){
                        post.setDislikes(post.getDislikes() + 1);
                        disliked = true;

                        updatePost(post);
                    } else {
                        post.setDislikes(post.getDislikes() - 1);
                        disliked = false;
                        post_like.setChecked(false);

                        updatePost(post);
                    }
                }
            }
        });



    }



    public void getAddress(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            if (latitude == 0 && longitude == 0){
                latitude = 0.000000;
                longitude = 0.000000;

//                addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                String city = addresses.get(0).getLocality();
//                String country = addresses.get(0).getCountryName();
//                String street = addresses.get(0).getAddressLine(0);
//            location_text.setText(city + "," + country);
//            location_text.setText(street);
                location.setText(latitude + " " + longitude);
            }else{
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                String street = addresses.get(0).getAddressLine(0);
//            location_text.setText(city + "," + country);
//            location_text.setText(street);
                location.setText(street);
            }
//            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            String city = addresses.get(0).getLocality();
//            String country = addresses.get(0).getCountryName();
//            String street = addresses.get(0).getAddressLine(0);
////            location_text.setText(city + "," + country);
////            location_text.setText(street);
//            location.setText(street);
//
//            System.out.println(city);
//            System.out.println(country);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void updatePost(Post post){
        Call<Post> call = postService.updatePost(post, post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

}
