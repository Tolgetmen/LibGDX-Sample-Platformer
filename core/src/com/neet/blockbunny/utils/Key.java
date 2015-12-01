package com.neet.blockbunny.utils;

/**
 * Created by tomer on 12/1/15.
 */
public class Key {
    private int trigger;
    private boolean isPressed, isDown;
    private int action;
    private String actionName = "";
    public boolean keyChanged = false;

    public Key(int action, int trigger, String actionName) {
        this.action = action;
        this.trigger = trigger;
        this.actionName = actionName;
    }

    public String getActionName() {
        return this.actionName;
    }

    public int getTrigger() {
        return trigger;
    }

    public int getAction() {
        return action;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setPressed(boolean pressed) {
        keyChanged = pressed != isPressed;
        isPressed = pressed;
    }

    public void setDown(boolean down) {
        isDown = down;
    }
}
