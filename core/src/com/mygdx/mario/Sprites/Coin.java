package com.mygdx.mario.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.MarioBros;
import com.mygdx.mario.Scenes.Hud;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLACNK_COIN = 28;

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.debug("tai", "coll coin");
        if (getCell().getTile().getId() == BLACNK_COIN){
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        }else {
            MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLACNK_COIN));
        Hud.addScore(100);
    }
}
