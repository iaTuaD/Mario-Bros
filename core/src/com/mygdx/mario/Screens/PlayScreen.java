package com.mygdx.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mario.MarioBros;
import com.mygdx.mario.Scenes.Hud;
import com.mygdx.mario.Sprites.Mario;
import com.mygdx.mario.Tools.B2WorldCreator;

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
    private Mario player;

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

        new B2WorldCreator(world,  map);
        player = new Mario(world, this);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        handInput(dt);
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    private void handInput(float dt) {
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
                player.b2Body.applyLinearImpulse(new Vector2(-0.5f, 0), player.b2Body.getWorldCenter(), true);
            } else if (Gdx.input.getX() >= Gdx.graphics.getWidth() / 2 && Gdx.input.getY() >= Gdx.graphics.getHeight() / 2) {
                //right
                player.b2Body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2Body.getWorldCenter(), true);
            }
            if (Gdx.input.getY() < Gdx.graphics.getHeight() / 2) {
                // above
                player.b2Body.applyLinearImpulse(new Vector2(0, 0.5f), player.b2Body.getWorldCenter(), true);
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
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
}
