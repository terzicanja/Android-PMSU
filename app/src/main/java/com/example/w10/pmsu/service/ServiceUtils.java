package com.example.w10.pmsu.service;


import com.example.w10.pmsu.util.DateSerialization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {

    public static final String SERVICE_API_PATH = "http://192.168.0.12:8080/api/";
    //147.91.167.145
    public static final String POSTS = "posts";
    public static final String COMMENTS = "comments";
    public static final String TAGS = "tags";
    public static final String TAGS_BY_POST = "tags";
    public static final String LOGIN = "users/{username}";

    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, DateSerialization.getUnixEpochDateTypeAdapter())
            .create();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(test())
            .build();


    public static PostService postService = retrofit.create(PostService.class);
    public static CommentService commentService = retrofit.create(CommentService.class);
    public static UserService userService = retrofit.create(UserService.class);
    public static TagService tagService = retrofit.create(TagService.class);


}
