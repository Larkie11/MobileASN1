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

public class Rank extends Activity implements OnClickListener {

    private Button btn_back;
    boolean _active = true;
    int _splashTime = 5000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.rank);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }
    public void onClick(View v) {
        Intent intent = new Intent();
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
