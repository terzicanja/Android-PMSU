package com.example.w10.pmsu.service;

import java.util.List;

import model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.LOGIN)
    Call<User> getUserByUsername(@Path("username") String username);

}
