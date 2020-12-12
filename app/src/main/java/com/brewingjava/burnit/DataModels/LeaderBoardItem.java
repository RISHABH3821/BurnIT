package com.brewingjava.burnit.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaderBoardItem {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sum")
    @Expose
    private String sum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

}