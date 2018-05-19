package com.example.w10.pmsu.service;


import java.util.List;

import model.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.TAGS)
    Call<List<Tag>> getTags();

    @GET(ServiceUtils.TAGS_BY_POST)
    Call<List<Tag>> getTagsByPost();

}
