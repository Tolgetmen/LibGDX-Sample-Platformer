package com.platformer.sample.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.sample.main.Game;
import com.platformer.sample.utils.Res;

public class Player extends B2DSprite {

    private int numCrystals;
    private int totalCrystals;
    public int jumpsLeft = 2;
    public boolean sprint;

    public Player(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("bunny");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }

    public void runLeft(boolean run) {
        int speed = run ? -1 : 0;
        if(sprint) speed = speed*2;
        if (getBody().getLinearVelocity().x <= 0 && speed != getBody().getLinearVelocity().x)
            getBody().setLinearVelocity(speed, getBody().getLinearVelocity().y);
    }

    public void runRight(boolean run) {
        int speed = run ? 1 : 0;
        if(sprint) speed = speed*2;
        if (getBody().getLinearVelocity().x >= 0 && speed != getBody().getLinearVelocity().x)
            getBody().setLinearVelocity(speed, getBody().getLinearVelocity().y);
    }

    public void collectCrystal() {
        numCrystals++;
    }

    public int getNumCrystals() {
        return numCrystals;
    }

    public void setTotalCrystals(int i) {
        totalCrystals = i;
    }

    public int getTotalCyrstals() {
        return totalCrystals;
    }

    public void jump() {
        jumpsLeft --;
        getBody().applyForceToCenter(0, Res.JUMP_POWER, true);
    }
}










