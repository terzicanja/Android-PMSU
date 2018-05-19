package com.example.w10.pmsu.service;

import java.util.List;

import model.Post;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.POSTS)
    Call<List<Post>> getPosts();

    @POST(ServiceUtils.POSTS)
    Call<Post> savePost(@Body Post post);
}
