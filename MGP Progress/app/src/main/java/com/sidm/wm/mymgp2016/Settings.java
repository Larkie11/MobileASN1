package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.CollationElementIterator;
import java.text.StringCharacterIterator;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

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
        //Facebook login
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");

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
                        .setContentTitle("Hello All")
                        .setContentDescription("Sharing this game")
                        .setContentUrl(Uri.parse("https://www.facebook.com/games/manage"))
                        .build();
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
                info.setText("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login failed.");
            }


        });
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

//    public void printHashKey() {
//        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "net.simplifiedcoding.androidlogin",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//    }


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
