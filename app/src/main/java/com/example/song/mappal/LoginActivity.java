package com.example.song.mappal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accesstoken;
    AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.song.mappal",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //login버튼 클릭시 action을 위한 콜백함수 등록
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("페북아이디", loginResult.getAccessToken().getUserId());
                Intent intent_testActivity;
                intent_testActivity = new Intent(LoginActivity.this,
                        testActivity.class);
                intent_testActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);  //액티비티 스택제거
                intent_testActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_testActivity);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        //Token 관련

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                //Log.d("페북아이디",accesstoken.getUserId());
            }
        };
        // If the access token is available already assign it.

        accesstoken = AccessToken.getCurrentAccessToken();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
