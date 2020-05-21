package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.texture.TextureHolder;

import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 23/02/2017.
 */

public class Slalom extends Obstacle {
    private float WORLD_WIDTH;
    private float REAL_WORLD_HEIGHT;

    private boolean hit = false;

    private boolean direction;

    private com.pendulum.game.objects.Entity leftpost;
    private com.pendulum.game.objects.Entity rightpost;
    private com.pendulum.game.objects.Entity blocker;
    private com.pendulum.game.objects.Entity pellet;

    private final float animationtime = 0.05f;
    private float currenttime = 0.0f;

    private Vector2 original_position;

    private boolean collected = false;

    private TextureHolder textureHolder;

    private boolean blockinplace = true;

    public Slalom(float WORLD_WIDTH, float REAL_WORLD_HEIGHT, int level, String ropename) {
        super(level, ropename, "slalom");
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;
    }
    public boolean isBlocked() {
        return true;
    }
    @Override
    public void create(float ypos, World world, TextureHolder textureHolder) {
        if(level%2 == 0) {
            direction = true;
        } else {
            direction = false;
        }

        this.textureHolder = textureHolder;

        float seperation = WORLD_WIDTH / PPM / 4.0f - (Float.valueOf(level)*0.1f);
        if(seperation<3.0f) {
            seperation = 3.0f;
        }

        Body[] posts;
        Vector2 middle;
        if(direction) {
            middle = new Vector2(WORLD_WIDTH / PPM / 4.0f , ypos);
        } else {
            middle = new Vector2(WORLD_WIDTH / PPM * 3.0f /4.0f , ypos);
        }
        posts = com.pendulum.game.objects.ObjectHandler.createSlalom(new Vector2(middle.x - seperation/2, ypos), world, seperation);
        blocker = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createBackgroundBody(new Vector2(middle.x, ypos), world, seperation, false, false, 0.5f), textureHolder.getTexture("blocked.png"),
                new Vector2(seperation, 0.5f), "blocker");
        blocker.addTex(textureHolder.getTexture("blocked1.5.png")); blocker.addTex(textureHolder.getTexture("blocked2.png")); blocker.addTex(textureHolder.getTexture("blocked2.5.png"));
        blocker.addTex(textureHolder.getTexture("blocked3.png")); blocker.addTex(textureHolder.getTexture("blocked3.5.png"));blocker.addTex(textureHolder.getTexture("blocked4.png"));
        blocker.addTex(textureHolder.getTexture("blocked4.5.png"));blocker.addTex(textureHolder.getTexture("blocked4.51.png"));blocker.addTex(textureHolder.getTexture("blocked4.52.png"));
        blocker.addTex(textureHolder.getTexture("blocked5.png"));blocker.addTex(textureHolder.getTexture("blocked5.5.png"));blocker.addTex(textureHolder.getTexture("blocked6.png"));
        blocker.addTex(textureHolder.getTexture("blocked6.5.png"));blocker.addTex(textureHolder.getTexture("blocked7.png"));blocker.addTex(textureHolder.getTexture("blocked7.5.png"));
        blocker.addTex(textureHolder.getTexture("open.png"));blocker.addTex(textureHolder.getTexture("open1.5.png")); blocker.addTex(textureHolder.getTexture("open2.png")); blocker.addTex(textureHolder.getTexture("open2.5.png"));
        blocker.addTex(textureHolder.getTexture("open3.png")); blocker.addTex(textureHolder.getTexture("open3.5.png"));blocker.addTex(textureHolder.getTexture("open4.png"));blocker.addTex(textureHolder.getTexture("open4.5.png"));
        blocker.addTex(textureHolder.getTexture("open4.51.png")); blocker.addTex(textureHolder.getTexture("open4.52.png"));blocker.addTex(textureHolder.getTexture("open5.png"));blocker.addTex(textureHolder.getTexture("open5.5.png"));
        blocker.addTex(textureHolder.getTexture("open6.png"));  blocker.addTex(textureHolder.getTexture("open6.5.png"));blocker.addTex(textureHolder.getTexture("open7.png"));blocker.addTex(textureHolder.getTexture("open7.5.png"));
        if(direction) {
            leftpost = new com.pendulum.game.objects.Entity(posts[0], textureHolder.getTexture("post.png"), new Vector2(1.0f, 2.0f), "postU");
            rightpost = new com.pendulum.game.objects.Entity(posts[1], textureHolder.getTexture("post.png"), new Vector2(1.0f, 2.0f), "postU");

        } else {
            leftpost = new com.pendulum.game.objects.Entity(posts[0], textureHolder.getTexture("post.png"), new Vector2(1.0f, 2.0f), "postD");
            rightpost = new com.pendulum.game.objects.Entity(posts[1], textureHolder.getTexture("post.png"), new Vector2(1.0f, 2.0f), "postD");

        }

