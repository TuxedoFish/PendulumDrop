package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.pendulum.game.utils.Constants.CATEGORY_PELLET;
import static com.pendulum.game.utils.Constants.MASK_PELLET;
import static com.pendulum.game.utils.Constants.SCORE_RADIUS;

/**
 * Created by Harry on 20/02/2017.
 */

public abstract class Obstacle {
    public int level;
    public String ropename;
    private String type;

    public Obstacle(int level, String ropename, String type) {
        this.level = level;
        this.ropename = ropename;
        this.type = type;
    }

    public String getType() {return type; }
    public int getLevel() { return level; }
    public abstract boolean isBlocked();
    public abstract void create(float ypos, World world, com.pendulum.game.texture.TextureHolder textureHolder);
    public abstract boolean update(float dt, Entity player, World world);
    public abstract void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler);
    public abstract void dispose(World world);
    public abstract float getLowestY();
    public abstract void setPosition(Vector2 position);
    public abstract Vector2 getPosition();
    public abstract void notifyCollision(Entity pellet);
    public abstract void removeBlocker(World world);
    public abstract void action(String action, World world);
    public abstract void setTint(Color tint);

    public static Entity addPellet(Vector2 position, World world, com.pendulum.game.texture.TextureHolder textureHolder) {
        Entity pellet = new Entity(ObjectHandler.createCollisionBody(position, SCORE_RADIUS+1.0f, world, BodyDef.BodyType.StaticBody, CATEGORY_PELLET, MASK_PELLET),
                textureHolder.getTexture("score.png"), new Vector2(SCORE_RADIUS, SCORE_RADIUS), "pellet");
        return pellet;
    }
}
