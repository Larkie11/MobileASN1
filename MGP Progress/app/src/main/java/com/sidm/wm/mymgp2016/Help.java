package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;

/**
 * Created by 155208U on 11/16/2016.
 */

public class Help extends Activity implements OnClickListener{

    boolean _active = true;
    int _splashTime = 5000;
    private Button btn_back;
    Soundmanager soundmanager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundmanager = new Soundmanager(getApplicationContext());
        soundmanager.PlayOTHERS();
        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.help);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        //thread for displaying the Splash Screen
    }
    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_back) {
            //Change scene
            soundmanager.StopOTHERS();
            intent.setClass(this, Mainmenu.class);
        }  startActivity(intent);
    }

    protected void onPause() {
        soundmanager.StopOTHERS();
        super.onPause();
    }

    protected void onStop()
    {
        soundmanager.StopOTHERS();
        super.onStop();
    }
    protected void onDestroy()
    {
        soundmanager.StopOTHERS();
        super.onPause();
    }
}
