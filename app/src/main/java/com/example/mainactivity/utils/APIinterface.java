package com.example.mainactivity.utils;

import com.example.mainactivity.models.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface APIinterface {


    @GET("api/users?page=")
    Call<Model> getProfileData(@Query("page") String page);
}
