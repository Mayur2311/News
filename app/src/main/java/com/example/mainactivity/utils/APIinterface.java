package com.example.mainactivity.utils;

import com.example.mainactivity.models.Datum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface APIinterface {

    @GET("list_news.json")
    Call<Datum> getListByYear(@Query("access_key") String access_key);
}
