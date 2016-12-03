package com.sidm.wm.mymgp2016;

/**
 * Created by 155208U on 11/21/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering
    Cart cart = new Cart();
    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;

    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    Boolean moveout = false,movein = false;
    Integer priceof = 0;
    String touchingitem = "";

    //ALL THE COLORS
    int black = getContext().getResources().getColor(R.color.Black);
    int white = getContext().getResources().getColor(R.color.White);
    int red = getContext().getResources().getColor(R.color.Red);

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] spaceship = new Bitmap[4];
    //Variables for all other stuff
    private Bitmap shelf,apples,button, cartbg, removedialogue;
    private Bitmap plus,minus;
    private Bitmap applelogo, pearlogo;

    //Set coordiate of button
    MyCoord buttonCord = new MyCoord(1630,100);
    //Get x of button to move it later
    int x = buttonCord.getX();

    // 4b) Variable as an index to keep track of the spaceship images
    private short spaceshipIndex = 0;

    //Position of shelves
    Map<String,MyCoord> multiplePoints=new HashMap<String, MyCoord>();
    //Position of ui stuff like buttons/dialogue boxes
    Map<String,MyCoord> UIStuff=new HashMap<String, MyCoord>();


    //Check if is moving
    Boolean moving = false;
    //Star sprite Animation
    private SpriteAnimation star_anim;

    // For spaceship location
    int mX, mY;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;


    Boolean touching = false;
    Boolean touchingrubbish = false;
    //Show the remove dialogue box
    Boolean showremove = false;
    // Variable for Game State check
    private short GameState;
    Integer numbertoremove = 1;

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
        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.tilefloor);
        shelf =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shelf), ScreenWidth/10, ScreenHeight/4, true);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);
        apples =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.apples), ScreenWidth/10, ScreenHeight/10, true);
        button =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button), ScreenWidth/10, ScreenHeight/10, true);
        removedialogue =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.showdialogue), ScreenWidth/2, ScreenHeight/2, true);

        cartbg =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button), ScreenWidth/5, ScreenHeight, true);
        applelogo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.applelogo), ScreenWidth/12, ScreenHeight/11, true);
        pearlogo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pearlogo), ScreenWidth/12, ScreenHeight/11, true);
        plus =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus), ScreenWidth/13, ScreenHeight/13, true);
        minus =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.minus), ScreenWidth/13, ScreenHeight/13, true);

        mX = 800;
        mY = 800;

        //Add in the coordinates of the shelves for collision checking later
        multiplePoints.put("appleshelf", new MyCoord(300, 300));
        multiplePoints.put("pearshelf", new MyCoord(900, 300));


        UIStuff.put("Dialogue Box", new MyCoord(500,280));
        UIStuff.put("Remove button", new MyCoord(600,650));
        UIStuff.put("Cancel button", new MyCoord(1100,650));
        UIStuff.put("Plus", new MyCoord(1000,450));
        UIStuff.put("Minus", new MyCoord(700,450));
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
    public void RenderTextOnScreen(Canvas canvas, String text, int posX, int posY, int textsize, int color)
    {
        Paint paint = new Paint();
        if (canvas != null && text.length() != 0)
        {
            Typeface tf =Typeface.createFromAsset(getContext().getAssets(),"fonts/kcreg.ttf");
            paint.setTypeface(tf);
            canvas.drawText("Sample text in bold RECOGNITION",0,0,paint);
            paint.setARGB(255,0,0,0);
            paint.setStrokeWidth(2);
            paint.setTextSize(textsize);
            paint.setColor(color);
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

        canvas.drawBitmap(button, buttonCord.getX(), buttonCord.getY(), null);
        canvas.drawBitmap(cartbg, buttonCord.getX()+180, buttonCord.getY(), null);

        MyCoord pear=multiplePoints.get("pearshelf");

        if(pear != null)
        {
            canvas.drawBitmap(shelf, pear.getX(), pear.getY(), null);
        }

        int i = cart.GetCartSize();
        RenderTextOnScreen(canvas, "Size: " + i,150,100,30,black);

        int textYpos = 250;
        int imageYpos = 100;
        int sums = 0;
        Iterator price = cart.prices.keySet().iterator();
        Iterator iterator = cart.mycart.keySet().iterator();
        Integer valueofitem = 0;

        String text="";
        Integer sum = 0;
        int appleprice = 0;
        for(Map.Entry<String, Integer> entry : cart.mycart.entrySet()) {
            System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > 0) {
                textYpos += 50;
                imageYpos += 100;
                RenderTextOnScreen(canvas, key + " " + value.toString(), buttonCord.getX() + 350, textYpos, 50, black);
                UIStuff.put(key, new MyCoord(buttonCord.getX() + 180, imageYpos));
                MyCoord touchapple = UIStuff.get(key);

                if (key == "Apples") {

                    //array[0] = (valueofitem * values);
                    canvas.drawBitmap(applelogo, touchapple.getX(), touchapple.getY(), null);
                }
                if (key == "Pears") {
                    canvas.drawBitmap(pearlogo, touchapple.getX(), touchapple.getY(), null);
                }
                int priceofapple = cart.prices.get(key);
                appleprice += priceofapple * value;

            } else {
                textYpos -= 50;
                imageYpos -= 50;
            }
        }
/*
        while (iterator.hasNext()) {
           String key = iterator.next().toString();
            Integer value = cart.get(key);
            int values = cart.get(key);
            valueofitem = prices.get(key);

            sum+=values;

            //To Calculate the price of each item in cart
            int[] array = new int[cart.size()];

            //System.out.println("Hashmap: " + value.toString());
            //if(value > 0 && key == "apples")
                textYpos += 50;
            if(value > 0) {
                RenderTextOnScreen(canvas, key + " " + value.toString(), buttonCord.getX() + 230, textYpos, 60);
                touchableCarts.put(key,new MyCoord(buttonCord.getX() + 230,textYpos));
                MyCoord touchapple=touchableCarts.get(key);

                if (key == "Apples")
                {
                    array[0] = (valueofitem * values);
                    canvas.drawBitmap(apples, touchapple.getX(), touchapple.getY(), null);
                }
                if (key == "Pears")
                {
                    array[1] = (valueofitem * values);
                }
            }

            else
                textYpos -= 50;

           /// for (int a : array)
               // sums += a;

        }*/

        RenderTextOnScreen(canvas, "Price" + appleprice, buttonCord.getX() + 230, textYpos+500, 60,black);

        //System.out.println("Sum: " + priceof.toString());
        //RenderTextOnScreen(canvas, "Price" + sum, buttonCord.getX() + 230, textYpos+500, 60);

        RenderTextOnScreen(canvas, "CART",buttonCord.getX()+30,170,50,black);

        if(showremove) {
            canvas.drawBitmap(removedialogue, UIStuff.get("Dialogue Box").getX(), UIStuff.get("Dialogue Box").getY(), null);
            RenderTextOnScreen(canvas, "Do you want to remove from cart?" ,UIStuff.get("Dialogue Box").getX() + 100, UIStuff.get("Dialogue Box").getY() + 100,60,white);
            canvas.drawBitmap(button, UIStuff.get("Remove button").getX(), UIStuff.get("Remove button").getY(), null);
            canvas.drawBitmap(button, UIStuff.get("Cancel button").getX(), UIStuff.get("Cancel button").getY(), null);
            canvas.drawBitmap(plus, UIStuff.get("Plus").getX(), UIStuff.get("Plus").getY(), null);
            canvas.drawBitmap(minus, UIStuff.get("Minus").getX(), UIStuff.get("Minus").getY(), null);
            RenderTextOnScreen(canvas, "Yes" ,UIStuff.get("Remove button").getX() + 50, UIStuff.get("Remove button").getY() + 80,60,red);
            RenderTextOnScreen(canvas, "Cancel" ,UIStuff.get("Cancel button").getX() + 50, UIStuff.get("Cancel button").getY() + 80,60,black);
            RenderTextOnScreen(canvas, numbertoremove.toString() ,UIStuff.get("Plus").getX() -100,UIStuff.get("Plus").getY()+50,60,white);

        }

        // Bonus) To print FPS on the screen
        RenderTextOnScreen(canvas, "FPS: " + FPS,150,70,30,black);
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        if(Math.abs(x1 - x2) < w1 + w2)
        {
            if(Math.abs(y1 - y2) < h1 + h2)
            {
                return true;
            }
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

                if(moveout) {
                    if (x >= 1300) {
                        x -= (dt * 300);
                        buttonCord.setX(x);
                    } else
                        moveout = false;
                }
                if(movein) {
                    if (x <= 1630) {
                        x += (dt * 300);
                        buttonCord.setX(x);
                    } else
                        movein = false;
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
            if(!showremove) {
                if (UIStuff.get("Apples") != null) {
                    String index = "Apples";
                    Integer value = cart.mycart.get(index);
                    if (value > 0)
                        if (CheckCollision(UIStuff.get(index).getX(), UIStuff.get(index).getY(), applelogo.getWidth(), applelogo.getHeight(), X, Y, 0, 0)) {
                            showremove = true;
                            touchingitem = index;
                        }
                }

                if (UIStuff.get("Pears") != null) {
                    String index = "Pears";
                    Integer value = cart.mycart.get(index);
                    if (value > 0)
                        if (CheckCollision(UIStuff.get(index).getX(), UIStuff.get(index).getY(), applelogo.getWidth(), applelogo.getHeight(), X, Y, 0, 0)) {
                            showremove = true;
                            touchingitem = index;
                        }
                }
            }
                if(showremove) {
                    Integer value = cart.mycart.get(touchingitem);
                    if (CheckCollision(UIStuff.get("Remove button").getX(), UIStuff.get("Remove button").getY(), button.getWidth(), button.getHeight(), X, Y, 0, 0)) {
                        cart.removeFromCart(touchingitem, numbertoremove);
                        showremove = false;
                        numbertoremove = 1;
                    }
                    if (CheckCollision(UIStuff.get("Cancel button").getX(), UIStuff.get("Cancel button").getY(), button.getWidth(), button.getHeight(), X, Y, 0, 0)) {
                        showremove = false;
                        numbertoremove = 1;
                    }
                    if (CheckCollision(UIStuff.get("Plus").getX(), UIStuff.get("Plus").getY(), button.getWidth(), button.getHeight(), X, Y, 0, 0)) {
                       if(numbertoremove < value)
                        numbertoremove++;
                    }
                    if (CheckCollision(UIStuff.get("Minus").getX(), UIStuff.get("Minus").getY(), button.getWidth(), button.getHeight(), X, Y, 0, 0)) {
                        numbertoremove--;
                        if(numbertoremove == 0)
                            numbertoremove = value;
                    }
                }
                if (CheckCollision(buttonCord.getX(), buttonCord.getY(), button.getWidth(), button.getHeight(), X, Y, 0, 0)) {
                    if (buttonCord.getX() >= 1630)
                        moveout = true;
                    if (buttonCord.getX() <= 1300)
                        movein = true;
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

                if(CheckCollision(mX,mY,spaceship[spaceshipIndex].getWidth()/2,spaceship[spaceshipIndex].getHeight()/2,star_anim.getX(),star_anim.getY(),star_anim.getSpriteWidth(),star_anim.getSpriteHeight()))
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
                    if (CheckCollision(mX, mY, spaceship[spaceshipIndex].getWidth()/2, spaceship[spaceshipIndex].getHeight()/2, coord2.getX(), coord2.getY(), shelf.getWidth()/2, shelf.getHeight()/2)) {
                        for (int idx = 20; idx < ScreenWidth - 20; idx++) {
                            if(!touchingrubbish){
                               cart.removeFromCart("Apples",1);
                                cart.addToCart("Pears",1);
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
                    if (CheckCollision(mX, mY, spaceship[spaceshipIndex].getWidth()/2, spaceship[spaceshipIndex].getHeight()/2, coord.getX(), coord.getY(), shelf.getWidth()/2, shelf.getHeight()/2)) {
                        for (int idx = 20; idx < ScreenWidth - 20; idx++) {
                            coord.setX(500);
                            coord.setY(500);

                            if(!touching)
                            {
                               cart.addToCart("Apples",1);
                                touching = true;
                            }
                            break;
                        }
                    }
                    else {
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