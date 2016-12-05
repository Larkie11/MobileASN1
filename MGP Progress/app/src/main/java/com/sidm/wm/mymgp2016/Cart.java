package com.sidm.wm.mymgp2016;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WM on 3/12/2016.
 */

public class Cart {
    //My cart
    Map<String,Integer> mycart;
    //The price list
    Map<String,Integer> prices;

    Cart()
    {
        mycart = new HashMap<String,Integer>();
        prices=new HashMap<String, Integer>();
        //Init the prices of items here
        prices.put("Apples", 2);
        prices.put("Pears", 3);
        prices.put("Flowers", 1);
    }

    public void addToCart(String toAdd, Integer add)
    {
        String index = toAdd;

        if(mycart.get(index) != null)
        {
            Integer value = mycart.get(index);
            mycart.put(index, value + add);
        }
        else
        {
            mycart.put(toAdd,add);
        }
    }
    public void removeFromCart(String toRemove, Integer remove)
    {
        String index = toRemove;

        if(mycart.get(index) != null) {
            Integer value = mycart.get(index);
            if (value > 0)
                mycart.put(index, value - remove);
        }
    }
    public int GetCartSize()
    {
        return mycart.size();
    }
}


