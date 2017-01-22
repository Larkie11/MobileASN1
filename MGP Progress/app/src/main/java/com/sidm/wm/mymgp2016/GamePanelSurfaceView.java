package com.sidm.wm.mymgp2016;

/**
 * Created by 155208U on 11/21/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.AlertDialog.Builder;

import com.facebook.FacebookSdk;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map.Entry;
import java.util.Set;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering
    Cart cart;
    ShopList shoppinglist;
    HMStrings strings = new HMStrings();
    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    private Boolean ispaused = false;
    private Objects pause1;
    private Objects pause2;
    private Boolean restartPressed = false;
    private Objects restart;
    private Objects yes;

    Soundmanager soundmanager;
    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;
    int price;
    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    Boolean moveout ,movein , docheckout;
    Boolean movelistin, movelistout;
    String touchingitem = "";
    String addingwhat = "";
    Boolean clearstage = false;
    Vibrator v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    //ALL THE COLORS
    int black = getContext().getResources().getColor(R.color.Black);
    int white = getContext().getResources().getColor(R.color.White);
    int red = getContext().getResources().getColor(R.color.Red);
    int blue = getContext().getResources().getColor(R.color.Blue);
    int purple = getContext().getResources().getColor(R.color.Purple);

    int slsum = 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] spaceship = new Bitmap[4];
    private Bitmap[] cash = new Bitmap[7];
    //Variables for all other stuff
    private Bitmap shelf,apples,cartbutton, cartbg, removedialogue, button, addtocartbutton, shoppingList, leaving, shoppingListbutton;
    private Bitmap plus,minus;
    private Bitmap applelogo, pearlogo, flower, checkoutbutton;

    //Set coordiate of button
    MyCoord buttonCord = new MyCoord(100,100);
    //Get x of button to move it later
    int x = 0;
    //get y of button to move it later
    int y = 0;

    // 4b) Variable as an index to keep track of the spaceship images
    private short spaceshipIndex = 0;

    //Position of shelves
    Map<String,MyCoord> multiplePoints=new HashMap<String, MyCoord>();
    //Position of ui stuff like buttons/dialogue boxes
    Map<String,MyCoord> UIStuff=new HashMap<String, MyCoord>();

    //Map<String,Integer> ShoppingList = new HashMap<String,Integer>();
    //temp hashmap to store shoplist data
    Map<String,Integer> temp = new HashMap<String,Integer>();

    String[] itemList = {"Apples", "Pears", "Flowers"};

    private Random random = new Random();

    //Check if is moving
    Boolean moving = false;
    Boolean playernear = false;

    Boolean movingsprite = false;
    //Star sprite Animation
    //private SpriteAnimation star_anim;
    //private SpriteAnimation cash_anim;
    private SpriteAnimation playeravatar;
    private SpriteAnimation cashier;
    // For spaceship location
    int mX, mY;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;
    MyCoord pause, toUnpause, toRestart, restartYes;



    Boolean showaddtocart = false, showcashier = false;
    //Show the remove dialogue box
    Boolean showremove = false;
    // Variable for Game State check
    private short GameState;
    Integer numbertoremove = 1;
    Activity activityTracker = new Activity();
    CharSequence text;

    private long lastTime = System.currentTimeMillis();

    //Week 13
    int toastTime;
    Toast toast;
	public boolean showAlert = false;
	AlertDialog.Builder alert = null;
	private Alert AlertObj;
    SharedPreferences SharedPrefname;
    SharedPreferences.Editor editorN;

    String playername;
    SharedPreferences SharePrefscore;
    SharedPreferences.Editor editorS;
    int highscore;
    String value;
    String scorevalue;
    int randDiscount;

    //Week 14
    private SensorManager sensor;
    float[] SensorVar = new float[3];
    private float[]values = {0,0,0};
    private Bitmap ball;
    final long startTime;
    long duration;
    long timeElasped = 0;
    long gameTimer;
    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context, Activity activity){
        // Context is the current state of the application/object
        super(context);
        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        cart = new Cart();
        shoppinglist = new ShopList();

        duration = 0;
        startTime = System.nanoTime();
        gameTimer = 1000;
        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        GameState = 0;
        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.tilefloor);
        shelf =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shelf), ScreenWidth/10, ScreenHeight/4, true);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);
        leaving = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.exit), ScreenWidth, ScreenHeight, true);
        apples =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.apples), ScreenWidth/10, ScreenHeight/10, true);
        flower =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.flower), ScreenWidth/10, ScreenHeight/10, true);
        cartbutton =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.showdialogue), ScreenWidth/10, ScreenHeight/10, true);
        button =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button), ScreenWidth/10, ScreenHeight/10, true);
        addtocartbutton =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.showdialogue), ScreenWidth/4, ScreenHeight/10, true);
        checkoutbutton =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.showdialogue), ScreenWidth/4, ScreenHeight/10, true);
        removedialogue =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.showdialogue), ScreenWidth/2, ScreenHeight/2, true);
        cartbg =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.showdialogue), ScreenWidth, ScreenHeight, true);
        applelogo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.applelogo), ScreenWidth/12, ScreenHeight/11, true);
        pearlogo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pearlogo), ScreenWidth/12, ScreenHeight/11, true);
        plus =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus), ScreenWidth/13, ScreenHeight/13, true);
        minus =   Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.minus), ScreenWidth/13, ScreenHeight/13, true);
        shoppingList = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shoppinglist), ScreenWidth/4, ScreenHeight/3, true);
        shoppingListbutton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shoppinglist), ScreenWidth/6, ScreenHeight/15, true);
         moving = false;
         playernear = false;
        showaddtocart= showcashier = false;
        soundmanager = new Soundmanager(context);

        numbertoremove = 1;
         movingsprite = false;

         moveout =movein= docheckout = false;
         movelistin = movelistout = false;
         touchingitem = "";
         addingwhat = "";
         clearstage = false;
        mX = ScreenWidth/5;
        mY = (ScreenHeight/2)+300;
        price = 0;

        ispaused = false;
        pause1 = new Objects(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.pause_no)), (int)ScreenWidth/15, (int)ScreenHeight/10, true),ScreenWidth-200,30);
        pause2 = new Objects(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.unpause)), (int)ScreenWidth/15, (int)ScreenHeight/10, true),ScreenWidth-200,30);
        restart = new Objects(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.restart)), (int)ScreenWidth/15, (int)ScreenHeight/10, true),ScreenWidth-200,30);
        yes = new Objects(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.yes)), (int)ScreenWidth/15, (int)ScreenHeight/10, true),ScreenWidth-200,30);

        int lowest = 1;
        int highest = 4;
        //ANDY's Shopping list
        //rand shopping list
        for(int j = 0; j < itemList.length; j++) {
            int idx = random.nextInt(itemList.length);
            String randomItem = (itemList[idx]);

            int idx2 = random.nextInt(highest - lowest) + lowest;

            shoppinglist.addToList(randomItem, idx2);
            shoppinglist.addToList(randomItem, idx2);
            shoppinglist.addToList(randomItem, idx2);
        }

        temp.putAll(shoppinglist.myshoppinglist);

        int lowestdiscount = 1;
        int highestdiscount = 5;
        randDiscount = random.nextInt(highestdiscount - lowestdiscount) + lowestdiscount;

        //Get price of everything in shopping list to give player their budget
        for(Map.Entry<String,Integer>sl:shoppinglist.myshoppinglist.entrySet())
        {
            int extraBudget = random.nextInt(3-1) + 1;
            String key = sl.getKey();
            Integer value = sl.getValue();
            if (value > 0) {
                int priceofbudget = cart.prices.get(key);
                slsum += (priceofbudget * value) + extraBudget;
            }
        }

        //Wei Min - Coordinates for the shelves
        //Add in the coordinates of the shelves for collision checking later
        multiplePoints.put(strings.AppleShelf, new MyCoord((ScreenWidth/4) - 300,ScreenHeight/4));
        multiplePoints.put(strings.PearShelf, new MyCoord((ScreenWidth/3) - 100, (ScreenHeight/3)));
        multiplePoints.put(strings.FlowerShelf, new MyCoord(ScreenWidth/2, (ScreenHeight/2)));

        pause = new MyCoord(ScreenWidth/12,ScreenHeight/8);
        toUnpause = new MyCoord(ScreenWidth/4 + 550, ScreenHeight - 490);
        toRestart = new MyCoord(ScreenWidth/12 + 100, ScreenHeight/8);
        restartYes = new MyCoord(ScreenWidth/4 + 300, ScreenHeight - 200);

        //Wei Min - Coordinates for ui stuff, like dialogue box etc
        //Using screen width/height so it works on other phoness
        UIStuff.put(strings.DialogueBox, new MyCoord(ScreenWidth/4,ScreenHeight/3));
        UIStuff.put(strings.RemoveButton, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 100,UIStuff.get(strings.DialogueBox).getY() + 350));
        UIStuff.put(strings.CancelButton, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 600,UIStuff.get(strings.DialogueBox).getY() + 350));
        UIStuff.put(strings.Plus, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 150,UIStuff.get(strings.DialogueBox).getY() + 200));
        UIStuff.put(strings.Minus, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 550,UIStuff.get(strings.DialogueBox).getY()+ 200));
        UIStuff.put(strings.CartButton, new MyCoord(ScreenWidth-200,100));
        UIStuff.put(strings.AddButton, new MyCoord(ScreenWidth/3,ScreenHeight - 200));
        UIStuff.put(strings.CheckOutButton, new MyCoord(ScreenWidth/3,ScreenHeight - 200));
        UIStuff.put(strings.Result, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 100,UIStuff.get(strings.DialogueBox).getY() + 350));
        UIStuff.put(strings.Menu, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 600,UIStuff.get(strings.DialogueBox).getY() + 350));

        UIStuff.put(strings.ShoppingList, new MyCoord(ScreenWidth/4, ScreenHeight-ScreenHeight));

        x  =  UIStuff.get(strings.CartButton).getX();
        y = UIStuff.get(strings.ShoppingList).getY();

        //Star animation sprite shit
        //cash_anim = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.dollarbills)), (int)ScreenWidth/2, (int)ScreenHeight/4, true),320,64,5,7);
        playeravatar = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.avatar2)), (int)ScreenWidth/4, (int)ScreenHeight/4, true),320,64,5,4);
        cashier = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.cashier)), (int) ScreenWidth / 2, (int) ScreenHeight / 4, true), 320, 64, 5, 4);
        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);
        // Make the GamePanel focusable so it can handle events
        setFocusable(true);


        //week 13
        SharedPrefname = getContext().getSharedPreferences("Nameofplayer",Context.MODE_PRIVATE);
        editorN = SharedPrefname.edit();
        value = SharedPrefname.getString("Nameofplayer", "");

        SharePrefscore = getContext().getSharedPreferences("Highscore",Context.MODE_PRIVATE);
        editorS = SharePrefscore.edit();

        scorevalue = SharePrefscore.getString("Highscore", "");
        highscore = 100;

        //highscore = SharePrefscore.getInt("Highscore",0);

        toastmessage(context);
        //Week 14 accelerometer
        sensor = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor.registerListener(this,sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),SensorManager.SENSOR_DELAY_NORMAL);

        //Week 13 alerts
        AlertObj = new Alert(this);
        alert = new AlertDialog.Builder(getContext());
        final EditText input = new EditText(getContext());
        //After input, player can/not use Enter/Ok
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        //Define max char user can enter (EG to 6)
        int maxLength = 6;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);
        alert.setTitle("Game Over");
        alert.setMessage("Please enter your name");
        alert.setCancelable(false);
        alert.setIcon(R.drawable.shoppingicon);
        alert.setView(input);
        alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                playername = input.getText().toString();
                final String appendedValue;
                final String appendedScore;
                appendedValue = value + "|" + playername;
                editorN.putString("Nameofplayer",appendedValue);

                editorN.commit();

                appendedScore =  String.valueOf(scorevalue) + "|" + String.valueOf(timeElasped);
                editorS.putString("Highscore",appendedScore);
                editorS.commit();

                //Remove all shared pref value
                //editorS.remove("Highscore");
                //editorN.remove("Nameofplayer");
                //editorN.commit();
                //editorS.commit();
                Context context = getContext();
                context.startActivity(new Intent(context, Rank.class));
            }
        });
    }

    public void GoStage2(){
        clearstage = false;
        showremove = false;
        moving = false;
        playernear = false;
        showaddtocart= showcashier = false;
        numbertoremove = 1;
        movingsprite = false;
        moveout = movein = docheckout = false;
        movelistin = movelistout = false;
        touchingitem = "";
        addingwhat = "";
        clearstage = false;
        mX = ScreenWidth/5;
        mY = (ScreenHeight/2)+300;
        price = 0;
        slsum = 0;

        gameTimer = 1000;

        int lowest = 2;
        int highest = 6;
//      ANDY's Shopping list
//      rand shopping list
        for(int j = 0; j < itemList.length; j++) {
            int idx = random.nextInt(itemList.length);
            String randomItem = (itemList[idx]);

            int idx2 = random.nextInt(highest - lowest) + lowest;

            shoppinglist.addToList(randomItem, idx2);
            shoppinglist.addToList(randomItem, idx2);
            shoppinglist.addToList(randomItem, idx2);
        }

        temp.putAll(shoppinglist.myshoppinglist);

        //Get price of everything in shopping list to give player their budget
        for(Map.Entry<String,Integer>sl:shoppinglist.myshoppinglist.entrySet())
        {
            int extraBudget = random.nextInt(3-1) + 1;
            String key = sl.getKey();
            Integer value = sl.getValue();
            if (value > 0) {
                int priceofbudget = cart.prices.get(key);
                slsum += (priceofbudget * value) + extraBudget;
            }
        }

        int randAppleYLow = 100;
        int randAppleYHigh = 200;
        int result = random.nextInt(randAppleYHigh - randAppleYLow);

        //New Coordinates for the shelves
        //Add in the coordinates of the shelves for collision checking later
        multiplePoints.put(strings.AppleShelf, new MyCoord((ScreenWidth/4),ScreenHeight/4 + result));
        multiplePoints.put(strings.PearShelf, new MyCoord((ScreenWidth/3) - 300, (ScreenHeight/3 - 50)));
        multiplePoints.put(strings.FlowerShelf, new MyCoord(ScreenWidth/2, (ScreenHeight/2 - 50)));

        playeravatar.setX(mX);
        playeravatar.setY(mY);

    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
        soundmanager.PlayBGM();
    }

    //Wei Min check bitmap and finger touch collision
    public boolean clickOnBitmap(Bitmap myBitmap, MotionEvent event, MyCoord touched) {
        float xEnd = touched.getX() + myBitmap.getWidth();
        float yEnd = touched.getY() + myBitmap.getHeight();;

        if ((event.getX() >= touched.getX() && event.getX() <= xEnd)
                && (event.getY() >= touched.getY() && event.getY() <= yEnd) ) {
            int pixX = (int) (event.getX() - touched.getX());
            int pixY = (int) (event.getY() - touched.getY() );
            if (!(myBitmap.getPixel(pixX, pixY) == 0)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //Wei Min check bitmap and bitmap collision
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
    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);
            soundmanager.StopBGM();
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
        sensor.unregisterListener(this);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    //Andy Render with custom font and also different colors
    public void RenderTextOnScreen(Canvas canvas, String text, int posX, int posY, int textsize, int color)
    {
        Paint paint = new Paint();
        if (canvas != null && text.length() != 0)
        {
            Typeface tf =Typeface.createFromAsset(getContext().getAssets(),"fonts/did.otf");
            paint.setTypeface(tf);
            paint.setARGB(255,0,0,0);
            paint.setStrokeWidth(2);
            paint.setTextSize(textsize);
            paint.setColor(color);
            canvas.drawText(text,posX,posY,paint);
        }
    }
    public void RenderPause(Canvas canvas) {
        if (ispaused) {
            canvas.drawBitmap(pause2.getBitmap(), toUnpause.getX(), toUnpause.getY(), null);
            RenderTextOnScreen(canvas, "Game Paused!", UIStuff.get(strings.DialogueBox).getX() + 150, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);
        }
        else
        {
            canvas.drawBitmap(pause1.getBitmap(),pause.getX(),pause.getY(),null);
        }
        if(restartPressed){
            canvas.drawBitmap(pause2.getBitmap(), toUnpause.getX(), toUnpause.getY(), null);
            canvas.drawBitmap(yes.getBitmap(), restartYes.getX(), restartYes.getY(), null);
            RenderTextOnScreen(canvas, "Restart game?", UIStuff.get(strings.DialogueBox).getX() + 150, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);
        }
        else
        {
            canvas.drawBitmap(restart.getBitmap(), toRestart.getX(), toRestart.getY(), null);
        }
    }
    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);

        //Andy render animated sprite
//        cash_anim.draw(canvas);
//        cash_anim.setX(850);
//        cash_anim.setY(-10);

        cashier.draw(canvas);
        cashier.setX(ScreenWidth - 500);
        cashier.setY(100);
        //Andy render shopping list background
        canvas.drawBitmap(shoppingList, UIStuff.get(strings.ShoppingList).getX(), UIStuff.get(strings.ShoppingList).getY()-(shoppingListbutton.getHeight()*5), null);
        canvas.drawBitmap(shoppingListbutton, UIStuff.get(strings.ShoppingList).getX(), UIStuff.get(strings.ShoppingList).getY(), null);

        int offset = -300;
        //Andy render shopping list text
        for(Map.Entry<String,Integer> entry : shoppinglist.myshoppinglist.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > 0) {
                offset += 50;
                RenderTextOnScreen(canvas, key + " x " + value.toString(), UIStuff.get(strings.ShoppingList).getX() + 10, UIStuff.get(strings.ShoppingList).getY() + offset, 50, black);
            }
        }

        //Wei Min render the shelves and the items inside shelves
        for (Map.Entry<String,MyCoord> entry : multiplePoints.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            canvas.drawBitmap(shelf, multiplePoints.get(key).getX(),  multiplePoints.get(key).getY(), null);

            if(key == strings.AppleShelf)
            {
                canvas.drawBitmap(apples,  multiplePoints.get(key).getX(),  multiplePoints.get(key).getY() + 50, null);
            }
            if(key == strings.PearShelf)
            {
                canvas.drawBitmap(pearlogo,  multiplePoints.get(key).getX(),  multiplePoints.get(key).getY() + 50, null);
            }
            if(key == strings.FlowerShelf)
            {
                canvas.drawBitmap(flower, multiplePoints.get(strings.FlowerShelf).getX(), multiplePoints.get(strings.FlowerShelf).getY() + 50, null);
            }
        }

        //Andy render player on top of other bitmaps
        playeravatar.draw(canvas);
        playeravatar.setX(mX);
        playeravatar.setY(mY);

        //Wei Min render cart button and background
        canvas.drawBitmap(cartbutton, UIStuff.get(strings.CartButton).getX(), UIStuff.get(strings.CartButton).getY(), null);
        canvas.drawBitmap(cartbg, UIStuff.get(strings.CartButton).getX()+130, UIStuff.get(strings.CartButton).getY()-20, null);


        //Wei Min position of stuff in cart
        int textYpos = 150;
        int imageYpos = 50;
        int appleprice = 0;

        //Render shopping list string and also budget player has
        RenderTextOnScreen(canvas, "SHOPPING LIST", UIStuff.get(strings.ShoppingList).getX()+10, UIStuff.get(strings.ShoppingList).getY()+50, 40, red);
        RenderTextOnScreen(canvas, "Budget : $" + slsum , UIStuff.get(strings.ShoppingList).getX()+10, UIStuff.get(strings.ShoppingList).getY()- 10, 45,red);

        //Simple item discount?
        if(gameTimer <= 900 && gameTimer >= 800){
            cart.prices.put("Apples", 1);
            cart.prices.put("Pears", 2);
            cart.prices.put("Flowers", 0); //lol put $1 first
            RenderTextOnScreen(canvas, "Items On Sale!",450,700,40,blue);
        }
        else{
            cart.prices.put("Apples", 2);
            cart.prices.put("Pears", 3);
            cart.prices.put("Flowers", 1);
        }

        //Overall discount?
        if(gameTimer < 600 && gameTimer >= 500){
            //if(appleprice - randDiscount > appleprice) {
                appleprice -= randDiscount;
            //}
            RenderTextOnScreen(canvas, "Overall Discount!",450,700,40,purple);
        }


        //Rendering logo in cart when player add to cart, and also their values together with calculation for price inside cart
        for(Map.Entry<String, Integer> entry : cart.mycart.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > 0) {
                textYpos += 120;
                imageYpos += 130;
                UIStuff.put(key, new MyCoord(UIStuff.get(strings.CartButton).getX() + 180, imageYpos));
                MyCoord touchapple = UIStuff.get(key);
                RenderTextOnScreen(canvas, key + " x " + value.toString(), touchapple.getX()+ 200, textYpos, 40, white);

                if (key == "Apples") {

                    canvas.drawBitmap(applelogo, touchapple.getX(), touchapple.getY(), null);
                }
                if (key == "Pears") {
                    canvas.drawBitmap(pearlogo, touchapple.getX(), touchapple.getY(), null);
                }
                if (key == "Flowers") {
                    canvas.drawBitmap(flower, touchapple.getX(), touchapple.getY(), null);
                }
                int allprices = cart.prices.get(key);
                //Technically is price for everything in the cart
                appleprice += allprices * value;
            }
        }
        RenderTextOnScreen(canvas, "Price : $" + appleprice, UIStuff.get(strings.CartButton).getX() + 190, ScreenHeight - 100, 60,white);

        //Wei Min Showing the dialogue/pop up boxes with buttons - All trying to be relative to screen size
        //When player go near the shelves
        if(showaddtocart)
        {
            canvas.drawBitmap(addtocartbutton, UIStuff.get(strings.AddButton).getX(), UIStuff.get(strings.AddButton).getY(), null);
            RenderTextOnScreen(canvas, "Add " + addingwhat + " to cart, Cost:" + " $ " + price, UIStuff.get(strings.AddButton).getX() + 20, UIStuff.get(strings.AddButton).getY() + 50, 40,white);
        }
        //When player go near cashier
        if(showcashier)
        {
            canvas.drawBitmap(checkoutbutton, UIStuff.get(strings.CheckOutButton).getX(), UIStuff.get(strings.CheckOutButton).getY(), null);
            RenderTextOnScreen(canvas, "Check out", UIStuff.get(strings.CheckOutButton).getX() + 20, UIStuff.get(strings.CheckOutButton).getY() + 50, 40,white);
        }
        RenderTextOnScreen(canvas, "CART",UIStuff.get(strings.CartButton).getX()+30,170,50,white);
        //Show dialogue box when player press on button that shoes up when near cashier
        if(docheckout || clearstage || ispaused || restartPressed || gameTimer <= 0)
        {
            canvas.drawBitmap(removedialogue, UIStuff.get(strings.DialogueBox).getX(), UIStuff.get(strings.DialogueBox).getY(), null);
        }
        if(clearstage)
        {
            canvas.drawBitmap(button, UIStuff.get(strings.Menu).getX(), UIStuff.get(strings.Menu).getY(), null);
            RenderTextOnScreen(canvas, "Next", UIStuff.get(strings.Menu).getX() + 20, UIStuff.get(strings.Menu).getY() + 80, 60, red);
        }
        else if(gameTimer <= 0)
        {
            canvas.drawBitmap(button, UIStuff.get(strings.Menu).getX(), UIStuff.get(strings.Menu).getY(), null);
            RenderTextOnScreen(canvas, "You have ran out of time!", UIStuff.get(strings.DialogueBox).getX() + 80, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);
            RenderTextOnScreen(canvas, "Next", UIStuff.get(strings.Menu).getX() + 20, UIStuff.get(strings.Menu).getY() + 80, 60, red);
        }
        //Show player their result if they got the cart all right or wrong or out of budget
        if(docheckout) {
            if(!clearstage) {
                canvas.drawBitmap(button, UIStuff.get(strings.Result).getX(), UIStuff.get(strings.Result).getY(), null);
                RenderTextOnScreen(canvas, "Result", UIStuff.get(strings.Result).getX() + 10, UIStuff.get(strings.Result).getY() + 80, 40, red);
            }
            if (appleprice <= slsum) {
                if (compareItems()) {
                    //cart.mycart.clear();
                    if(clearstage)
                    {
                        RenderTextOnScreen(canvas, "You have cleared the stage!", UIStuff.get(strings.DialogueBox).getX() + 150, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);
                    }
                    else
                        RenderTextOnScreen(canvas, "You have bought everything!", UIStuff.get(strings.DialogueBox).getX() + 150, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);


                } else {
                    if(clearstage)
                    {
                        RenderTextOnScreen(canvas, "You have failed the stage!", UIStuff.get(strings.DialogueBox).getX() + 100, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);
                    }
                    else
                        RenderTextOnScreen(canvas, "You didn't buy everything...!", UIStuff.get(strings.DialogueBox).getX() + 50, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);

                }
            } else {
                if(clearstage)
                {
                    RenderTextOnScreen(canvas, "You can't even check out...", UIStuff.get(strings.DialogueBox).getX() + 200, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);
                }
                else
                    RenderTextOnScreen(canvas, "You are over budget!", UIStuff.get(strings.DialogueBox).getX() + 200, UIStuff.get(strings.DialogueBox).getY() + 200, 50, white);

            }
        }
        //Showing the remove dialogue box
        if(showremove) {
            canvas.drawBitmap(removedialogue, UIStuff.get(strings.DialogueBox).getX(), UIStuff.get(strings.DialogueBox).getY(), null);
            RenderTextOnScreen(canvas, "Remove from cart?" ,UIStuff.get(strings.DialogueBox).getX() + 200, UIStuff.get(strings.DialogueBox).getY() + 100,50,white);
            canvas.drawBitmap(button, UIStuff.get(strings.RemoveButton).getX(), UIStuff.get(strings.RemoveButton).getY(), null);
            canvas.drawBitmap(button, UIStuff.get(strings.CancelButton).getX(), UIStuff.get(strings.CancelButton).getY(), null);
            canvas.drawBitmap(plus, UIStuff.get(strings.Plus).getX(), UIStuff.get(strings.Plus).getY(), null);
            canvas.drawBitmap(minus, UIStuff.get(strings.Minus).getX(), UIStuff.get(strings.Minus).getY(), null);
            RenderTextOnScreen(canvas, "Yes" ,UIStuff.get(strings.RemoveButton).getX() + 50, UIStuff.get(strings.RemoveButton).getY() + 80,60,red);
            RenderTextOnScreen(canvas, "No" ,UIStuff.get(strings.CancelButton).getX() + 50, UIStuff.get(strings.CancelButton).getY() + 80,60,black);
            RenderTextOnScreen(canvas, numbertoremove.toString() ,UIStuff.get(strings.Plus).getX() + 250,UIStuff.get(strings.Plus).getY()+50,60,white);
        }

        RenderPause(canvas);

        // Render game timer on screen
        RenderTextOnScreen(canvas, "Time Left: " + gameTimer,150,100,30,red);

        // Bonus) To print FPS on the screen
        RenderTextOnScreen(canvas, "FPS: " + FPS,150,70,30,red);
    }

    //Update method to update the game play
    public void update(float dt, float fps) {
        FPS = fps;
        switch (GameState) {
            case 0:
            case 1:
            {
                duration = System.nanoTime()-startTime;
                timeElasped = duration/1000000000;
                if(gameTimer <= 0) {
                    gameTimer = 0;
                }
                if(gameTimer > 0) {
                    gameTimer -= 0.000000001 * duration / 1000000000;

                    //Andy update sprite animation
                    //cash_anim.update(System.currentTimeMillis());
                    if (movingsprite) {
                        playeravatar.update(System.currentTimeMillis());
                    }
                    if (!playernear) {
                        cashier.update(System.currentTimeMillis());
                    }
                    // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                    spaceshipIndex++;

                    if (spaceshipIndex > 3) {
                        spaceshipIndex = 0;
                    }
                    if (!moving)
                        movingsprite = false;

                    //Wei Min Show player the check out button
                    if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, cashier.getX(), cashier.getY(), playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2)) {
                        showcashier = true;
                        playernear = true;
                    } else {
                        showcashier = false;
                        playernear = false;
                    }
                    //Wei Min position of cart slider when pressed
                    if (moveout) {
                        if (x >= ScreenWidth - 700) {
                            x -= (dt * 300);
                            UIStuff.get(strings.CartButton).setX(x);
                        } else
                            moveout = false;
                    }
                    if (movein) {
                        if (x <= ScreenWidth - 200) {
                            x += (dt * 300);
                            UIStuff.get(strings.CartButton).setX(x);
                        } else
                            movein = false;
                    }
                    //Andy position of shopping list slider
                    if (movelistout) {
                        if (y <= 300) {
                            y += (dt * 200);
                            UIStuff.get(strings.ShoppingList).setY(y);
                        } else
                            movelistout = false;
                    }
                    if (movelistin) {
                        if (y > 50) {
                            y -= (dt * 200);
                            UIStuff.get(strings.ShoppingList).setY(y);
                        } else
                            movelistin = false;
                    }
                }
                break;
            }
            //Wei Min - goes to main menu after game state = 2
            case 2:

                break;
        }
    }

    //Wei Min Compare for when check out, if there are things that the player never buy
    public Boolean compareItems()
    {
        Map<String,Integer> result = new HashMap<String,Integer>();
        Set<Entry<String, Integer>> filter = cart.mycart.entrySet();

        for( Map.Entry<String,Integer> entry : temp.entrySet() ) //compare with temp map
        {
            if( !filter.contains( entry ))
            {
                result.put(entry.getKey(), entry.getValue());
                return false;
            }
        }

        return true;
    }


    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch(GameState)
        {
            case 0:
            case 1:
                RenderGameplay(canvas);
                break;
            case 2:
                //Frame is still only 11 and 20+ on my laptop when nothing is updating/rendering
                canvas.drawBitmap(leaving, bgX, bgY, null);
                RenderTextOnScreen(canvas, "FPS: " + FPS,150,70,60,white);
                RenderTextOnScreen(canvas, "Tap to continue",ScreenWidth/4,ScreenHeight - 200,100,white);
                break;
        }
    }
    public void toastmessage(Context context)
    {
        text = "hi";
        toastTime = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, text, toastTime);
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

                if(GameState == 2)
                {
                    Context context = this.getContext();
                    context.startActivity(new Intent(context, Mainmenu.class));
                    GameState = 3;
                }
                //only pause2.bitmap works pause1 doesnt even show up
                if(!ispaused && clickOnBitmap(pause2.getBitmap(),event,pause)) {
                    ispaused = true;
                    myThread.pause();
                }
                else if (ispaused && clickOnBitmap(pause1.getBitmap(),event,toUnpause)){
                    ispaused = false;
                    myThread.unPause();
                }

                if(!restartPressed && clickOnBitmap(restart.getBitmap(),event,toRestart)){
                    restartPressed = true;
                    myThread.pause();
                }
                else if(restartPressed && clickOnBitmap(pause1.getBitmap(),event,toUnpause)){
                    restartPressed = false;
                    myThread.unPause();
                }
                else if(restartPressed && clickOnBitmap(yes.getBitmap(),event,restartYes)){
                    restartPressed = false;
                    myThread.unPause();
                    GameState = 2;
                }
                //Andy player movement
                if (CheckCollision(mX, mY, playeravatar.getSpriteWidth(), playeravatar.getSpriteHeight(), X, Y, 0, 0))
                {
                    moving = true;
                }
                else
                {
                    moving = false;
                }
                //Wei Min all the adding to cart, removing from cart/button presses
                if(showaddtocart)
                {
                    if(clickOnBitmap(addtocartbutton,event,UIStuff.get(strings.AddButton))) {
                        v.vibrate(500);
                        soundmanager.SFX1(5,5);
                        cart.addToCart(addingwhat,1);
                        shoppinglist.checkoffList(addingwhat, 1);
                        toast.show();

                        break;
                    }
                }
                if(clickOnBitmap(shoppingListbutton,event,UIStuff.get(strings.ShoppingList))) {

                    if (UIStuff.get(strings.ShoppingList).getY() <= 100)
                    {
                        movelistout = true;
                    }
                    if (UIStuff.get(strings.ShoppingList).getY()>= 200) {
                        movelistin = true;
                    }
                }
                if(!showremove) {
                    if (UIStuff.get("Apples") != null) {
                        String index = "Apples";
                        Integer value = cart.mycart.get(index);
                        if (value > 0)
                            if(clickOnBitmap(applelogo,event,UIStuff.get(index)))
                            {
                                showremove = true;
                                touchingitem = index;
                            }
                    }
                    if (UIStuff.get("Flowers") != null) {
                        String index = "Flowers";
                        Integer value = cart.mycart.get(index);
                        if (value > 0)
                            if(clickOnBitmap(flower,event,UIStuff.get(index)))
                            {
                                showremove = true;
                                touchingitem = index;
                            }
                    }
                    if (UIStuff.get("Pears") != null) {
                        String index = "Pears";
                        Integer value = cart.mycart.get(index);
                        if (value > 0)
                            if(clickOnBitmap(pearlogo,event,UIStuff.get(index)))
                            {   showremove = true;
                                touchingitem = index;
                            }
                    }
                }
                if(showcashier)
                {
                    if(clickOnBitmap(button,event,UIStuff.get(strings.CheckOutButton)))
                    {
                        docheckout = true;
//                        showAlert = true;
//                        AlertObj.RunAlert();

                        if(UIStuff.get(strings.CartButton).getX() <= ScreenWidth - 700)
                            movein = true;
                    }
                }
                if(docheckout) {
                    if(clickOnBitmap(button,event,UIStuff.get(strings.Result)))
                    {
                        soundmanager.SFX3(5, 5);
                        clearstage = true;
                    }
                }
                if(clearstage || gameTimer <= 0) {
                    if(clickOnBitmap(button,event,UIStuff.get(strings.Menu)) && GameState == 0)
                    {
                        //AlertObj.RunAlert();
                        GameState = 1;
                        temp.clear();
                        for (Map.Entry<String,Integer> entry : cart.mycart.entrySet()) {
                            String key = entry.getKey();
                            Integer value = entry.getValue();
                            cart.removeFromCart(key, value);
                        }
                        GoStage2();
                    }
                    else if(clickOnBitmap(button,event,UIStuff.get(strings.Menu)) && GameState == 1)
                    {
                        showAlert = true;
                        if(showAlert) {
                            AlertObj.RunAlert();
                            showAlert = false;
                        }

                    }
                }
                if(showremove) {
                    Integer value = cart.mycart.get(touchingitem);
                    if(clickOnBitmap(button,event,UIStuff.get(strings.RemoveButton)))
                    {
                        cart.removeFromCart(touchingitem, numbertoremove);
                        showremove = false;
                        numbertoremove = 1;
                    }
                    if(clickOnBitmap(button,event,UIStuff.get(strings.CancelButton)))
                    {
                        showremove = false;
                        numbertoremove = 1;
                    }
                    if(clickOnBitmap(plus,event,UIStuff.get(strings.Plus)))
                    {
                        if(numbertoremove < value)
                            numbertoremove++;
                    }
                    if(clickOnBitmap(minus,event,UIStuff.get(strings.Minus)))
                    {
                        numbertoremove--;
                        if(numbertoremove == 0)
                            numbertoremove = value;
                    }
                }
                if(clickOnBitmap(button,event,UIStuff.get(strings.CartButton)) && !docheckout)
                {
                    if (UIStuff.get(strings.CartButton).getX() >= ScreenWidth - 200)
                        moveout = true;
                    if (UIStuff.get(strings.CartButton).getX() <= ScreenWidth - 500)
                        movein = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //Andy moving player
                if(moving == true)
                {
                    movingsprite = true;
                    mX = (short)(X - playeravatar.getSpriteWidth()/2);
                    mY = (short)(Y - playeravatar.getSpriteHeight()/2);
                }
                //Andy
                if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, playeravatar.getX(), playeravatar.getY(), playeravatar.getSpriteWidth(), playeravatar.getSpriteHeight())) {
                    Random rX = new Random();
                    Random rY = new Random();
//                    for(int idx = 20; idx < ScreenWidth - 20; idx++)
//                    {
//                        cash_anim.setX(rX.nextInt(idx));
//                        cash_anim.setY(rY.nextInt(idx));
//                    }
                }
                //Wei Min collision with shelves
                for (Map.Entry<String,MyCoord> entry : multiplePoints.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    MyCoord tempcoord = multiplePoints.get(key);

                    if(key == strings.AppleShelf) {
                        if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, tempcoord.getX(), tempcoord.getY(), shelf.getWidth() / 2, shelf.getHeight() / 2)) {
                            showaddtocart = true;
                            addingwhat = "Apples";
                            price = cart.prices.get(addingwhat);
                        }
                    }
                    else if(key == strings.PearShelf) {
                        if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, tempcoord.getX(), tempcoord.getY(), shelf.getWidth() / 2, shelf.getHeight() / 2)) {
                            showaddtocart = true;
                            addingwhat = "Pears";
                            price = cart.prices.get(addingwhat);

                        }
                    }
                    else if (key == strings.FlowerShelf)
                        if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, tempcoord.getX(), tempcoord.getY(), shelf.getWidth() / 2, shelf.getHeight() / 2)) {
                            showaddtocart = true;
                            addingwhat = "Flowers";
                            price = cart.prices.get(addingwhat);
                        }

                        //check cashier

                        else
                            addingwhat = "";

                }
                if(addingwhat == "")
                    showaddtocart = false;




                break;
        }

        return true;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        values = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void SensorMove()
    {
        float testX, testY;
        testX = mX + (values[1] * ((System.currentTimeMillis()-lastTime)/1000));
    }
}