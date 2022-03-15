package com.example.petproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText join_email, join_password;
    private Button loginBtn1;
    private TextView signTv1;
    private PreferenceHelper preferenceHelper;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activty);

        preferenceHelper = new PreferenceHelper(this);

        join_email = (EditText) findViewById(R.id.join_email);
        join_password = (EditText) findViewById(R.id.join_password);

        loginBtn1 = (Button) findViewById(R.id.loginBtn1);
        signTv1 = (TextView) findViewById(R.id.signTv1);

        signTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        loginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        final String userEmail = join_email.getText().toString().trim();
        final String userPassword = join_password.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginInterface.LOGIN_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
            
        LoginInterface api = retrofit.create(LoginInterface.class);
        Call<String> call = api.getUserLogin(userEmail, userPassword);
        call.enqueue(new Callback<String>() {
            private final String TAG = "LoginActivity";

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("Success", response.body());
                    
                    String jsonResponse = response.body();
                    parseLoginData(jsonResponse);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });
    }

    private void parseLoginData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("ture")) {
                saveInfo(response);

                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                dialog = builder.setMessage("로그인에 성공하였습니다.").setPositiveButton("확인",
                        null).create();
                dialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInfo(String response) {
        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putUserEmail(dataobj.getString("userEmail"));
                    preferenceHelper.putUserPassword(dataobj.getString("userPassword"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}