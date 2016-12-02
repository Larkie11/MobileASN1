package com.sidm.wm.mymgp2016;

/**
 * Created by 155208U on 11/21/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by RoseHime on 21/11/2016.
 */

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;

    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] spaceship = new Bitmap[4];
    private Bitmap shelf,apples,button;

    // 4b) Variable as an index to keep track of the spaceship images
    private short spaceshipIndex = 0;

    Map<String,MyCoord> multiplePoints=new HashMap<String, MyCoord>();
    HashMap<String, Integer> cart;
    //Check if ship is moving
    Boolean moving = false;
    //Star sprite Animation
    private SpriteAnimation star_anim;

    // For spaceship location
    int mX, mY;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;
    Boolean showapples = false;
    Boolean touching = false;
    Boolean touchingrubbish = false;
    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);
        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        cart = new HashMap<String, Integer>();
        cart.put("apples",0);
        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.tilefloor);
        shelf =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shelf), ScreenWidth/10, ScreenHeight/4, true);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);
        apples =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.apples), ScreenWidth/10, ScreenHeight/10, true);
        button =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.apples), ScreenWidth/10, ScreenHeight/10, true);

        mX = 800;
        mY = 800;
        multiplePoints.put("appleshelf", new MyCoord(300, 300));
        multiplePoints.put("pearshelf", new MyCoord(900, 300));

        // 4c) Load the images of the spaceships

        //spaceship[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
        //spaceship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        //spaceship[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
        //spaceship[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);
        //student to scaled your spaceship base on scaledbg xxxx
        spaceship[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart), ScreenWidth/10, ScreenHeight/6, true);
        spaceship[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart1), ScreenWidth/10, ScreenHeight/6, true);
        spaceship[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart2), ScreenWidth/10, ScreenHeight/6, true);
        spaceship[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart3), ScreenWidth/10, ScreenHeight/6, true);

        //Star animation sprite shit
        star_anim = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.flystar)), (int)ScreenWidth/5, (int)ScreenHeight/5, true),320,64,5,5);
        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    public void RenderTextOnScreen(Canvas canvas, String text, int posX, int posY, int textsize)
    {
        if (canvas != null && text.length() != 0)
        {
            Paint paint = new Paint();
            paint.setARGB(255,0,0,0);
            paint.setStrokeWidth(2);
            paint.setTextSize(textsize);
            canvas.drawText(text,posX,posY,paint);
        }
    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);

        // 4d) Draw the spaceships
        canvas.drawBitmap(spaceship[spaceshipIndex], mX, mY, null);



        star_anim.draw(canvas);
        star_anim.setY(600);
        MyCoord coord=multiplePoints.get("appleshelf");

        if(coord!=null) {
            canvas.drawBitmap(shelf, coord.getX(), coord.getY(), null);
            canvas.drawBitmap(apples, coord.getX(), coord.getY(), null);
        }
        if(showapples)
            canvas.drawBitmap(button, coord.getX()+300, coord.getY(), null);

        MyCoord pear=multiplePoints.get("pearshelf");
        if(pear != null)
        {
            canvas.drawBitmap(shelf, pear.getX(), pear.getY(), null);
        }

        int i = multiplePoints.size();
        RenderTextOnScreen(canvas, "Size: " + i,150,100,30);

        Iterator iterator = cart.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            Integer value = cart.get(key);

            System.out.println("Hashmap: " + value.toString());
            RenderTextOnScreen(canvas, "Apples: " + value.toString(),300,130,60);
        }

        // Bonus) To print FPS on the screen
        RenderTextOnScreen(canvas, "FPS: " + FPS,150,70,30);
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        if (x2 >= x1 && x2 <= x1 + w1) {
            if (y2>=y1 && y2<=y1+h1)
                return true;
        }
        if (x2 + w2 >= x1 && x2 + w2 <= x1 + w1) { // Top right corner
             if (y2>=y1 && y2<=y1+h1)
                 return true;
        }
    return false;

}
    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;
        switch (GameState) {
            case 0: {
                star_anim.update(System.currentTimeMillis());
                // 3) Update the background to allow panning effect

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                spaceshipIndex++;

                if (spaceshipIndex > 3)
                {
                    spaceshipIndex = 0;
                }

            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getAction();


        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short) event.getX();
        short Y = (short) event.getY();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                if(CheckCollision(mX,mY,spaceship[spaceshipIndex].getWidth(),spaceship[spaceshipIndex].getHeight(),X,Y,0,0))
                {
                    moving = true;
                }
                else
                {
                    moving = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(moving == true)
                {
                    mX = (short)(X - spaceship[spaceshipIndex].getWidth()/2);
                    mY = (short)(Y - spaceship[spaceshipIndex].getHeight()/2);
                }
                //Check if ship is colliding with star
                MyCoord coord=multiplePoints.get("appleshelf");
                MyCoord coord2=multiplePoints.get("pearshelf");

                if(CheckCollision(mX,mY,spaceship[spaceshipIndex].getWidth(),spaceship[spaceshipIndex].getHeight(),star_anim.getX(),star_anim.getY(),star_anim.getSpriteWidth()/2,star_anim.getSpriteHeight()/2))
                {
                    Random rX = new Random();
                    Random rY = new Random();
                    for(int idx = 20; idx < ScreenWidth - 20; idx++)
                    {
                        star_anim.setX(rX.nextInt(idx));
                        star_anim.setY(rY.nextInt(idx));
                    }
                }
                if(coord2!=null) {
                    if (CheckCollision(mX, mY, spaceship[spaceshipIndex].getWidth(), spaceship[spaceshipIndex].getHeight(), coord2.getX(), coord2.getY(), shelf.getWidth(), shelf.getHeight())) {
                        for (int idx = 20; idx < ScreenWidth - 20; idx++) {
                            showapples = true;
                            if(!touchingrubbish){

                                String index = "apples";
                                Integer value = cart.get(index);
                                if(value > 0)
                                cart.put(index, value - 1);
                                touchingrubbish = true;
                            }
                            break;
                        }
                    }
                    else {
                        touchingrubbish = false;
                    }
                }
                if(coord!=null) {
                    if (CheckCollision(mX, mY, spaceship[spaceshipIndex].getWidth(), spaceship[spaceshipIndex].getHeight(), coord.getX(), coord.getY(), shelf.getWidth(), shelf.getHeight())) {
                        for (int idx = 20; idx < ScreenWidth - 20; idx++) {
                            coord.setX(500);
                            coord.setY(500);
                            showapples = true;


                            if(!touching)
                            {
                                String index = "apples";
                                Integer value = cart.get(index);
                                cart.put(index, value + 1);
                                touching = true;

                            }
                            break;
                        }
                    }
                    else {
                        showapples = false;
                        touching = false;
                    }
                }


            break;
        }
        /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mX = (short)(X - spaceship[spaceshipIndex].getWidth()/2);
            mY = (short)(Y - spaceship[spaceshipIndex].getHeight()/2);
        }*/

        return true;
    }
}