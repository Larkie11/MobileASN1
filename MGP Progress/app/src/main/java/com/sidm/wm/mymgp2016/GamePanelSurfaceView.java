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

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] spaceship = new Bitmap[4];
    private Bitmap[] cash = new Bitmap[7];
    //Variables for all other stuff
    private Bitmap shelf,apples,cartbutton, cartbg, removedialogue, button, addtocartbutton, shoppingList;
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
        ShoppingList.put("Flowers", 3);

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
        multiplePoints.put(strings.AppleShelf, new MyCoord(200, 250));
        multiplePoints.put(strings.PearShelf, new MyCoord(800, 300));
        multiplePoints.put(strings.FlowerShelf, new MyCoord(450, 550));

        multiplePoints.put("shoppinglist", new MyCoord(490, 0));

        UIStuff.put(strings.DialogueBox, new MyCoord(500,280));
        UIStuff.put(strings.RemoveButton, new MyCoord(600,650));
        UIStuff.put(strings.CancelButton, new MyCoord(1100,650));
        UIStuff.put(strings.Plus, new MyCoord(1000,450));
        UIStuff.put(strings.Minus, new MyCoord(700,450));
        UIStuff.put(strings.CartButton, new MyCoord(100,100));
        UIStuff.put(strings.AddButton, new MyCoord(700,900));
        UIStuff.put(strings.CheckOutButton, new MyCoord(700,900));

        UIStuff.get(strings.CartButton).setX(ScreenWidth-200);
        x  =  UIStuff.get(strings.CartButton).getX();

        // 4c) Load the images of the spaceships

        //spaceship[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
        //spaceship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        //spaceship[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
        //spaceship[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);
        //student to scaled your spaceship base on scaledbg xxxx
//        spaceship[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart), ScreenWidth/10, ScreenHeight/6, true);
//        spaceship[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart1), ScreenWidth/10, ScreenHeight/6, true);
//        spaceship[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart2), ScreenWidth/10, ScreenHeight/6, true);
//        spaceship[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart3), ScreenWidth/10, ScreenHeight/6, true);

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

        playeravatar.draw(canvas);
        playeravatar.setX(mX);
        playeravatar.setY(mY);

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

//        for(Map.Entry<String,Integer> entry : ShoppingList.entrySet())
//        {
//
//            String key = entry.getKey();
//            RenderTextOnScreen(canvas, key, 500, 40 + offset, 30, black);
//        }


//    //String itemname = ShoppingList.values().toString();
//    for (Map.Entry<Integer, String> entry : ShoppingList.entrySet()) {
//        //Integer key = entry.getKey();
//        String value = entry.getValue();
//        for(int j = 0; j < 5; j++)
//            RenderTextOnScreen(canvas, value, 500, 35 + (j * offset), 30, black);
//    }
//    List<Integer> valuesList = new ArrayList<Integer>(Quantity.values());
////        int randomIndex = new Random().nextInt(valuesList.size());
////        Integer randomValue = valuesList.get(randomIndex);
//    Collections.shuffle(valuesList);
//    for(int k = 0; k < 5; k++)
//    {
//        RenderTextOnScreen(canvas, "X " + valuesList.toString(), 650, 35 + (k * offset), 30, black);
//    }



        for(int j = 0; j < randomQuantity.length; j++)
        {
            RenderTextOnScreen(canvas, "X " + randomQuantity[j], 700, 40 + (j * offset), 35, black);
        }

        for(int k = 0; k < itemList.length; k++)
        {
            RenderTextOnScreen(canvas, itemList[k], 500, 40 + (k * offset), 35, black);
        }

        int i = cart.GetCartSize();
        RenderTextOnScreen(canvas, "Size: " + i,150,100,30,black);

        int textYpos = 100;
        int imageYpos = 50;

        int appleprice = 0;
        int pearprice = 0;
        int flowerprice = 0;


        int budget = 0;
        int applebudgetinit = 0;
        int pearbudgetinit = 0;
        int flowerbudgetinit = 0;
        for(Map.Entry<String, Integer> entry : cart.mycart.entrySet()) {
            System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > 0) {
                textYpos += 100;
                imageYpos += 100;
                RenderTextOnScreen(canvas, key + " " + value.toString(), UIStuff.get(strings.CartButton).getX() + 330, textYpos, 30, white);
                UIStuff.put(key, new MyCoord(UIStuff.get(strings.CartButton).getX() + 180, imageYpos));
                MyCoord touchapple = UIStuff.get(key);

                if (key == "Apples") {
                    //array[0] = (valueofitem * values);
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
                int priceofpear = cart.prices.get(key);
                pearprice += priceofpear * value;
                int priceoflower = cart.prices.get(key);
                flowerprice += priceoflower * value;

                for( Map.Entry<String,Integer> entry2 : ShoppingList.entrySet() )
                {
                    applebudgetinit += priceofapple * entry2.getValue();
                    pearbudgetinit += priceofpear * entry2.getValue();
                    flowerbudgetinit += priceoflower * entry2.getValue();
                    int priceOffset = random.nextInt(5 - 1) + 1;
                    budget += applebudgetinit + pearbudgetinit + flowerbudgetinit + priceOffset;
                }

            }
            else {
                textYpos -= 20;
                imageYpos -= 20;
            }

        }

        RenderTextOnScreen(canvas, "Budget: $" + budget, 80, 150, 40, white);
        RenderTextOnScreen(canvas, "Price : " + appleprice, UIStuff.get(strings.CartButton).getX() + 180, 1030, 60,white);



        //System.out.println("Sum: " + priceof.toString());
        //RenderTextOnScreen(canvas, "Price" + sum, buttonCord.getX() + 230, textYpos+500, 60);
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
        if(docheckout) {
            if (compareItems()) {
                System.out.print("Yay all correct");
                RenderTextOnScreen(canvas, "Yay all correct!", 500, 500, 50, black);

            } else {
                System.out.print("Wrong");
                RenderTextOnScreen(canvas, "Nope...!", 500, 500, 50, black);
            }
            docheckout = false;
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
            RenderTextOnScreen(canvas, numbertoremove.toString() ,UIStuff.get(strings.Plus).getX() -100,UIStuff.get(strings.Plus).getY()+50,60,white);
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
                } else {
                    showcashier = false;
                    playernear = false;
                }
                //Slider for cart list at right side
                if (moveout) {
                    if (x >= 1300) {
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

                    //DO THE COMPARING HERE
                    //COMPARE BETWEEN ALL INSIDE CART AND LIST
                    //IF LIST HAVE EXTRA THING MEANS PLAYER FAIL, NO SCORE
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
                if(clickOnBitmap(button,event,UIStuff.get(strings.CartButton)))
                {
                    if (UIStuff.get(strings.CartButton).getX() >= ScreenWidth - 200)
                        moveout = true;
                    if (UIStuff.get(strings.CartButton).getX() <= 1300)
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