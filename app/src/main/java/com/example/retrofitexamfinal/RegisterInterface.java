package com.example.retrofitexamfinal;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {



    String REGIST_URL = "http://54.180.155.66/";

    @FormUrlEncoded
    @POST("retrofit_simple_register.php")
    Call<String> getUserRegist(
            @Field("id") String id,
            @Field("pw") String pw,
            @Field("pwCheck") String pwCheck,
            @Field("nickname") String nickname
    );
}