        if(Math.random()>=0.7f) {
            pellet = addPellet(new Vector2((float)(posts[0].getPosition().x+(seperation/2.0)), ypos), world, textureHolder);
        }

        original_position = new Vector2(leftpost.getBody().getPosition());
    }

    public void setPosition(Vector2 position) {
        Vector2 difference = new Vector2(position.x-leftpost.getBody().getPosition().x,
                position.y-leftpost.getBody().getPosition().y);
        leftpost.getBody().setTransform(leftpost.getBody().getPosition().add(difference), leftpost.getBody().getAngle());
        rightpost.getBody().setTransform(rightpost.getBody().getPosition().add(difference), rightpost.getBody().getAngle());
        blocker.getBody().setTransform(blocker.getBody().getPosition().add(difference), blocker.getBody().getAngle());
        if(pellet!=null) {pellet.getBody().setTransform(pellet.getBody().getPosition().add(difference), pellet.getBody().getAngle());}
    }

    public Vector2 getPosition() {
        return original_position;
    }

    @Override
    public boolean update(float dt, com.pendulum.game.objects.Entity player, World world) {
        if(currenttime>=animationtime) {
            currenttime=0.0f;
            if(blockinplace) {
                if (blocker.getTextureid() < 15) {
                    blocker.setTextureid(blocker.getTextureid() + 1);
                } else {
                    blocker.setTextureid(0);
                }
            } else {
                if (blocker.getTextureid() < 16 || blocker.getTextureid()==31) {
                    blocker.setTextureid(16);
                } else {
                    blocker.setTextureid(blocker.getTextureid() + 1);
                }
            }
        } else {
            currenttime += dt;
        }
        if(hit && blocker.getTransparency()>0.0f) {
            if(blocker.getTransparency()-(dt/1.0f) < 0.0f) {
                blocker.setTransparency(0.0f);
            } else {
                blocker.setTransparency(blocker.getTransparency() - (dt / 0.3f));
            }
        }
        if(collected && pellet!=null) {
            world.destroyBody(pellet.getBody());
            pellet = null;
        }
        //IS TO THE LEFT OF RIGHT PLATFORM
        if(player.getBody().getPosition().x < rightpost.getBody().getPosition().x + 0.8f) {
            //IS TO THE RIGHT OF LEFT PLATFORM
            if(player.getBody().getPosition().x > leftpost.getBody().getPosition().x - 0.8f) {
                //PLAYER HASNT REACHED YET
                if(!hit) {
                    if((direction && player.getBody().getLinearVelocity().y>0.0f) || (!direction && player.getBody().getLinearVelocity().y<0.0f)) {
                        if((player.getBody().getPosition().y > leftpost.getBody().getPosition().y - 0.8f) && (player.getBody().getPosition().y < leftpost.getBody().getPosition().y + 0.8f)) {
                            if (!blockinplace) {
                                hit = true;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void removeBlocker(World world) {
        blocker.setPosition(blocker.getBody().getPosition());
        blocker.setTex(textureHolder.getTexture("open.png"));
        blocker.setAngle(0.0f);
        world.destroyBody(blocker.getBody());
        blockinplace = false;
    }

    @Override
    public void action(String action, World world) {

    }

    @Override
    public void setTint(Color tint) {

    }

    @Override
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        blocker.render(spriteBatch, shaderHandler);
        leftpost.render(spriteBatch, shaderHandler);
        rightpost.render(spriteBatch, shaderHandler);
        if(pellet!=null) {pellet.render(spriteBatch, shaderHandler);}
    }

    @Override
    public void dispose(World world) {
        if(blockinplace) {
            world.destroyBody(blocker.getBody());
        }
        world.destroyBody(leftpost.getBody());
        world.destroyBody(rightpost.getBody());
    }

    @Override
    public float getLowestY() {
        return leftpost.getBody().getPosition().y-0.5f;
    }

    @Override
    public void notifyCollision(com.pendulum.game.objects.Entity pellet) {
        if(this.pellet == pellet) {
            collected = true;
        }
    }
}
