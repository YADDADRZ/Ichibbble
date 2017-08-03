package com.project.liuzhenyu.ichibbble.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.liuzhenyu.ichibbble.Dribbble.Auth.Auth;
import com.project.liuzhenyu.ichibbble.Dribbble.Auth.AuthActivity;
import com.project.liuzhenyu.ichibbble.Dribbble.Dribbble;
import com.project.liuzhenyu.ichibbble.R;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.activity_login_btn) TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // bind button to this loginBtn variable use ButterKnife
        ButterKnife.bind(this);
        Dribbble.init(this);

        if (!Dribbble.isLogin()){
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // we need URL to start a webview which is in Auth.
                    Auth.openAuthActivity(LoginActivity.this);
                }
            });
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Auth.REQ_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            String token;
            try {
                token = Auth.fetchAccessToken(authCode);
                Log.i("AUTH_CODE", token);
                Dribbble.login(LoginActivity.this, token);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
