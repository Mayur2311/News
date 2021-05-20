package com.example.mainactivity.utils;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIinterface {

    @GET("list_news.json")
    Call<Newsist> getListByYear(@Query("sort_by") String year, @Query("page") String page);

}
