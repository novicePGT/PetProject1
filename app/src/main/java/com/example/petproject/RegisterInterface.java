package com.example.petproject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
public interface RegisterInterface {
    String REGIST_URL = "http://3.39.42.187:8080/api/";

    @FormUrlEncoded
    @POST("retrofit_simpleregister.php")
    Call<String> getUserRegist(
            @Field("userEmail") String userEamil,
            @Field("userPassword") String userPassword,
            @Field("userRpassword") String userRpassword,
            @Field("password") String password
    );
}
