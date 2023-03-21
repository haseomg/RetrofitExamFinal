package com.example.retrofitexamfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private EditText inputId, inputPw;
    private Button btnlogin;
    private TextView tvreg;
    private PreferenceHelper preferenceHelper;

    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceHelper = new PreferenceHelper(this);

        inputId = (EditText) findViewById(R.id.inputId);
        inputPw = (EditText) findViewById(R.id.inputPw);

        btnlogin = (Button) findViewById(R.id.btn);
        tvreg = (TextView) findViewById(R.id.tvreg);

        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                Login.this.finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {

        final String userId = inputId.getText().toString().trim();
        final String userPw = inputPw.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginInterface.LOGIN_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        LoginInterface api = retrofit.create(LoginInterface.class);
        Call<String> call = api.getUserLogin(userId, userPw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.e("onSuccess", response.body());

                    String jsonResponse = response.body();
                    parseLoginData(jsonResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });

    }

    private void parseLoginData(String response) {
        try {
            jsonObject = new JSONObject(response);
            Log.i(TAG, "jsonObject check 1 : " + jsonObject);
            Log.i(TAG, "response check  : " + response);

            if (jsonObject.getString("status").equals("true")) {
                saveInfo(response);

                Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "ERROR CHECK : " + e);
            Log.i(TAG, "jsonObject check : " + jsonObject);
            Log.i(TAG, "response check  : " + response);
            // 현재 에러가 나서 여기로 작동 된다.
            Toast.makeText(Login.this, "Login Successfully?", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveInfo(String response) {
        preferenceHelper.putIsLogin(true);
        Log.i(TAG, "saveInfo Method");
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putName(dataobj.getString("name"));
                    preferenceHelper.putHobby(dataobj.getString("hobby"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}