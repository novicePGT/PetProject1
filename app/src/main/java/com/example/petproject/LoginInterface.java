package com.example.petproject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {
    String LOGIN_URL = "http://3.39.42.187:8080/api/";
    //@Field는 POST로 서버에 값을 보낼 때 붙여야 하는 어노테이션이다
    //@Field() 안의 큰따옴표 안에는 PHP 파일에서 $_POST['username'];
    // 의 [''] 안에 있는 것과 똑같은 이름을 넣어야 한다.
    @FormUrlEncoded
    @POST("retrofit_simplelogin.php")
    Call<String> getUserLogin(
            @Field("userEmail") String userEmail,
            @Field("userPassword") String userPassword
    );
}
