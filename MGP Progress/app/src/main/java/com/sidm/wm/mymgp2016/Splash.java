package com.sidm.wm.mymgp2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 155208U on 11/16/2016.
 */

public class Splash extends Activity {
    boolean _active = true;
    int _splashTime = 5000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        //thread for displaying the Splash Screen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(200);
                        if(_active) {
                            waited += 200;
                        }
                    }
                } catch(InterruptedException e) {
                    //do nothing
                } finally {
                    _active = false;
                    Intent intent = new Intent(Splash.this, Mainmenu.class);
                    startActivity(intent);
                    //Create new activity based on and intent with CurrentActivity
                    //Intent intent = new Intent(Help.this, Mainmenu.class);
                    //startActivity(intent);
                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
            Intent intent = new Intent(Splash.this, Mainmenu.class);
            startActivity(intent);
        }
        return true;
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
