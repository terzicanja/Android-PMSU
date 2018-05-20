package com.example.w10.pmsu.service;


import java.util.List;

import model.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.TAGS)
    Call<List<Tag>> getTags();

    @GET("posts/tags/{id}")
    Call<List<Tag>> getTagsByPost(@Path("id") Integer id);

}
