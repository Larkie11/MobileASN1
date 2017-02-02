package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Html;
import android.text.style.TtsSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.CollationElementIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by 155208U on 11/16/2016.
 */

public class Settings extends Activity implements OnClickListener {

    boolean _active = true;
    int _splashTime = 5000;
    private Button btn_back, btn_share, btn_details;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ProfilePictureView profile;
    private Dialog details_dialog;
    private LoginManager loginManager;

    SharedPreferences SharePrefscore;
    String highscore;
    String lasthighscore;



    //Allow permissions
    //List<String> PERMISSIONS = Arrays.asList("publish_actions");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());


        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.settings);

        //Facebook profile picture
        profile = (ProfilePictureView) findViewById(R.id.picture);
        //Facebook share
        btn_share = (Button) findViewById(R.id.share);
        btn_share.setVisibility(View.INVISIBLE);
        shareDialog = new ShareDialog(this);
        //Facebook details
        btn_details = (Button) findViewById(R.id.details);
        btn_details.setVisibility(View.INVISIBLE);
        //Dialog details
        details_dialog = new Dialog(this);
        details_dialog.setContentView(R.layout.dialog_details);
        details_dialog.setTitle("Details");
        info = (TextView) details_dialog.findViewById(R.id.facebookinfo);

        callbackManager = CallbackManager.Factory.create();

        //Facebook login
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");

        SharePrefscore = getSharedPreferences("Highscore", Context.MODE_PRIVATE);

        highscore = SharePrefscore.getString("Highscore", "");
        lasthighscore = highscore.substring(highscore.lastIndexOf("|") + 1);

        //highscore = highscoreList.get(highscoreList.size()-1);

       // highscore = SharePrefscore.getString("Highscore","");

//        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                AccessToken at = AccessToken.getCurrentAccessToken();
//                if (currentAccessToken == null) {
//                    profile.setProfileId(" ");
//
//                } else {
//                    profile.setProfileId(Profile.getCurrentProfile().getId());
//                }
//            }
//        };
//
//        accessTokenTracker.startTracking();
//
//        loginManager = loginManager.getInstance();
//        loginManager.logInWithPublishPermissions(this, PERMISSIONS);


        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details_dialog.show();
            }
        });
        if (AccessToken.getCurrentAccessToken() != null) {
            RequestData();
            btn_share.setVisibility(View.VISIBLE);
            btn_details.setVisibility(View.VISIBLE);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    btn_share.setVisibility(View.INVISIBLE);
                    btn_details.setVisibility(View.INVISIBLE);
                    profile.setProfileId(null);
                }
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentTitle("Hello All I scored " + lasthighscore + " in Lets Shop!")
                        .setContentDescription("Sharing this game")
                        .setContentUrl(Uri.parse("https://www.facebook.com/games/manage"))
                        .build();

//                Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                SharePhoto photo = new SharePhoto.Builder()
//                        .setBitmap(image)
//                        .setCaption("Thank you for playing Game. Your final score is " + highscore)
//                        .build();
//
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(photo)
//                        .build();

                //ShareApi.share(content, null);
                shareDialog.show(content);
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                    btn_share.setVisibility(View.VISIBLE);
                    btn_details.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancel() {
                //info.setText("Login cancelled.");
                System.out.println("Login attempt cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                //info.setText("Login failed.");
                System.out.println("Login attempt failed");
            }


        });
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    public static void printHashKey(Context pContext) {

    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                        info.setText(Html.fromHtml(text));
                        profile.setProfileId(json.getString("id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        if (v == btn_back) {
            //Change scene
            intent.setClass(this, Mainmenu.class);
        }
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }
    protected void onDestroy()
    {
        super.onPause();
    }
}
