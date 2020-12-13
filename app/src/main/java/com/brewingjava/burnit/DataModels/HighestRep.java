package com.brewingjava.burnit.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HighestRep {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}