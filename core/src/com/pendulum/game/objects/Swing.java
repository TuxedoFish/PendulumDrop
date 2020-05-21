package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.texture.TextureHolder;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.CATEGORY_JOINTS;
import static com.pendulum.game.utils.Constants.JOINT_RADIUS;
import static com.pendulum.game.utils.Constants.MASK_JOINTS;
import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 06/03/2017.
 */

public class Swing extends Obstacle {
    private float WORLD_WIDTH;
    private float REAL_WORLD_HEIGHT;

    private boolean hit = false;
    private boolean broke = false;

    private com.pendulum.game.objects.Entity joint;
    private ArrayList<com.pendulum.game.objects.Entity> rope;
    private Joint connection;
    private com.pendulum.game.objects.Entity blocker;
    private com.pendulum.game.objects.Entity pellet;

    private Vector2 original_position;

    private final float animationtime = 0.03f;
    private float currenttime = 0.0f;

    private float radius = 5.0f;

    private boolean collected = false;

    private TextureHolder textureHolder;

    private boolean blockinplace = true;

    private String rodname;
    private String ballname;

    public Swing(float WORLD_WIDTH, float REAL_WORLD_HEIGHT, int level, String ropename, String ballname, String rodname) {
        super(level, ropename, "swing");
        this.ballname = ballname;
        this.rodname = rodname;
        rope = new ArrayList<com.pendulum.game.objects.Entity>();
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;
    }
    public boolean isBlocked() {
        return true;
    }
    @Override
    public void create(float ypos, World world, TextureHolder textureHolder) {
        this.radius = 5.0f - (level * 0.1f);
        if(radius < 2.0f) {
            this.radius = 2.0f;
        }

        this.textureHolder = textureHolder;

        float xpos = (float)((JOINT_RADIUS/2.0f) + radius + (Math.random() * (WORLD_WIDTH / PPM - (2.0f*((JOINT_RADIUS/2.0f) + radius)))));

        this.joint = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(xpos, ypos), JOINT_RADIUS/2.0f, world, BodyDef.BodyType.StaticBody, CATEGORY_JOINTS, MASK_JOINTS),
                textureHolder.getTexture("ball_" + ballname), new Vector2(JOINT_RADIUS, JOINT_RADIUS), "joint");
        this.blocker = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(xpos, ypos), radius*2.0f, world, BodyDef.BodyType.StaticBody, CATEGORY_JOINTS, MASK_JOINTS),
                textureHolder.getTexture("closedcircle1.png"), new Vector2(radius*2.0f, radius*2.0f), "joint");
        blocker.addTex(textureHolder.getTexture("closedcircle2.png")); blocker.addTex(textureHolder.getTexture("closedcircle3.png")); blocker.addTex(textureHolder.getTexture("closedcircle4.png"));
        blocker.addTex(textureHolder.getTexture("closedcircle5.png")); blocker.addTex(textureHolder.getTexture("closedcircle6.png")); blocker.addTex(textureHolder.getTexture("closedcircle7.png"));
        blocker.addTex(textureHolder.getTexture("closedcircle8.png")); blocker.addTex(textureHolder.getTexture("closedcircle9.png")); blocker.addTex(textureHolder.getTexture("closedcircle10.png"));
        blocker.addTex(textureHolder.getTexture("closedcircle11.png")); blocker.addTex(textureHolder.getTexture("closedcircle12.png")); blocker.addTex(textureHolder.getTexture("closedcircle13.png"));
        blocker.addTex(textureHolder.getTexture("closedcircle14.png")); blocker.addTex(textureHolder.getTexture("closedcircle15.png")); blocker.addTex(textureHolder.getTexture("closedcircle16.png"));
        blocker.addTex(textureHolder.getTexture("opencircle1.png"));
        blocker.addTex(textureHolder.getTexture("opencircle2.png")); blocker.addTex(textureHolder.getTexture("opencircle3.png")); blocker.addTex(textureHolder.getTexture("opencircle4.png"));
        blocker.addTex(textureHolder.getTexture("opencircle5.png")); blocker.addTex(textureHolder.getTexture("opencircle6.png")); blocker.addTex(textureHolder.getTexture("opencircle7.png"));
        blocker.addTex(textureHolder.getTexture("opencircle8.png")); blocker.addTex(textureHolder.getTexture("opencircle9.png")); blocker.addTex(textureHolder.getTexture("opencircle10.png"));
        blocker.addTex(textureHolder.getTexture("opencircle11.png")); blocker.addTex(textureHolder.getTexture("opencircle12.png")); blocker.addTex(textureHolder.getTexture("opencircle13.png"));
        blocker.addTex(textureHolder.getTexture("opencircle14.png")); blocker.addTex(textureHolder.getTexture("opencircle15.png")); blocker.addTex(textureHolder.getTexture("opencircle16.png"));

        if(Math.random()>=0.7f) {
            double angle = ((Math.round(Math.random() * 4.0f)/4.0f) * 2.0f * Math.PI) - Math.PI;
            pellet = addPellet(new Vector2((float)(joint.getBody().getPosition().x+(radius * 0.9f * Math.sin(angle))), (float)(joint.getBody().getPosition().y+(radius * 0.9f * Math.cos(angle)))), world, textureHolder);
        }

        original_position = new Vector2(joint.getBody().getPosition());
    }

    private void createRope(com.pendulum.game.objects.Entity player, World world) {
        rope = com.pendulum.game.objects.ObjectHandler.createRope(joint.getBody(), player.getBody(), world, "rod", textureHolder, 0.0f, rodname);
        connection = player.getBody().getJointList().get(0).joint;
    }

    public void setPosition(Vector2 position) {
        Vector2 difference = new Vector2(position.x-joint.getBody().getPosition().x,
                position.y-joint.getBody().getPosition().y);
        joint.getBody().setTransform(joint.getBody().getPosition().add(difference), joint.getBody().getAngle());
        blocker.getBody().setTransform(joint.getBody().getPosition().x, joint.getBody().getPosition().y, blocker.getBody().getAngle());
        blocker.setPosition(joint.getBody().getPosition());
        if(pellet!=null) {pellet.getBody().setTransform(pellet.getBody().getPosition().add(difference), pellet.getBody().getAngle());}
    }

    public Vector2 getPosition() {
        return original_position;
    }

    @Override
    public boolean update(float dt, com.pendulum.game.objects.Entity player, World world) {
        if(hit && blocker.getTransparency()>0.0f) {
            if(blocker.getTransparency()-(dt/1.0f) < 0.0f) {
                blocker.setTransparency(0.0f);
            } else {
                blocker.setTransparency(blocker.getTransparency() - (dt / 0.3f));
            }
        }
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
        if(collected && pellet!=null) {
            world.destroyBody(pellet.getBody());
            pellet = null;
        }
        Vector2 PtoJ = new Vector2(player.getBody().getPosition().x-joint.getBody().getPosition().x, player.getBody().getPosition().y-joint.getBody().getPosition().y);
        if(PtoJ.len()<radius && !hit) {
            createRope(player, world);
            hit = true;
            return true;
        }
        return false;
    }

    @Override
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        blocker.render(spriteBatch, shaderHandler);
        if(pellet!=null) {pellet.render(spriteBatch, shaderHandler);}
        for(int i=0; i<rope.size(); i++) {
            rope.get(i).render(spriteBatch, shaderHandler);
        }
        joint.render(spriteBatch, shaderHandler);
    }

    @Override
    public void dispose(World world) {
        world.destroyBody(joint.getBody());
        for(int i=0; i<rope.size(); i++) {
            world.destroyBody(rope.get(i).getBody());
        }
        rope.clear();
    }

    @Override
    public float getLowestY() {
        if(!blockinplace) {
            return blocker.getPosition().y - radius;
        } else {
            return blocker.getBody().getPosition().y - radius;
        }
    }

    @Override
    public void notifyCollision(com.pendulum.game.objects.Entity pellet) {
        if(this.pellet == pellet) {
            collected = true;
        }
    }

    @Override
    public void removeBlocker(World world) {
        blocker.setPosition(new Vector2(blocker.getBody().getPosition()));
        blocker.setAngle(0.0f);
        blocker.setTextureid(16);
        world.destroyBody(blocker.getBody());
        blockinplace = false;
    }

    @Override
    public void action(String action, World world) {
        if(hit && !broke) {
            if (action == "break_rope") {
                world.destroyJoint(connection);
                broke = true;
            }
        }
    }

    @Override
    public void setTint(Color tint) {

    }
}
