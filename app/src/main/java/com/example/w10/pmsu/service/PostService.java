package com.example.w10.pmsu.service;

import java.util.List;

import model.Post;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.POSTS)
    Call<List<Post>> getPosts();

    @GET("posts/sort/date/asc")
    Call<List<Post>> getPostsByDateAsc();

    @POST("posts/create")
    Call<Post> savePost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Body Post post, @Path("id") int id);

    @DELETE("posts/{id}")
    Call<Post> deletePost(@Path("id") int id);
}
