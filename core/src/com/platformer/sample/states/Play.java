package com.platformer.sample.states;

import static com.platformer.sample.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.sample.entities.Player;
import com.platformer.sample.handlers.B2DVars;
import com.platformer.sample.handlers.GameStateManager;
import com.platformer.sample.handlers.contactListener;
import com.platformer.sample.main.Game;
import com.platformer.sample.utils.Key;
import com.platformer.sample.utils.Res;

import javax.microedition.khronos.opengles.GL10;

public class Play extends GameState {

    private boolean debug;
    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;
    private contactListener cl;

    private TiledMap tileMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;

    public Play(GameStateManager gsm) {

        super(gsm);

        // set up box2d stuff
        world = new World(new Vector2(0, -9.81f), true);
        cl = new contactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // create player
        createPlayer();

        // create tiles
        createTiles();

        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

    }

    public void handleInput() {
        for (Key key : Res.getKeys()) {
            if (key.isPressed()) {
                System.out.println(key.getActionName());
                switch (key.getAction()) {
                    case Res.JUMP:
                        if (cl.isPlayerOnGround()) {
                            player.jumpsLeft = 2;
                        }
                        if (player.jumpsLeft > 0) {
                            player.jump();
                            key.setPressed(false);
                        }
                        break;
                }
            }
            //if (key.keyChanged) {
            switch (key.getAction()) {
                case Res.SHIFT:
                    player.sprint = key.isPressed();
                    break;
                case Res.RIGHT:
                    player.runRight(key.isPressed());
                    break;
                case Res.LEFT:
                    player.runLeft(key.isPressed());
                    break;
            }
            //key.keyChanged = false;
            //System.out.println("Changed");
            // }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            restartGame();
        }
    }

    private void restartGame() {
        player.getBody().setTransform(160 / PPM, 200 / PPM, 0);
    }

    public void update(float dt) {

        handleInput();

        world.step(dt, 6, 2);

        player.update(dt);

    }

    public void render() {

        // clear screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // draw tile map
        tmr.setView(cam);
        tmr.render();
        cam.position.set(player.getPosition().x * PPM + Game.V_WIDTH / 4, cam.position.y, 0);
        cam.update();
        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        if (debug) {
            // draw box2d world
            b2dr.render(world, b2dCam.combined);
        }


    }

    public void dispose() {
    }

    private void createPlayer() {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // create player
        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyType.DynamicBody;
        bdef.linearVelocity.set(0, 0);
        Body body = world.createBody(bdef);

        shape.setAsBox(13 / PPM, 13 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED;
        body.createFixture(fdef).setUserData("player");

        // create foot sensor
        shape.setAsBox(13 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        // create player
        player = new Player(body);

        body.setUserData(player);

    }

    private void createTiles() {

        // load tile map
        tileMap = new TmxMapLoader().load("android/assets/res/maps/test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        tileSize = Integer.valueOf(tileMap.getProperties().get("tilewidth").toString());

        TiledMapTileLayer layer;

        layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
        createLayer(layer, B2DVars.BIT_RED);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
        createLayer(layer, B2DVars.BIT_RED);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
        createLayer(layer, B2DVars.BIT_RED);

    }

    private void createLayer(TiledMapTileLayer layer, short bits) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        // go through all the cells in the layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                Cell cell = layer.getCell(col, row);

                // check if cell exists
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create a body + fixture from cell
                bdef.type = BodyType.StaticBody;
                bdef.position.set(
                        (col + 0.5f) * tileSize / PPM,
                        (row + 0.5f) * tileSize / PPM
                );

                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(
                        -tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(
                        -tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2(
                        tileSize / 2 / PPM, tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);


            }
        }
    }

}









