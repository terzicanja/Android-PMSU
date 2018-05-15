package com.example.w10.pmsu.service;


import java.util.List;

import model.Comment;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.COMMENTS)
    Call<List<Comment>> getComments();

}
