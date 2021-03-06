package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.SharedPreferences;

import static android.content.ContentValues.TAG;


public class GameMode extends Activity implements OnClickListener {

    private Button btn_casual;
    private Button btn_timed;
    private Button btn_back;
    Soundmanager  soundmanager;
    SharedPreferences SharedPrefMode;
    SharedPreferences.Editor modeEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundmanager = new Soundmanager(getApplicationContext());
        soundmanager.PlayMAIN();
        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.gamemode);

        btn_casual = (Button) findViewById(R.id.btn_casual);
        btn_casual.setOnClickListener(this);

        btn_timed = (Button) findViewById(R.id.btn_timed);
        btn_timed.setOnClickListener(this);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        SharedPrefMode = this.getSharedPreferences("Mode", Context.MODE_PRIVATE);
        modeEditor = SharedPrefMode.edit();
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        soundmanager.StopMAIN();
        if (v == btn_timed) {
            //Change scene
            modeEditor.putInt("Mode",1);
            modeEditor.commit();
            intent.setClass(this, Gamepage.class);
        } else if (v == btn_casual) {
            modeEditor.putInt("Mode",0);
            modeEditor.commit();
            intent.setClass(this, Gamepage.class);
        }
        else if (v == btn_back) {
            //Change scene
            intent.setClass(this, Mainmenu.class);
        }
        startActivity(intent);
    }

    protected void onPause() {

        soundmanager.StopMAIN();
        super.onPause();
    }

    protected void onStop()
    {
        soundmanager.StopMAIN();
        super.onStop();
    }
    protected void onDestroy()
    {
        soundmanager.StopMAIN();
        super.onPause();
    }
}
