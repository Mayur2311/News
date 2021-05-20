
package com.example.mainactivity.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsList {

    @SerializedName("pagination")
    @Expose
    public Pagination pagination;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;

}
