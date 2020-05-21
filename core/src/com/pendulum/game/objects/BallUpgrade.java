package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.texture.TextureHolder;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.CATEGORY_JOINTS;
import static com.pendulum.game.utils.Constants.CATEGORY_TEXT;
import static com.pendulum.game.utils.Constants.JOINT_RADIUS;
import static com.pendulum.game.utils.Constants.MASK_JOINTS;
import static com.pendulum.game.utils.Constants.MASK_TEXT;
import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 23/03/2017.
 */

public class BallUpgrade {
    private boolean unlocked = false;
    private int price = -1;
    private Entity ball;
    private ArrayList<Entity> rope = new ArrayList<Entity>();
    private Entity score_emoji;
    private Entity lock;
    private float lock_opacity = 1.0f;
    private float scalar = 0.0f;
    private boolean selected;
    private float time = 0.0f;
    private boolean unlock = false;

    private com.pendulum.game.objects.ParticleCluster particles;

    private BitmapFont font;
    private GlyphLayout price_text;

    private float balltimeperiod = 0.5f;
    private float ballamplitude = ((float)Math.PI/8.0f);
    private float ballangularspeed = (float)((2.0f*Math.PI)/balltimeperiod);
    private float timeperiod = 2.0f;
    private String ballname = "NULL";
    private String ropename = "NULL";


