package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.utils.Constants;

/**
 * Created by Harry on 20/02/2017.
 */

public class Platform extends Obstacle{
    private float REAL_WORLD_HEIGHT;
    private float WORLD_WIDTH;

    private Entity leftPlatform;
    private Entity rightPlatform;

    private Entity blocker;

    private Entity pellet;

    private Vector2 original_position;

    private boolean movement = true;
    private float maxspeedmpers;
    private float speedmpers;
    private final float acceleration = 2f;

    private float seperation;

    private final float animationtime = 0.05f;
    private float currenttime = 0.0f;

    private com.pendulum.game.texture.TextureHolder textureHolder;

    private boolean hit = false;

    private boolean collected = false;

    private boolean blocked = false;

    private boolean blockinplace = true;

    private float displacement = 0.0f;

    private int totallevel = 0;

    public Platform(float WORLD_WIDTH, float REAL_WORLD_HEIGHT, int level, boolean blocked, String ropename, int totallevel) {
        super(level, ropename, "platform");
        this.totallevel = totallevel;
        this.blocked = blocked;
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;
    }
    public boolean isBlocked() {
        return blocked;
    }
    @Override
    public float getLowestY() {
        return leftPlatform.getBody().getPosition().y - 0.5f;
    }
    @Override
    public void create(float ypos, World world, com.pendulum.game.texture.TextureHolder textureHolder) {
        this.textureHolder = textureHolder;
        seperation = 10f - (Float.valueOf(level)*0.1f);
        if(level==0) {
            maxspeedmpers = 2.0f;
        } else {
            maxspeedmpers = 2.0f + (3.0f-(30.0f/Float.valueOf(level+10)));
        }
        speedmpers = 0.0f;

        if(seperation<5.0f) {
            seperation = 5.0f;
        }
        float widthA = (float)Math.random()*((WORLD_WIDTH/ Constants.PPM)-seperation);
        float widthB = (WORLD_WIDTH/ Constants.PPM) - widthA - seperation + 0.1f;

        float red   = (float)(Math.sin(Constants.FREQUENCY*totallevel + 0) * 127) + 128;
        float green = (float)(Math.sin(Constants.FREQUENCY*totallevel + 2) * 127) + 128;
        float blue  = (float)(Math.sin(Constants.FREQUENCY*totallevel + 4) * 127) + 128;

        red/=255.0f;
        blue/=255.0f;
        green/=255.0f;

        float height = 0.6f;

        leftPlatform = new Entity(ObjectHandler.createBackgroundBody(new Vector2((widthA) - (WORLD_WIDTH/ Constants.PPM/2.0f),
                        ypos),
                world, WORLD_WIDTH/ Constants.PPM, true, true, height), textureHolder.getTexture("platform.png"), new Vector2( WORLD_WIDTH/ Constants.PPM, 2*height), "platformL");
        leftPlatform.addTex(textureHolder.getTexture("platformend.png"));

        leftPlatform.setTint(red, blue, green);

        if(blocked) {
            blocker = new Entity(ObjectHandler.createBackgroundBody(new Vector2(widthA + (seperation / 2.0f), ypos), world, seperation, false, false, 0.5f), textureHolder.getTexture("blocked.png"),
                    new Vector2(seperation, 0.5f), "blocker");
            blocker.setPosition(blocker.getBody().getPosition());
            blocker.setAngle(0.0f);
            blocker.addTex(textureHolder.getTexture("blocked1.5.png")); blocker.addTex(textureHolder.getTexture("blocked2.png")); blocker.addTex(textureHolder.getTexture("blocked2.5.png"));
            blocker.addTex(textureHolder.getTexture("blocked3.png")); blocker.addTex(textureHolder.getTexture("blocked3.5.png"));blocker.addTex(textureHolder.getTexture("blocked4.png"));
            blocker.addTex(textureHolder.getTexture("blocked4.5.png"));blocker.addTex(textureHolder.getTexture("blocked4.51.png"));blocker.addTex(textureHolder.getTexture("blocked4.52.png"));
            blocker.addTex(textureHolder.getTexture("blocked5.png"));blocker.addTex(textureHolder.getTexture("blocked5.5.png"));blocker.addTex(textureHolder.getTexture("blocked6.png"));
            blocker.addTex(textureHolder.getTexture("blocked6.5.png"));blocker.addTex(textureHolder.getTexture("blocked7.png"));blocker.addTex(textureHolder.getTexture("blocked7.5.png"));
            blocker.addTex(textureHolder.getTexture("open.png"));blocker.addTex(textureHolder.getTexture("open1.5.png")); blocker.addTex(textureHolder.getTexture("open2.png")); blocker.addTex(textureHolder.getTexture("open2.5.png"));
            blocker.addTex(textureHolder.getTexture("open3.png")); blocker.addTex(textureHolder.getTexture("open3.5.png"));blocker.addTex(textureHolder.getTexture("open4.png"));blocker.addTex(textureHolder.getTexture("open4.5.png"));
            blocker.addTex(textureHolder.getTexture("open4.51.png")); blocker.addTex(textureHolder.getTexture("open4.52.png"));blocker.addTex(textureHolder.getTexture("open5.png"));blocker.addTex(textureHolder.getTexture("open5.5.png"));
            blocker.addTex(textureHolder.getTexture("open6.png"));  blocker.addTex(textureHolder.getTexture("open6.5.png"));blocker.addTex(textureHolder.getTexture("open7.png"));blocker.addTex(textureHolder.getTexture("open7.5.png"));
        }

        rightPlatform = new Entity(ObjectHandler.createBackgroundBody(new Vector2(widthA +
                        seperation +  (WORLD_WIDTH/ Constants.PPM/2.0f), ypos),
                world,  WORLD_WIDTH/ Constants.PPM, false, true, height), textureHolder.getTexture("platform.png"), new Vector2(WORLD_WIDTH/ Constants.PPM, 2*height), "platformR");
        rightPlatform.addTex(textureHolder.getTexture("platformend.png"));

        rightPlatform.setTint(red, blue, green);

        if(Math.random()>=0.7f) {
            pellet = addPellet(new Vector2((float)((widthA)+(seperation/2.0)), ypos), world, textureHolder);
        }

        original_position = new Vector2(leftPlatform.getBody().getPosition());
    }
    public void setPosition(Vector2 position) {
        Vector2 difference = new Vector2(position.x-leftPlatform.getBody().getPosition().x,
                position.y-leftPlatform.getBody().getPosition().y);
        displacement = position.x-original_position.x;
        leftPlatform.getBody().setTransform(leftPlatform.getBody().getPosition().add(difference), leftPlatform.getBody().getAngle());
        rightPlatform.getBody().setTransform(rightPlatform.getBody().getPosition().add(difference), rightPlatform.getBody().getAngle());
        if(blocked) {
            if(blockinplace) {blocker.getBody().setTransform(blocker.getBody().getPosition().add(difference), blocker.getBody().getAngle());}
            blocker.setPosition(blocker.getPosition().add(difference));
        }
        if(pellet!=null) {pellet.getBody().setTransform(pellet.getBody().getPosition().add(difference), pellet.getBody().getAngle());}
    }

