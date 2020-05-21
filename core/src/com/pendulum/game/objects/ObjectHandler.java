package com.pendulum.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.CATEGORY_POST;
import static com.pendulum.game.utils.Constants.CATEGORY_ROPE;
import static com.pendulum.game.utils.Constants.CATEGORY_SCENERY;
import static com.pendulum.game.utils.Constants.CATEGORY_TEXT;
import static com.pendulum.game.utils.Constants.MASK_POST;
import static com.pendulum.game.utils.Constants.MASK_ROPE;
import static com.pendulum.game.utils.Constants.MASK_SCENERY;
import static com.pendulum.game.utils.Constants.MASK_TEXT;
import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 08/01/2017.
 */

public class ObjectHandler {

    public static ArrayList<Entity> createRope(Body joint, Body player, World world, String type, com.pendulum.game.texture.TextureHolder textureHolder, float xdisplacement, String texture) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Vector2 jointpos = new Vector2(joint.getPosition().x + (float)(xdisplacement*(float)Math.cos(joint.getAngle())), joint.getPosition().y + (float)(xdisplacement*(float)Math.sin(joint.getAngle())));

        Vector2 JtoP = new Vector2(jointpos.x - player.getPosition().x,
                jointpos.y - player.getPosition().y);
        double distance = Math.sqrt(((Math.pow(JtoP.x, 2)) + (Math.pow(JtoP.y, 2))));
        Vector2 JtoP_UNIT =  new Vector2((float)(JtoP.x/distance),
                (float)(JtoP.y/distance));

        int length = 1;
        float height = 0.5f;
        float originallength = (float)distance/height;

        if(type=="ROPE") {
            if (Math.round(originallength) != originallength) {
                length = Math.round(originallength);
                height = (float)(distance/(float)length);
            } else {
                length = (int)originallength;
            }
        } else {
            length = 1;
            height = (float)distance;
        }

        ArrayList<Entity> segments = new ArrayList<Entity>();
        RevoluteJoint[] joints = new RevoluteJoint[length+2];

        double degrees = 0.0f;
        if(joint.getPosition().x<player.getPosition().x) {
            degrees = Math.acos((JtoP.y)/(distance));
        } else {
            degrees = Math.acos((-JtoP.y)/(distance));
        }

        float width = 0.5f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = createFixture(shape, 1.0f, 0.5f, CATEGORY_ROPE, MASK_ROPE);

        for(int i=0; i < length; i++) {
            bodyDef.position.set((float)(jointpos.x - (JtoP_UNIT.x*(height*(i+0.5)))),
                    (float)(jointpos.y - (JtoP_UNIT.y*(height*(i+0.5)))));
            segments.add(segments.size(), new Entity(world.createBody(bodyDef), textureHolder.getTexture(texture), new Vector2(width+0.1f, height+0.1f), "rope"));
            segments.get(i).getBody().createFixture(fixtureDef);
            segments.get(i).getBody().setTransform(segments.get(i).getBody().getPosition(),(float)degrees);
        }

        shape.dispose();

        RevoluteJointDef jointDef = new RevoluteJointDef();

        if(joint.getPosition().x<player.getPosition().x) {
            jointDef.localAnchorB.y = (float) height / 2;
        } else {
            jointDef.localAnchorB.y = (float) -height / 2;
        }

        jointDef.localAnchorA.x = xdisplacement;

        jointDef.bodyA = joint;
        jointDef.bodyB = segments.get(0).getBody();
        joints[0] = (RevoluteJoint)world.createJoint(jointDef);

        jointDef.localAnchorA.x = 0.0f;

        for(int i=0; i<length-1; i++) {
            if(joint.getPosition().x<player.getPosition().x) {
                jointDef.localAnchorA.y = -height/2;
                jointDef.localAnchorB.y = height/2;
            } else {
                jointDef.localAnchorA.y = (float) height / 2;
                jointDef.localAnchorB.y = (float) -height / 2;
            }
            jointDef.bodyA = segments.get(i).getBody();
            jointDef.bodyB = segments.get(i+1).getBody();
            joints[i+1] = (RevoluteJoint)world.createJoint(jointDef);
        }

        jointDef.localAnchorB.y = 0.0f;

        if(joint.getPosition().x<player.getPosition().x) {
            jointDef.localAnchorA.y = (float) -height / 2;
        } else {
            jointDef.localAnchorA.y = (float) height / 2;
        }

        jointDef.bodyA = segments.get(segments.size()-1).getBody();
        jointDef.bodyB = player;
        joints[joints.length-1] = (RevoluteJoint)world.createJoint(jointDef);

