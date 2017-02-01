package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by 155208U on 11/16/2016.
 */

public class Rank extends Activity implements OnClickListener {

    private Button btn_back;
    SharedPreferences SharePrefscore;
    SharedPreferences SharePrefname;
    boolean _active = true;
    Soundmanager soundmanager;
    int _splashTime = 5000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundmanager = new Soundmanager(getApplicationContext());
        soundmanager.PlayRANK();
        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.rank);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        TextView scoreText;
        TextView playerText;
        String highscore;
        String playername;

        scoreText = (TextView) findViewById(R.id.highscoreText);
        SharePrefscore = getSharedPreferences("Highscore", Context.MODE_PRIVATE);
        highscore = SharePrefscore.getString("Highscore","");
        String hi = highscore.replace("|",System.getProperty("line.separator"));
        playerText = (TextView) findViewById(R.id.nameText);
        SharePrefname = getSharedPreferences("Nameofplayer", Context.MODE_PRIVATE);
        playername = SharePrefname.getString("Nameofplayer","");
        String player = playername.replace("|",System.getProperty("line.separator"));
        //scoreText.setMovementMethod(new ScrollingMovementMethod());
        scoreText.setText(hi);
        playerText.setText(player);
        //playerText.setMovementMethod(new ScrollingMovementMethod());



        Log.d(TAG,"high"+playername + "" +hi);
    }
    public void onClick(View v){
        Intent intent = new Intent();
        soundmanager.StopRANK();
        if (v == btn_back) {
            //Change scene
            intent.setClass(this, Mainmenu.class);
        }  startActivity(intent);
    }
   /* @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
            Intent intent = new Intent(Rank.this, Mainmenu.class);
           startActivity(intent);
        }
        return true;
    }*/

    protected void onPause() {
        soundmanager.StopRANK();
        super.onPause();
    }

    protected void onStop()
    {
        soundmanager.StopRANK();
        super.onStop();
    }
    protected void onDestroy()
    {
        soundmanager.StopRANK();
        super.onPause();
    }
}