    public Vector2 getPosition() {
        return original_position;
    }
    @Override
    public boolean update(float dt, Entity player, World world) {
        if(blocked) {
            if (currenttime >= animationtime) {
                currenttime = 0.0f;
                if (blockinplace) {
                    if (blocker.getTextureid() < 15) {
                        blocker.setTextureid(blocker.getTextureid() + 1);
                    } else {
                        blocker.setTextureid(0);
                    }
                } else {
                    if (blocker.getTextureid() < 16 || blocker.getTextureid() == 31) {
                        blocker.setTextureid(16);
                    } else {
                        blocker.setTextureid(blocker.getTextureid() + 1);
                    }
                }
            } else {
                currenttime += dt;
            }
        }
        if(hit && blocked && blocker.getTransparency()>0.0f) {
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
        if(movement) {
            leftPlatform.getBody().setTransform(leftPlatform.getBody().getPosition().x + (speedmpers*dt), leftPlatform.getBody().getPosition().y, leftPlatform.getBody().getAngle());
            rightPlatform.getBody().setTransform(rightPlatform.getBody().getPosition().x + (speedmpers*dt), rightPlatform.getBody().getPosition().y, rightPlatform.getBody().getAngle());
            if(blocked) {
                blocker.setPosition(new Vector2(blocker.getPosition().x + (speedmpers * dt), blocker.getPosition().y));
                if(blockinplace){blocker.getBody().setTransform(new Vector2(blocker.getBody().getPosition().x + (speedmpers * dt), blocker.getBody().getPosition().y), 0.0f);}
            }
            if(pellet!=null) {pellet.getBody().setTransform(pellet.getBody().getPosition().x + (speedmpers*dt), pellet.getBody().getPosition().y, pellet.getBody().getAngle());}

            if(speedmpers<maxspeedmpers) {
                speedmpers+=acceleration*dt;
            }

            if(rightPlatform.getBody().getPosition().x - (WORLD_WIDTH/ Constants.PPM/2.0f) + 1.0f >= (WORLD_WIDTH/ Constants.PPM) + displacement ) {
                movement = false;
                speedmpers = 0.5f * maxspeedmpers;
            }
        } else {
            leftPlatform.getBody().setTransform(leftPlatform.getBody().getPosition().x - (speedmpers*dt), leftPlatform.getBody().getPosition().y, leftPlatform.getBody().getAngle());
            rightPlatform.getBody().setTransform(rightPlatform.getBody().getPosition().x - (speedmpers*dt), rightPlatform.getBody().getPosition().y, rightPlatform.getBody().getAngle());
            if(blocked) {
                blocker.setPosition(new Vector2(blocker.getPosition().x - (speedmpers * dt), blocker.getPosition().y));
                if(blockinplace){blocker.getBody().setTransform(new Vector2(blocker.getBody().getPosition().x - (speedmpers * dt), blocker.getBody().getPosition().y), 0.0f);}
            }
            if(pellet!=null) {pellet.getBody().setTransform(pellet.getBody().getPosition().x - (speedmpers*dt), pellet.getBody().getPosition().y, pellet.getBody().getAngle());}

            if(speedmpers<maxspeedmpers) {
                speedmpers+=acceleration*dt;
            }

            if(rightPlatform.getBody().getPosition().x - (WORLD_WIDTH/ Constants.PPM/2.0f) - 1.0f <= (seperation) + displacement) {
                movement = true;
                speedmpers = 0.5f * maxspeedmpers;
            }
        }
        //IS TO THE LEFT OF RIGHT PLATFORM
        if(player.getBody().getPosition().x < rightPlatform.getBody().getPosition().x - (WORLD_WIDTH/ Constants.PPM/2.0f)) {
            //IS TO THE RIGHT OF LEFT PLATFORM
            if(player.getBody().getPosition().x > leftPlatform.getBody().getPosition().x + (WORLD_WIDTH/ Constants.PPM/2.0f)) {
                //IS BELOW LEVEL OF PLATFORM AND PLAYER HASNT REACHED YET
                if(player.getBody().getPosition().y < rightPlatform.getBody().getPosition().y && !hit) {
                    if((blocked && !blockinplace) || (!blocked)) {
                        hit = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        if(blocked){blocker.render(spriteBatch, shaderHandler);}
        leftPlatform.render(spriteBatch, shaderHandler);
        rightPlatform.render(spriteBatch, shaderHandler);
        if(pellet!=null) {pellet.render(spriteBatch, shaderHandler);}
    }

    @Override
    public void dispose(World world) {
        if(blockinplace) {
            if (blocker != null) {
                world.destroyBody(blocker.getBody());
            }
        }
        world.destroyBody(leftPlatform.getBody());
        world.destroyBody(rightPlatform.getBody());
        if(pellet!=null) {world.destroyBody(pellet.getBody());}
    }

    @Override
    public void notifyCollision(Entity pellet) {
        if(this.pellet == pellet) {
            collected = true;
        }
    }

    @Override
    public void removeBlocker(World world) {
        if(blockinplace) {
            blocker.setTex(textureHolder.getTexture("open.png"));
            blockinplace = false;
            world.destroyBody(blocker.getBody());
        }
    }

    @Override
    public void action(String action, World world) {

    }

    @Override
    public void setTint(Color tint) {
        rightPlatform.setTint(tint.r, tint.g, tint.b);
        leftPlatform.setTint(tint.r, tint.g, tint.b);
    }
}
