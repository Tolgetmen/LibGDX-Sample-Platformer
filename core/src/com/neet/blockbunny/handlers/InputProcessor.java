package com.neet.blockbunny.handlers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.neet.blockbunny.utils.Key;
import com.neet.blockbunny.utils.Res;

public class InputProcessor extends InputAdapter {

    public InputProcessor() {
        Res.updateKeys();
    }

    public boolean keyDown(int k) {
        for (Key key : Res.getKeys()) {
            if (k == key.getTrigger()) {
                System.out.println("Key " + k);
                key.setPressed(true);
                return true;
            }
        }
        return true;
    }

    public boolean keyUp(int k) {
        for (Key key : Res.getKeys()) {
            if (k == key.getTrigger()) {
                key.setPressed(false);
                return true;
            }
        }
        return true;
    }

}
