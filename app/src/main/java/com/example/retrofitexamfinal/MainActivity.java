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

public class MainActivity extends AppCompatActivity
{
    public final String TAG = "[MainActivity]";

    private EditText etId, etPw, etPwCheck, etNickName;
    private Button btnregister;
    private TextView tvlogin;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceHelper = new PreferenceHelper(this);



        etId = (EditText) findViewById(R.id.etId);
//        etPw = (EditText) findViewById(R.id.etPw);
//        etPwCheck = (EditText) findViewById(R.id.etPwCheck);
        etNickName = (EditText) findViewById(R.id.etNickName);

        btnregister = (Button) findViewById(R.id.btn);
        tvlogin = (TextView) findViewById(R.id.tvlogin);

        tvlogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerMe();
            }
        });
    }

    private void registerMe()
    {
        Log.i(TAG,"registerMe 메서드 작동");
        final String id = etId.getText().toString();
        final String nickname = etNickName.getText().toString();
        Log.i(TAG,"String id : " + id);
        Log.i(TAG,"String nickname : " + nickname);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RegisterInterface.REGIST_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RegisterInterface api = retrofit.create(RegisterInterface.class);
        Call<String> call = api.getUserRegist(id,nickname);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response)
            {
                Log.i(TAG,"onResponse 메서드 작동");

                if (response.isSuccessful() && response.body() != null)
                {
                    Log.e("onSuccess [성공]", response.body());

                    String jsonResponse = response.body();
                    try
                    {
                        parseRegData(jsonResponse);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
            {
                Log.e(TAG, "에러 = " + t.getMessage());
            }
        });
    }

    private void parseRegData(String response) throws JSONException
    {
        Log.i(TAG,"parseRegData 메서드 작동");
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("true"))
        {
            saveInfo(response);
            Toast.makeText(MainActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"parseRegData status == true");
        }
        else
        {
            Log.i(TAG,"parseRegData status == true가 아닐 때");
            Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInfo(String response)
    {
        preferenceHelper.putIsLogin(true);
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                Log.i(TAG, "제이슨 상태가 true일 때");
//                JSONArray dataArray = jsonObject.getJSONArray("data");
//                for (int i = 0; i < dataArray.length(); i++)
//                {
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    preferenceHelper.putName(dataobj.getString("nickname"));
//                    preferenceHelper.putHobby(dataobj.getString("pwCheck"));
//                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}