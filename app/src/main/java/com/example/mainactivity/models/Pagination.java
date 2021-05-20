
package com.example.mainactivity.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Pagination {

    @SerializedName("limit")
    @Expose
    public Integer limit;
    @SerializedName("offset")
    @Expose
    public Integer offset;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("total")
    @Expose
    public List<Datum> news = null;
}
