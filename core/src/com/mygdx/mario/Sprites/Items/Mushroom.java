package com.mygdx.mario.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.mario.MarioBros;
import com.mygdx.mario.Screens.PlayScreen;

public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0, 0);
    }

    @Override
    protected void defineItem() {
        BodyDef bDef = new BodyDef();
//        bDef.position.set(50 / MarioBros.PPM, 32 / MarioBros.PPM);
        bDef.position.set(getX() , getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);
        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);
    }

    @Override
    public void use() {
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.setLinearVelocity(velocity);
    }

}
