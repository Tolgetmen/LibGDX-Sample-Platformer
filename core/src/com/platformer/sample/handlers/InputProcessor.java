package com.platformer.sample.handlers;

import com.badlogic.gdx.InputAdapter;
import com.platformer.sample.utils.Key;
import com.platformer.sample.utils.Res;

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
