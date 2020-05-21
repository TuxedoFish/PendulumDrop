package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.CATEGORY_PARTICLE;
import static com.pendulum.game.utils.Constants.MASK_PARTICLE;

/**
 * Created by Harry on 16/02/2017.
 */

public class ParticleCluster {
    private float transparency = 1.0f;
    private ArrayList<Entity> particles = new ArrayList<Entity>();
    private final float timeperiod = 1.0f;

    private boolean dead = false;

    private Vector2 position;
    private float strength;

    private boolean created = false;

    public ParticleCluster(Vector2 position, float strength) {
        this.position = position;
        this.strength = strength;
    }
    public float getTransparency() {
        return transparency;
    }
    public void create(World world, com.pendulum.game.texture.TextureHolder textureHolder) {
        for(int i=0; i<10; i++) {
            float angle = (float)((Math.random()-0.5f)*Math.PI*2);
            addParticle(position, angle, strength/5.0f, world, textureHolder);
        }
        created=true;
    }
    public boolean isCreated() {
        return created;
    }
    public void addParticle(Vector2 position, float direction, float strength, World world, com.pendulum.game.texture.TextureHolder textureHolder) {
        Entity particle = new Entity(ObjectHandler.createCollisionBody(position, 0.5f, world, BodyDef.BodyType.DynamicBody, CATEGORY_PARTICLE, MASK_PARTICLE),
                textureHolder.getTexture("score.png"), new Vector2(0.5f, 0.5f), "particle");
        particle.getBody().setLinearVelocity((float)(Math.cos(direction) * strength), (float)(Math.sin(direction)  * strength));
        particle.getBody().setFixedRotation(true);
        particles.add(particle);
    }
    public void update(float dt, World world) {
        if(transparency > 0) {
            transparency -= 1 * (dt / timeperiod);
            for(int i=0; i<particles.size(); i++) {
                particles.get(i).setTransparency(transparency);
            }
        }
        if (transparency < 0 && !dead) {
            dead = true;
            for (int i = 0; i < particles.size(); i++) {
                world.destroyBody(particles.get(i).getBody());
            }
        }
    }
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        if(transparency > 0) {
            for (int i = 0; i < particles.size(); i++) {
                particles.get(i).render(spriteBatch, shaderHandler);
            }
        }
    }
}
