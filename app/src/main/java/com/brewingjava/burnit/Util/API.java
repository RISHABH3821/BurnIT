package com.brewingjava.burnit.Util;
import com.brewingjava.burnit.DataModels.LeaderBoardItem;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @GET("/leaderboard/")
    Call<List<LeaderBoardItem>> getLeaderBoard();

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
    Call<String> updateStats(@Field("weight") int weight,@Field("height") int height,
                                @Field("email") String email);


    @FormUrlEncoded
    @POST("/reps/")
    Call<String> saveReps(@Field("reps") int reps,
                        @Field("type") String type,
                        @Field("email") String email);

}
