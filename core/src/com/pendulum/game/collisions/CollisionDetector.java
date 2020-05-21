package com.pendulum.game.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pendulum.game.state.PlayScreen;
import com.pendulum.game.utils.Constants;

/**
 * Created by Harry on 26/01/2017.
 */

public class CollisionDetector implements ContactListener {
    private int deletepellet = -1;
    private PlayScreen parent;
    private boolean resolved = true;

    public CollisionDetector(PlayScreen parent) {
        this.parent = parent;
    }

    public int getDeletepellet() {
        return deletepellet;
    }

    public void reset() {
        deletepellet = -1;
    }

    @Override
    public void beginContact(Contact contact) {
        if((contact.getFixtureA().getFilterData().categoryBits == Constants.CATEGORY_PELLET && contact.getFixtureB().getFilterData().categoryBits == Constants.CATEGORY_PLAYER) ||
                (contact.getFixtureB().getFilterData().categoryBits == Constants.CATEGORY_PELLET && contact.getFixtureA().getFilterData().categoryBits == Constants.CATEGORY_PLAYER)) {
            contact.setEnabled(false);
            if((contact.getFixtureA().getFilterData().categoryBits == Constants.CATEGORY_PELLET)) {
                parent.notifyCollision(contact, true, (com.pendulum.game.objects.Entity)contact.getFixtureA().getBody().getUserData());
            } else {
                parent.notifyCollision(contact, true, (com.pendulum.game.objects.Entity)contact.getFixtureB().getBody().getUserData());
            }
        }
        if((contact.getFixtureA().getFilterData().categoryBits == Constants.CATEGORY_PLAYER && contact.getFixtureB().getFilterData().categoryBits == Constants.CATEGORY_SCENERY) ||
                (contact.getFixtureB().getFilterData().categoryBits == Constants.CATEGORY_PLAYER && contact.getFixtureA().getFilterData().categoryBits == Constants.CATEGORY_SCENERY)) {
            resolved = false;
        }
    }
    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if(!resolved) {
            parent.notifyCollision(contact, false, null);
            resolved = true;
        }
    }
}
