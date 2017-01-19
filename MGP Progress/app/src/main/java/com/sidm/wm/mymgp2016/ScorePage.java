package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by 155208u on 1/9/2017.
 */

public class ScorePage extends Activity implements OnClickListener {

    private Button btn_back;
    SharedPreferences SharePrefscore;
    SharedPreferences SharePrefname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.score);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
//        TextView scoreText;
//        TextView playerText;
//        int highscore;
//        String playername;
//
//        scoreText = (TextView) findViewById(R.id.highscoreText);
//        SharePrefscore = getSharedPreferences("Highscore", Context.MODE_PRIVATE);
//        highscore = SharePrefscore.getInt("Highscore",0);
//
//        playerText = (TextView) findViewById(R.id.nameText);
//        SharePrefname = getSharedPreferences("Nameofplayer", Context.MODE_PRIVATE);
//        playername = SharePrefname.getString("Nameofplayer","");
//
//        scoreText.setText(String.format("" + highscore));
//        playerText.setText(String.format(playername));
//
//        Log.d(TAG,"high"+playername + "" +highscore);
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_back) {
            //Change scene
            intent.setClass(this, Mainmenu.class);
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

