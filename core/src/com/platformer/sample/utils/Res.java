package com.platformer.sample.utils;

import com.badlogic.gdx.Input;

import java.util.ArrayList;

/**
 * Created by tomer on 12/1/15.
 */
public class Res {

    public static final int JUMP = 1, RIGHT = 2, LEFT = 3, SHIFT = 4;
    public static final float JUMP_POWER = 200;

    private static ArrayList<Key> keys = new ArrayList<Key>();

    public static Key getKey(int index) {
        return keys.get(index);
    }

    public static void updateKeys() {
        keys.add(new Key(0, Input.Keys.K, "Nothing"));
        keys.add(new Key(JUMP, Input.Keys.UP, "Jump"));
        keys.add(new Key(RIGHT, Input.Keys.RIGHT, "Right"));
        keys.add(new Key(LEFT, Input.Keys.LEFT, "Left"));
        keys.add(new Key(SHIFT, Input.Keys.SHIFT_LEFT, "Sprint"));
    }

    public static ArrayList<Key> getKeys() {
        return keys;
    }

    public static Key getKeyByAction(int action) {
        for (Key key : keys) {
            if (key.getAction() == (action)) {
                return key;
            }
        }
        return keys.get(0);
    }
}
