package com.sidm.wm.mymgp2016;

/**
 * Created by 155208U on 11/21/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
import java.util.Map.Entry;
import java.util.Set;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering
    Cart cart = new Cart();
    HMStrings strings = new HMStrings();
    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;
    String addingwhat = "";
Boolean clearstage = false;
    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;

    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    Boolean moveout = false,movein = false, docheckout = false;
    Integer priceof = 0;
    String touchingitem = "";
    Vibrator v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    //ALL THE COLORS
    int black = getContext().getResources().getColor(R.color.Black);
    int white = getContext().getResources().getColor(R.color.White);
    int red = getContext().getResources().getColor(R.color.Red);

    int slsum = 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] spaceship = new Bitmap[4];
    private Bitmap[] cash = new Bitmap[7];
    //Variables for all other stuff
    private Bitmap shelf,apples,cartbutton, cartbg, removedialogue, button, addtocartbutton, shoppingList, leaving;
    private Bitmap plus,minus;
    private Bitmap applelogo, pearlogo, flower, checkoutbutton;

    //Set coordiate of button
    MyCoord buttonCord = new MyCoord(100,100);
    //Get x of button to move it later
    int x = 0;

    // 4b) Variable as an index to keep track of the spaceship images
    private short spaceshipIndex = 0;

    //Position of shelves
    Map<String,MyCoord> multiplePoints=new HashMap<String, MyCoord>();
    //Position of ui stuff like buttons/dialogue boxes
    Map<String,MyCoord> UIStuff=new HashMap<String, MyCoord>();

    Map<String,Integer> ShoppingList = new HashMap<String,Integer>();
    Map<Integer,Integer> Quantity = new HashMap<Integer, Integer>();

    String[] itemList = {"Apples", "Pears", "Flowers"};
    int[] randomQuantity = new int[3];

    private Random random = new Random();

    List<Map.Entry<String,Integer>> itemname = new ArrayList<Map.Entry<String, Integer>>(ShoppingList.entrySet());
    //List<Integer> valuesList = new ArrayList<Integer>(Quantity.values());

    //Check if is moving
    Boolean moving = false;
    Boolean playernear = false;

    Boolean movingsprite = false;
    //Star sprite Animation
    //private SpriteAnimation star_anim;
    private SpriteAnimation cash_anim;
    private SpriteAnimation playeravatar;
    private SpriteAnimation cashier;

    // For spaceship location
    int mX, mY;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;


    Boolean touching = false, showaddtocart = false, showcashier = false;
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
        shoppingList = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shoppinglist), ScreenWidth/4, ScreenHeight/4, true);

        mX = 800;
        mY = 800;

        ShoppingList.put("Apples", 1);
        ShoppingList.put("Pears", 2);
        for(Map.Entry<String,Integer>sl:ShoppingList.entrySet())
        {
            String key = sl.getKey();
            Integer value = sl.getValue();
            if (value > 0) {
                int priceofbudget = cart.prices.get(key);
                slsum += priceofbudget * value;
            }


        }

        Collections.shuffle(itemname);

//        Quantity.put(1, 1);
//        Quantity.put(2, 2);
//        Quantity.put(3, 3);
//        Quantity.put(4, 4);
//        Quantity.put(5, 5);


        int lowest = 1;
        int highest = 4;

        Collections.shuffle(Arrays.asList(itemList));

        for(int i = 0; i < randomQuantity.length; i++)
        {
            int n = random.nextInt(highest - lowest) + lowest;
            randomQuantity[i] = n;
        }

        //Add in the coordinates of the shelves for collision checking later
        multiplePoints.put(strings.AppleShelf, new MyCoord((ScreenWidth/4) - 300,ScreenHeight/4));
        multiplePoints.put(strings.PearShelf, new MyCoord((ScreenWidth/3) - 100, (ScreenHeight/3)));
        multiplePoints.put(strings.FlowerShelf, new MyCoord(ScreenWidth/2, (ScreenHeight/2)));

        multiplePoints.put("shoppinglist", new MyCoord(490, 0));

        UIStuff.put(strings.DialogueBox, new MyCoord(ScreenWidth/4,ScreenHeight - 1200));
        UIStuff.put(strings.RemoveButton, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 200,UIStuff.get(strings.DialogueBox).getX() + 150));
        UIStuff.put(strings.CancelButton, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 700,UIStuff.get(strings.DialogueBox).getX() + 150));
        UIStuff.put(strings.Plus, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 200,UIStuff.get(strings.DialogueBox).getX() -100));
        UIStuff.put(strings.Minus, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 700,UIStuff.get(strings.DialogueBox).getX() -100));
        UIStuff.put(strings.CartButton, new MyCoord(100,100));
        UIStuff.put(strings.AddButton, new MyCoord(ScreenWidth/3,ScreenHeight - 200));
        UIStuff.put(strings.CheckOutButton, new MyCoord(ScreenWidth/3,ScreenHeight - 200));
        UIStuff.put(strings.Result, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 200,UIStuff.get(strings.DialogueBox).getX() + 150));
        UIStuff.put(strings.Menu, new MyCoord(UIStuff.get(strings.DialogueBox).getX() + 700,UIStuff.get(strings.DialogueBox).getX() + 150));

        UIStuff.get(strings.CartButton).setX(ScreenWidth-200);
        x  =  UIStuff.get(strings.CartButton).getX();

        //Star animation sprite shit
        cash_anim = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.dollarbills)), (int)ScreenWidth/2, (int)ScreenHeight/4, true),320,64,5,7);
        playeravatar = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(),R.drawable.avatar2)), (int)ScreenWidth/4, (int)ScreenHeight/4, true),320,64,5,4);
        cashier = new SpriteAnimation(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.cashier)), (int) ScreenWidth / 2, (int) ScreenHeight / 4, true), 320, 64, 5, 4);
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
            Typeface tf =Typeface.createFromAsset(getContext().getAssets(),"fonts/did.otf");
            paint.setTypeface(tf);
            paint.setARGB(255,0,0,0);
            paint.setStrokeWidth(2);
            paint.setTextSize(textsize);
            paint.setColor(color);
            canvas.drawText(text,posX,posY,paint);
        }
    }

    public String getRandomList(List<String> list)
    {
        int index = random.nextInt(list.size());
        System.out.println("\nIndex: " + index);
        return list.get(index);
    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);

        cash_anim.draw(canvas);
        cash_anim.setX(850);
        cash_anim.setY(-10);

        cashier.draw(canvas);
        cashier.setX(ScreenWidth - 500);
        cashier.setY(100);

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

        canvas.drawBitmap(cartbutton, UIStuff.get(strings.CartButton).getX(), UIStuff.get(strings.CartButton).getY(), null);
        canvas.drawBitmap(cartbg, UIStuff.get(strings.CartButton).getX()+130, UIStuff.get(strings.CartButton).getY()-20, null);

        MyCoord shoppinglist = multiplePoints.get(("shoppinglist"));
        if (shoppinglist != null) {
            canvas.drawBitmap(shoppingList, shoppinglist.getX(), shoppinglist.getY(), null);
        }
        int offset = 30;


        int i = cart.GetCartSize();
        RenderTextOnScreen(canvas, "Size: " + i,150,100,30,black);
        playeravatar.draw(canvas);
        playeravatar.setX(mX);
        playeravatar.setY(mY);

        int textYpos = 200;
        int imageYpos = 50;
        int appleprice = 0;

        int ShoppingListplacement = 100;

        for(Map.Entry<String, Integer> entry : ShoppingList.entrySet()) {

            String key = entry.getKey();
            Integer value = entry.getValue();
            ShoppingListplacement += 50;
            RenderTextOnScreen(canvas, key + "x  " + value.toString(), 600, ShoppingListplacement, 30, black);
        }
        RenderTextOnScreen(canvas, "SHOPPING LIST", 550, 80, 50, red);
        RenderTextOnScreen(canvas, "Budget : $" + slsum , 550, 300, 60,red);


        for(Map.Entry<String, Integer> entry : cart.mycart.entrySet()) {
            System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > 0) {
                textYpos += 100;
                imageYpos += 130;
                RenderTextOnScreen(canvas, key + " " + value.toString(), UIStuff.get(strings.CartButton).getX() + 430, textYpos, 50, white);
                UIStuff.put(key, new MyCoord(UIStuff.get(strings.CartButton).getX() + 180, imageYpos));
                MyCoord touchapple = UIStuff.get(key);

                if (key == "Apples") {

                    canvas.drawBitmap(applelogo, touchapple.getX(), touchapple.getY(), null);
                }
                if (key == "Pears") {
                    canvas.drawBitmap(pearlogo, touchapple.getX(), touchapple.getY(), null);
                }
                if (key == "Flowers") {
                    canvas.drawBitmap(flower, touchapple.getX(), touchapple.getY(), null);
                }
                int priceofapple = cart.prices.get(key);
                appleprice += priceofapple * value;

            } else {
                textYpos -= 20;
                imageYpos -= 20;
            }
        }

        RenderTextOnScreen(canvas, "Price : $" + appleprice, UIStuff.get(strings.CartButton).getX() + 190, ScreenHeight - 100, 60,white);
        // 4d) Draw the spaceships
        //canvas.drawBitmap(spaceship[spaceshipIndex], mX, mY, null);
        if(showaddtocart)
        {
            canvas.drawBitmap(addtocartbutton, UIStuff.get(strings.AddButton).getX(), UIStuff.get(strings.AddButton).getY(), null);
            RenderTextOnScreen(canvas, "Add " + addingwhat + " to cart", UIStuff.get(strings.AddButton).getX() + 20, UIStuff.get(strings.AddButton).getY() + 50, 40,white);
        }
        if(showcashier)
        {
            canvas.drawBitmap(checkoutbutton, UIStuff.get(strings.CheckOutButton).getX(), UIStuff.get(strings.CheckOutButton).getY(), null);
            RenderTextOnScreen(canvas, "Check out", UIStuff.get(strings.CheckOutButton).getX() + 20, UIStuff.get(strings.CheckOutButton).getY() + 50, 40,white);
        }
        RenderTextOnScreen(canvas, "CART",UIStuff.get(strings.CartButton).getX()+30,170,50,white);
        if(docheckout || clearstage)
        {
            canvas.drawBitmap(removedialogue, UIStuff.get(strings.DialogueBox).getX(), UIStuff.get(strings.DialogueBox).getY(), null);
        }
        if(clearstage)
        {
            canvas.drawBitmap(button, UIStuff.get(strings.Menu).getX(), UIStuff.get(strings.Menu).getY(), null);
            RenderTextOnScreen(canvas, "Next", UIStuff.get(strings.Menu).getX() + 20, UIStuff.get(strings.Menu).getY() + 80, 60, red);
        }
        if(docheckout) {
            if(!clearstage) {
                canvas.drawBitmap(button, UIStuff.get(strings.Result).getX(), UIStuff.get(strings.Result).getY(), null);
                RenderTextOnScreen(canvas, "Result", UIStuff.get(strings.Result).getX() + 10, UIStuff.get(strings.Result).getY() + 80, 40, red);
            }
                if (appleprice <= slsum) {
                    if (compareItems()) {
                        System.out.print("Yay all correct");

                        //cart.mycart.clear();
                        if(clearstage)
                        {
                            RenderTextOnScreen(canvas, "You have cleared the stage!", UIStuff.get(strings.DialogueBox).getX() + 30, 500, 50, white);
                        }
                        else
                        RenderTextOnScreen(canvas, "Yay all correct!", UIStuff.get(strings.DialogueBox).getX() + 200, 500, 50, white);


                    } else {
                        System.out.print("Wrong");
                        if(clearstage)
                        {
                            RenderTextOnScreen(canvas, "You have failed the stage!", UIStuff.get(strings.DialogueBox).getX() + 100, 500, 50, white);
                        }
                        else
                        RenderTextOnScreen(canvas, "You didn't buy everything...!", UIStuff.get(strings.DialogueBox).getX() + 50, 500, 50, white);

                    }
                } else {
                    if(clearstage)
                    {
                        RenderTextOnScreen(canvas, "You can't even check out...", UIStuff.get(strings.DialogueBox).getX() + 200, 500, 50, white);
                    }
                    else
                    RenderTextOnScreen(canvas, "You are over budget!", UIStuff.get(strings.DialogueBox).getX() + 200, 500, 50, white);

                }
        }

        if(showremove) {
            canvas.drawBitmap(removedialogue, UIStuff.get(strings.DialogueBox).getX(), UIStuff.get(strings.DialogueBox).getY(), null);
            RenderTextOnScreen(canvas, "Remove from cart?" ,UIStuff.get(strings.DialogueBox).getX() + 200, UIStuff.get(strings.DialogueBox).getY() + 100,50,white);
            canvas.drawBitmap(button, UIStuff.get(strings.RemoveButton).getX(), UIStuff.get(strings.RemoveButton).getY(), null);
            canvas.drawBitmap(button, UIStuff.get(strings.CancelButton).getX(), UIStuff.get(strings.CancelButton).getY(), null);
            canvas.drawBitmap(plus, UIStuff.get(strings.Plus).getX(), UIStuff.get(strings.Plus).getY(), null);
            canvas.drawBitmap(minus, UIStuff.get(strings.Minus).getX(), UIStuff.get(strings.Minus).getY(), null);
            RenderTextOnScreen(canvas, "Yes" ,UIStuff.get(strings.RemoveButton).getX() + 50, UIStuff.get(strings.RemoveButton).getY() + 80,60,red);
            RenderTextOnScreen(canvas, "No" ,UIStuff.get(strings.CancelButton).getX() + 50, UIStuff.get(strings.CancelButton).getY() + 80,60,black);
            RenderTextOnScreen(canvas, numbertoremove.toString() ,UIStuff.get(strings.Plus).getX() + 350,UIStuff.get(strings.Plus).getY()+50,60,white);
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
    public void update(float dt, float fps) {
        FPS = fps;
        switch (GameState) {
            case 0: {
                cash_anim.update(System.currentTimeMillis());
                if (movingsprite) {
                    playeravatar.update(System.currentTimeMillis());
                }
                if (!playernear) {
                    cashier.update(System.currentTimeMillis());
                }
                // 3) Update the background to allow panning effect

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                spaceshipIndex++;

                if (spaceshipIndex > 3) {
                    spaceshipIndex = 0;
                }
                if (!moving)
                    movingsprite = false;

                if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, cashier.getX(), cashier.getY(), playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2)) {
                    showcashier = true;
                    playernear = true;
                }
                else {
                    showcashier = false;
                    playernear = false;
                }
                //Slider for cart list at right side
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

                break;
            }
            case 1:
                Context context = this.getContext();
                context.startActivity(new Intent(context, Mainmenu.class));
                break;
        }
    }

    public Boolean compareItems()
    {
        Map<String,Integer> result = new HashMap<String,Integer>();
        Set<Entry<String, Integer>> filter = cart.mycart.entrySet();

        for( Map.Entry<String,Integer> entry : ShoppingList.entrySet() )
        {
            if( !filter.contains( entry ))
            {
                result.put(entry.getKey(), entry.getValue());
                return false;
            }
        }
        for(String s2 : result.keySet())
        {
            System.out.println(" The values of the String are" + result.keySet());
            //System.out.println(" The values of the String are" + result.values());

        }
        return true;
    }


    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
            case 1:
                canvas.drawBitmap(leaving, bgX, bgY, null);
                canvas.drawBitmap(leaving, bgX + ScreenWidth, bgY, null);
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
          if (CheckCollision(mX, mY, playeravatar.getSpriteWidth(), playeravatar.getSpriteHeight(), X, Y, 0, 0))
            {
                moving = true;
            }
            else
            {
                moving = false;
            }
                    if(showaddtocart)
            {
                if(clickOnBitmap(addtocartbutton,event,UIStuff.get(strings.AddButton))) {
                    v.vibrate(500);
                    cart.addToCart(addingwhat,1);
                    break;
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
                    if(UIStuff.get(strings.CartButton).getX() <= ScreenWidth - 700)
                        movein = true;
                }
            }
                if(docheckout) {
                    if(clickOnBitmap(button,event,UIStuff.get(strings.Result)))
                    {
                        clearstage = true;
                    }
                }
                if(clearstage) {
                    if(clickOnBitmap(button,event,UIStuff.get(strings.Menu)))
                    {
                        GameState = 1;
                        //clearstage = false;
                        //docheckout = false;
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
                if(moving == true)
                {
                    movingsprite = true;
                    mX = (short)(X - playeravatar.getSpriteWidth()/2);
                    mY = (short)(Y - playeravatar.getSpriteHeight()/2);
                }
                if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, playeravatar.getX(), playeravatar.getY(), playeravatar.getSpriteWidth(), playeravatar.getSpriteHeight())) {
                            Random rX = new Random();
                            Random rY = new Random();
                            for(int idx = 20; idx < ScreenWidth - 20; idx++)
                    {
                        cash_anim.setX(rX.nextInt(idx));
                        cash_anim.setY(rY.nextInt(idx));
                    }
                }
                for (Map.Entry<String,MyCoord> entry : multiplePoints.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    MyCoord tempcoord = multiplePoints.get(key);

                    if(key == strings.AppleShelf) {
                        if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, tempcoord.getX(), tempcoord.getY(), shelf.getWidth() / 2, shelf.getHeight() / 2)) {
                            showaddtocart = true;
                            addingwhat = "Apples";
                        }
                    }
                    else if(key == strings.PearShelf) {
                        if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, tempcoord.getX(), tempcoord.getY(), shelf.getWidth() / 2, shelf.getHeight() / 2)) {
                            showaddtocart = true;
                            addingwhat = "Pears";
                        }
                    }
                    else if (key == strings.FlowerShelf)
                        if (CheckCollision(mX, mY, playeravatar.getSpriteWidth() / 2, playeravatar.getSpriteHeight() / 2, tempcoord.getX(), tempcoord.getY(), shelf.getWidth() / 2, shelf.getHeight() / 2)) {
                            showaddtocart = true;
                            addingwhat = "Flowers";
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
}