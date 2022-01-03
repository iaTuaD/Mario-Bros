package com.mygdx.mario.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mario.MarioBros;
import com.mygdx.mario.Scenes.Hud;
import com.mygdx.mario.Screens.PlayScreen;
import com.mygdx.mario.Sprites.Items.ItemDef;
import com.mygdx.mario.Sprites.Items.Mushroom;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLACNK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        if (getCell().getTile().getId() == BLACNK_COIN) {
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            if (object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM),
                        Mushroom.class));
                MarioBros.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }else {
                MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }
        }
        getCell().setTile(tileSet.getTile(BLACNK_COIN));
        Hud.addScore(100);
    }
}
