package com.example.petproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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

public class RegistActivity extends AppCompatActivity {

    private EditText create_email, create_name, create_password, create_Rpassword;
    private Button checkBtn, acceptBtn, backBtn;
    private TextView tv1;
    private PreferenceHelper preferenceHelper;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activity);

        preferenceHelper = new PreferenceHelper(this);

        create_email = (EditText) findViewById(R.id.create_email);
        create_name = (EditText) findViewById(R.id.create_name);
        create_password = (EditText) findViewById(R.id.create_password);
        create_Rpassword = (EditText) findViewById(R.id.create_Rpassword);

        checkBtn = (Button) findViewById(R.id.checkBtn);
        acceptBtn = (Button) findViewById(R.id.acceptBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        final String email = create_email.getText().toString();
        final String name = create_name.getText().toString();
        final String password = create_password.getText().toString();
        final String Rpassword = create_Rpassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RegisterInterface.REGIST_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RegisterInterface api = retrofit.create(RegisterInterface.class);
        Call<String> call = api.getUserRegist(email, name, password, Rpassword);
        call.enqueue(new Callback<String>() {
            private final String TAG = "RegistActivity";

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("Success", response.body());

                    String jasonResponse = response.body();
                    try {
                        parseRegData(jasonResponse);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });
    }

    private void parseRegData(String response) throws  JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("true")) {
            saveInfo(response);
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
            dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인",
                    null).create();
            dialog.show();
        }
    }

    private void saveInfo(String response) {
        preferenceHelper.putIsLogin(true);
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putUserEmail(dataobj.getString("userEmail"));
                    preferenceHelper.putUserPassword(dataobj.getString("userPassword"));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
