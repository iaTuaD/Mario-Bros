package com.mygdx.mario.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.MarioBros;

public class Mario extends Sprite {
    public World world;
    public Body b2Body;

    public Mario(World world) {
        this.world = world;
        defineMario();
    }

    public void defineMario() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);
        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MarioBros.PPM);
        fDef.shape = shape;
        b2Body.createFixture(fDef);
    }
}
