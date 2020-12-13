package com.brewingjava.burnit.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayRep {

@SerializedName("sum")
@Expose
private String sum;
@SerializedName("timestamp")
@Expose
private String timestamp;

public String getSum() {
return sum;
}

public void setSum(String sum) {
this.sum = sum;
}

public String getTimestamp() {
return timestamp;
}

public void setTimestamp(String timestamp) {
this.timestamp = timestamp;
}

}