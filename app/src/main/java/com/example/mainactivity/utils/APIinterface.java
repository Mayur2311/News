package com.example.mainactivity.utils;

import com.example.mainactivity.Model;

import retrofit2.Call;
import retrofit2.http.GET;


public interface APIinterface {

    @GET("api/users?page=1")
    Call<Model> getProfileData();
}
