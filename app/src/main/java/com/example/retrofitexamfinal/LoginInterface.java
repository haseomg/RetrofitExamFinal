package com.example.retrofitexamfinal;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {
    String LOGIN_URL = "http://54.180.155.66/";

    @FormUrlEncoded
    @POST("retrofit_simplelogin.php")
    Call<String> getUserLogin(
            @Field("id") String id,
            @Field("pw") String pw
    );
}
