package com.gama.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gama.base.bean.SDKVersion;
import com.gama.data.login.ILoginCallBack;
import com.gama.data.login.response.SLoginResponse;
import com.gama.sdk.callback.IPayListener;
import com.gama.sdk.out.IHGame;
import com.gama.sdk.out.IHGameFactory;

public class MainActivity extends Activity {


    private IHGame ihGame;
    protected Button loginButton, gLoginBtn, payBtn;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.activity = this;

        loginButton = findViewById(R.id.demo_login);
        gLoginBtn = findViewById(R.id.demo_login_g);
        payBtn = findViewById(R.id.demo_pay);

        ihGame = IHGameFactory.create();

        //初始化sdk，此方法必须在其他所有接口前调用  SDKVersion.cn为简体中文版，SDKVersion.global为全球版
        ihGame.initSDK(this, SDKVersion.cn);
        //在游戏Activity的onCreate生命周期中调用
        ihGame.onCreate(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ihGame.initSDK(activity, SDKVersion.cn);
                //登陆接口 ILoginCallBack为登录成功后的回调
                ihGame.login(activity, new ILoginCallBack() {
                    @Override
                    public void onLogin(SLoginResponse sLoginResponse) {
                        if (sLoginResponse != null) {
                            String uid = sLoginResponse.getUserId();
                            String accessToken = sLoginResponse.getAccessToken();
                            String timestamp = sLoginResponse.getTimestamp();
                            Toast.makeText(activity,"登录成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        gLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ihGame.initSDK(activity, SDKVersion.global);
                //登陆接口 ILoginCallBack为登录成功后的回调
                ihGame.login(activity, new ILoginCallBack() {
                    @Override
                    public void onLogin(SLoginResponse sLoginResponse) {
                        if (sLoginResponse != null) {
                            String uid = sLoginResponse.getUserId();
                            String accessToken = sLoginResponse.getAccessToken();
                            String timestamp = sLoginResponse.getTimestamp();
                            Toast.makeText(activity,"登录成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String cpOrderId = "cp"+System.currentTimeMillis();
                String extra = "extraxx";
                ihGame.webPay(activity, cpOrderId,extra, new IPayListener() {
                    @Override
                    public void onPayFinish(Bundle bundle) {

                    }
                });
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        ihGame.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ihGame.onActivityResult(this,requestCode, resultCode,data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ihGame.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ihGame.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ihGame.onDestroy(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ihGame.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ihGame.onWindowFocusChanged(this,hasFocus);
    }

}
