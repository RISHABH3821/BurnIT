package com.brewingjava.burnit.Util;

import com.brewingjava.burnit.DataModels.ChartDataModel;
import com.brewingjava.burnit.DataModels.HealthData;
import com.brewingjava.burnit.DataModels.HighestRep;
import com.brewingjava.burnit.DataModels.LeaderBoardItem;
import com.brewingjava.burnit.DataModels.TodayRep;
import com.github.mikephil.charting.data.ChartData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @GET("/Performance/")
    Call<String> getPerformanceTip(@Query("email") String email);

    @GET("/todayReps/")
    Call<List<TodayRep>> getTodaysReps(@Query("email") String email, @Query("type") String type);

    @GET("/HighestReps/")
    Call<List<HighestRep>> getHighestReps(@Query("email") String email, @Query("type") String type);

    @GET("/leaderboard/")
    Call<List<LeaderBoardItem>> getLeaderBoard();

    @GET("/Chart/")
    Call<List<ChartDataModel>> getChartData(@Query("email") String email, @Query("type") String type);

    @GET("/healthData/")
    Call<HealthData> getHealthData(@Query("email") String email);

    @FormUrlEncoded
    @POST("/signup/")
    Call<String> singUp(@Field("name") String name,
                        @Field("email") String email);

    @FormUrlEncoded
    @POST("/location/")
    Call<String> updateLocation(@Field("location") String location,
                                @Field("email") String email);

    @FormUrlEncoded
    @POST("/healthStats/")
    Call<String> updateStats(@Field("weight") int weight, @Field("height") int height,
                             @Field("email") String email);


    @FormUrlEncoded
    @POST("/reps/")
    Call<String> saveReps(@Field("reps") int reps,
                          @Field("type") String type,
                          @Field("email") String email);

}
