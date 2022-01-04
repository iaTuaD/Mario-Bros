package com.mygdx.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mario.MarioBros;
import com.mygdx.mario.Scenes.Hud;
import com.mygdx.mario.Sprites.Enemies.Enemy;
import com.mygdx.mario.Sprites.Items.Item;
import com.mygdx.mario.Sprites.Items.ItemDef;
import com.mygdx.mario.Sprites.Items.Mushroom;
import com.mygdx.mario.Sprites.Mario;
import com.mygdx.mario.Tools.B2WorldCreator;
import com.mygdx.mario.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {
    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private TextureAtlas atlas;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Mario player;
    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

//        new B2WorldCreator(world,  map);
        creator = new B2WorldCreator(this);
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
//        goomba = new Goomba(this, 5.64f, .16f);
    }

    public void spawnItem(ItemDef iDef) {
        itemsToSpawn.add(iDef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for (Enemy enemy : creator.getEnemy()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2Body.setActive(true);
            }
        }

        for (Item item : items) {
            item.update(dt);
        }

        hud.update(dt);

        if (player.currentState != Mario.State.DEAD) {
            gameCam.position.x = player.b2Body.getPosition().x;
        }
        gameCam.update();
        renderer.setView(gameCam);
    }

    private void handleInput(float dt) {
        if (player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                Gdx.app.debug("tai", "press");
                player.b2Body.applyLinearImpulse(new Vector2(0, 4f), player.b2Body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && player.b2Body.getLinearVelocity().x <= 2) {
                Gdx.app.debug("tai", "press");
                player.b2Body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2Body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && player.b2Body.getLinearVelocity().x >= -2) {
                Gdx.app.debug("tai", "press");
                player.b2Body.applyLinearImpulse(new Vector2(-0.5f, 0), player.b2Body.getWorldCenter(), true);
            }

            if (Gdx.input.isTouched()) {
                if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && Gdx.input.getY() >= Gdx.graphics.getHeight() / 2) {
                    //left
                    player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);
                } else if (Gdx.input.getX() >= Gdx.graphics.getWidth() / 2 && Gdx.input.getY() >= Gdx.graphics.getHeight() / 2) {
                    //right
                    player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
                }
                if (Gdx.input.getY() < Gdx.graphics.getHeight() / 2) {
                    // above
                    player.b2Body.applyLinearImpulse(new Vector2(0, 0.5f), player.b2Body.getWorldCenter(), true);
                }
            }
        }

    }

    @Override

    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0, 0, 1);
        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemy()) {
            enemy.draw(game.batch);
        }
        for (Item item : items) {
            item.draw(game.batch);
        }

        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public boolean gameOver() {
        if (player.currentState == Mario.State.DEAD && player.getStateTime() > 3) {
            return true;
        }
        return false;
    }
}
