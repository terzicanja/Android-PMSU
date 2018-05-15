package com.example.w10.pmsu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.w10.pmsu.service.ServiceUtils;
import com.example.w10.pmsu.service.UserService;

import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    UserService userService;
    ServiceUtils serviceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart()",Toast.LENGTH_SHORT).show();
    }

    public void btnStartPostsActivity(View view){

        EditText username_login = findViewById(R.id.username_login);
        EditText password_login = findViewById(R.id.password_login);

        final String username = username_login.getText().toString();
        final String password = password_login.getText().toString();

        userService = ServiceUtils.userService;
        Call<User> call = userService.getUserByUsername(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User u = response.body();

                if (u.getUsername().equals(username) && u.getPassword().equals(password)){
                    Intent intent = new Intent(LoginActivity.this, PostsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });



//        Intent i = new Intent(this, PostsActivity.class);
//        startActivity(i);
//        finish();
    }
}
