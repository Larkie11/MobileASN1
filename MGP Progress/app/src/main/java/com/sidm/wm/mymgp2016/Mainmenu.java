package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

public class Mainmenu extends Activity implements OnClickListener {

    private Button btn_start;
    private Button btn_help;
    private Button btn_rank;
    private Button btn_settings;
    private Button btn_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_help = (Button) findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        btn_rank = (Button) findViewById(R.id.btn_rank);
        btn_rank.setOnClickListener(this);

        btn_settings = (Button) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);

        btn_quit = (Button) findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_start) {
            //Change scene
            intent.setClass(this, Gamepage.class);
        } else if (v == btn_help) {
            intent.setClass(this, Help.class);
        }
        else if (v == btn_rank) {
            intent.setClass(this, Rank.class);
        }
        else if (v == btn_settings) {
            intent.setClass(this, Settings.class);
        }
        else if (v == btn_quit) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory( Intent.CATEGORY_HOME );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
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
