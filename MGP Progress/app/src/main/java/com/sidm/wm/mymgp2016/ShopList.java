package com.sidm.wm.mymgp2016;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Andy on 10/1/2017.
 */

public class ShopList {
    Map<String,Integer> myshoppinglist;
    ShopList()
    {
        myshoppinglist = new HashMap<String,Integer>();
    }

    public void addToList(String toAdd, Integer add)
    {
        String index = toAdd;

//        if(myshoppinglist.get(index) != null)
//        {
//            Integer value = myshoppinglist.get(index);
//            myshoppinglist.put(index, value + add);
//        }
//        else
//        {
            myshoppinglist.put(toAdd,add);
        //}
    }

    public void checkoffList(String toCheckOff, Integer CheckOff)
    {
        String index = toCheckOff;

        if(myshoppinglist.get(index) != null)
        {
            Integer value = myshoppinglist.get(index);
            if(value > 0)
            myshoppinglist.put(index, value - CheckOff);
        }
    }
}
