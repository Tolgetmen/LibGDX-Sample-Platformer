package com.platformer.sample.handlers;

import com.platformer.sample.utils.Key;

import java.util.ArrayList;

public class MyInput {
    public static ArrayList<Key> keys;
    public static ArrayList<Key> pkeys;

    static {
        keys = new ArrayList<Key>();
        pkeys = new ArrayList<Key>();
    }

    public static void update() {
        for (Key key : keys) {
            pkeys.add(key);
        }
    }
}
