        return segments;
    }
    public static Body createTextBox(Vector2 pos, float width, float height, World world) {
        Body pBody;
        FixtureDef fixtureDef;
        BodyDef def = new BodyDef();
        //Initialize body
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(pos.x, pos.y);
        pBody = world.createBody(def);
        //Add collision shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        //Add collision masks
        fixtureDef = createFixture(shape, 1.0f, 0.5f, CATEGORY_TEXT, MASK_TEXT);
        pBody.createFixture(fixtureDef);
        shape.dispose();

        return pBody;
    }
    public static Body createCollisionBody(Vector2 pos, float size, World world,
                                  BodyDef.BodyType bodyType, short category, short mask) {
        Body pBody;
        FixtureDef fixtureDef;
        BodyDef def = new BodyDef();
        //Initialize body
        def.type = bodyType;
        def.position.set(pos.x, pos.y);
        pBody = world.createBody(def);
        //Add collision shape
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2);
        //Add collision masks
        fixtureDef = createFixture(shape, 0.8f, 0.5f, category, mask);
        pBody.createFixture(fixtureDef);
        shape.dispose();

        return pBody;
    }
    public static Body[] createSlalom(Vector2 pos, World world, float width) {
        Body left;
        Body right;
        FixtureDef fixtureDef;
        BodyDef def = new BodyDef();
        //Initialize body
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(pos.x, pos.y);
        left = world.createBody(def);
        right = world.createBody(def);
        right.setTransform(pos.x + width, pos.y, 0.0f);
        //Add collision shape
        CircleShape shape = new CircleShape();
        shape.setRadius(1.0f / 2.0f);
        //Add collision masks
        fixtureDef = createFixture(shape, 1.0f, 0.5f, CATEGORY_POST, MASK_POST);
        left.createFixture(fixtureDef);
        right.createFixture(fixtureDef);
        shape.dispose();

        Body[] bodies = {left, right};
        return bodies;
    }
    public static Body createBackgroundBody(Vector2 pos, World world, float width, boolean left, boolean curved, float height) {
        Body pBody;
        FixtureDef fixtureDef;
        FixtureDef fixtureDefEnd;
        BodyDef def = new BodyDef();
        //Initialize body
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(pos.x, pos.y);
        pBody = world.createBody(def);
        //Add collision shape
        PolygonShape shape = new PolygonShape();
        CircleShape shapeend = new CircleShape();
        if(curved) {
            if (left) {
                shape.setAsBox((width / 2) - 0.5f, height, new Vector2(-0.5f, 0.0f), 0.0f);
                shapeend.setRadius(height);
                shapeend.setPosition(new Vector2(width / 2 - (height*2.0f), 0.0f));
            } else {
                shape.setAsBox((width / 2) - 0.5f, height, new Vector2(+0.5f, 0.0f), 0.0f);
                shapeend.setRadius(height);
                shapeend.setPosition(new Vector2((-width / 2) + height, 0.0f));
            }
        } else {
            shape.setAsBox((width / 2), height, new Vector2(0.0f, 0.0f), 0.0f);
        }
        //Add collision masks
        fixtureDef = createFixture(shape, 1.0f, 0.5f, CATEGORY_SCENERY, MASK_SCENERY);
        pBody.createFixture(fixtureDef);
        if(curved) {
            fixtureDefEnd = createFixture(shapeend, 1.0f, 0.5f, CATEGORY_SCENERY, MASK_SCENERY);
            pBody.createFixture(fixtureDefEnd);
        }
        shape.dispose();
        shapeend.dispose();

        return pBody;
    }
    public static Body[] createBackgroundSides(Vector2 pos, World world,
                                               float WORLD_WIDTH, float REAL_WORLD_HEIGHT) {
        Body[] sides = new Body[2];
        FixtureDef fixtureDef;
        BodyDef def = new BodyDef();
        //Initialize body
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(-0.01f, pos.y);
        sides[0] = world.createBody(def);
        sides[1] = world.createBody(def);
        sides[1].setTransform(WORLD_WIDTH/PPM+0.05f, sides[1].getPosition().y, sides[1].getAngle());
        //Add collision shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.01f, REAL_WORLD_HEIGHT/PPM);
        //Add collision masks
        fixtureDef = createFixture(shape, 1.0f, 0.5f, CATEGORY_SCENERY, MASK_SCENERY);
        sides[0].createFixture(fixtureDef);
        sides[1].createFixture(fixtureDef);
        shape.dispose();

        return sides;
    }
    private static FixtureDef createFixture(Shape shape, float density, float restitution, short category, short mask) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }
}
