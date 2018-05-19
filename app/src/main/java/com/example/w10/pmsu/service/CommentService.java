package com.example.w10.pmsu.service;


import java.util.List;

import model.Comment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.COMMENTS)
    Call<List<Comment>> getComments();



    @POST(ServiceUtils.COMMENTS)
    Call<ResponseBody> addComment(@Body Comment comment);

}