    public BallUpgrade(World world, Vector2 platformpos, float platformwidth, float xdisplacement, float angle, TextureHolder textureHolder, Entity platform, float ballheight, BitmapFont font,
                       Integer price, String ballname, String ropename, boolean unlocked) {
        float lock_scalar = 1.2f;

        this.ropename = ropename;
        this.ballname = ballname;
        this.price = price;
        this.unlocked = unlocked;

        ball = new Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(platformpos.x+(platformwidth/2)+(float)(xdisplacement*Math.cos(angle))-(JOINT_RADIUS/2.0f),
                        platformpos.y-ballheight+(float)(xdisplacement*Math.sin(angle))),
                JOINT_RADIUS, world, BodyDef.BodyType.DynamicBody, CATEGORY_JOINTS, MASK_JOINTS), textureHolder.getTexture("ball_" + ballname), new Vector2(JOINT_RADIUS, JOINT_RADIUS), "joint");
        lock = new Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(platformpos.x+(platformwidth/2)+(float)(xdisplacement*Math.cos(angle))-(JOINT_RADIUS/2.0f),
                        platformpos.y-(ballheight/2.0f)+(float)(xdisplacement*Math.sin(angle))),
                JOINT_RADIUS*lock_scalar, world, BodyDef.BodyType.StaticBody, CATEGORY_TEXT, MASK_TEXT), textureHolder.getTexture("lock.png"), new Vector2(JOINT_RADIUS*lock_scalar, JOINT_RADIUS*lock_scalar), "emoji");
        ball.getBody().setActive(false);
        rope = com.pendulum.game.objects.ObjectHandler.createRope(platform.getBody(), ball.getBody(), world, "ROPE", textureHolder, xdisplacement, "rope_" + ropename);
        ball.getBody().setActive(true);

        this.font = font;

        price_text = new GlyphLayout();
        price_text.setText(font, String.valueOf(price));

        float xlength = (JOINT_RADIUS*1.5f)+(price_text.width/PPM);

        score_emoji = new Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(platformpos.x+(platformwidth/2)+(float)(xdisplacement*Math.cos(angle))-(xlength/2),
                platformpos.y - ballheight - JOINT_RADIUS +(float)(xdisplacement*Math.sin(angle))),
                JOINT_RADIUS/2.0f, world, BodyDef.BodyType.StaticBody,
                CATEGORY_TEXT, MASK_TEXT), textureHolder.getTexture("score.png"), new Vector2(JOINT_RADIUS/2.0f, JOINT_RADIUS/2.0f), "emoji");

        if(!unlocked) {
            ball.setTransparency(0.5f);
            for(int i=0; i<rope.size(); i++) {
                rope.get(i).setTransparency(0.5f);
            }
        } else {
            lock_opacity = 0.0f;
            score_emoji.setTransparency(lock_opacity);
        }
    }
    public int getPrice() {
        return price;
    }
    public boolean isUnlocked() {
        return unlocked;
    }
    public void dispose(World world) {
        world.destroyBody(ball.getBody());
        for(int i=0; i<rope.size(); i++) {
            world.destroyBody(rope.get(i).getBody());
        }
        world.destroyBody(lock.getBody());
        world.destroyBody(score_emoji.getBody());
    }
    public void update(float dt, World world, TextureHolder textureHolder) {
        if(selected) {
            scalar = 1.0f;
        }
        if(!unlocked && unlock) {
            if(!particles.isCreated()) {
                particles.create(world, textureHolder);
            }
            lock_opacity -= 3.0f*dt;
            particles.update(3.0f*dt, world);
            if(lock_opacity<0.0f) {
                lock_opacity = 0.0f;
                unlock = false;
                unlocked = true;
            }
            score_emoji.setTransparency(lock_opacity);
            lock.setTransparency(lock_opacity);
        }
        if(scalar!=0.0f) {
            float velocity = (float)(ballamplitude*ballangularspeed*scalar*Math.cos(ballangularspeed*time));
            float angle = (float)(ballamplitude*Math.cos(ballangularspeed*time));
            ball.getBody().setLinearVelocity((float)(velocity*Math.cos(angle)), (float)(-velocity*Math.sin(angle)));

            time += dt;
            if(time>=timeperiod) {
                time = 0.0f;
                if(!selected) {
                    ball.getBody().setLinearVelocity(0.0f, 0.0f);
                    for(int j=0; j<rope.size(); j++) {
                        rope.get(j).getBody().setLinearVelocity(0.0f, 0.0f);
                    }
                }
            }
            if(!selected) {
                scalar -= dt;
                if(scalar<0.0f) {
                    scalar = 0.01f;
                }
            }
        }

    }
    public void enable() {selected=true;}
    public void disable() {
        selected = false;
    }
    public boolean isTouching(Vector3 mousepos, int pellets) {
        //If above bottom of ball
        if(mousepos.y > ball.getBody().getPosition().y - ball.getSize().y) {
            //If below top of ball
            if(mousepos.y < ball.getBody().getPosition().y + 5f) {
                //If within x bounds
                if((ball.getBody().getPosition().x - ball.getSize().x) <  mousepos.x &&
                        mousepos.x <  (ball.getBody().getPosition().x + ball.getSize().x)) {
                    if(pellets >= price || unlocked) {
                        selected = true;
                        particles = new com.pendulum.game.objects.ParticleCluster(new Vector2(lock.getBody().getPosition()), 150.0f);
                        if (!unlocked) {
                            unlock = true;
                            ball.setTransparency(1.0f);
                            for (int i = 0; i < rope.size(); i++) {
                                rope.get(i).setTransparency(1.0f);
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        for(int i=0; i<rope.size(); i++) {
            rope.get(i).render(spriteBatch, shaderHandler);
        }
        ball.render(spriteBatch, shaderHandler);
        score_emoji.render(spriteBatch, shaderHandler);
        if(particles!=null) {
            particles.render(spriteBatch, shaderHandler);
        }
        if(lock_opacity!=1.0f) {
            price_text.setText(font, String.valueOf(price), new Color(1.0f, 1.0f, 1.0f, lock_opacity), price_text.width, 1, false);
        }
        font.draw(spriteBatch, price_text, (score_emoji.getBody().getPosition().x + (JOINT_RADIUS/2.0f))*PPM, (score_emoji.getBody().getPosition().y + (JOINT_RADIUS/4.0f))*PPM);
        if(!unlocked) {
            lock.render(spriteBatch, shaderHandler);
        }
    }
}
