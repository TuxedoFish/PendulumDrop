package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 24/01/2017.
 */

public class Entity{
    private final Body mbody;
    private ArrayList<Texture> mtextures;
    private Vector2 size;
    private Boolean dead = false;
    private int currenttex = 0;
    private String type = "default";
    private Vector2 position = null;
    private float angle = -1;

    private Color tint;

    private float transparency = 1.0f;

    public Entity(Body pbody, Texture texture, Vector2 size, String type) {
        tint = new Color(1.0f, 1.0f, 1.0f, 0.0f);
        pbody.setUserData(this);
        this.mbody = pbody;
        mtextures = new ArrayList<Texture>();
        this.mtextures.add(texture);
        this.size = size;
        this.type = type;
    }
    public void setTextureid(int textureid) {
        this.currenttex=textureid;
    }
    public int getTextureid() {
        return currenttex;
    }
    public float getTransparency() {
        return transparency;
    }
    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }
    public String getType() {
        return type;
    }
    public void setPosition(Vector2 position) {
        this.position = new Vector2(position);
    }
    public Vector2 getPosition() {return position;}
    public void setAngle(float angle) {
        this.angle = angle;
    }
    public Vector2 getSize() {
        return size;
    }
    public void addTex(Texture texture) {
        mtextures.add(texture);
    }
    public void setTex(Texture texture) {
        mtextures.set(0, texture);
    }
    public void nextTex() {
        currenttex+=1;
        if(currenttex==mtextures.size()) {
            currenttex=0;
        }
    }
    public boolean isDead() {
        return dead;
    }
    public void setTint(float r, float g, float b) {
        tint.set(r,g,b,tint.a);
    }
    public void setDead(boolean dead) {
        this.dead = dead;
    }
    public Body getBody() {
        return mbody;
    }
    public void draw(float currentangle, Vector2 currentpos, SpriteBatch spriteBatch) {
        if (type == "platformL") {
            float ydisplacement = 0.0f;
            float xdisplacement = 0.0f;
            if(currentangle!=0) {
                ydisplacement=+4.0f;
                xdisplacement-=0.85f;
            }
            //draw end part
            if(size.y<2.0f) {
                spriteBatch.draw(mtextures.get(currenttex + 1), (currentpos.x + xdisplacement + (size.x / 2) - (size.y * 1.5f)) * PPM,
                        (currentpos.y + ydisplacement - (size.y / 2)) * PPM, ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.y * PPM, size.y * PPM, 1.0f, 1.0f,
                        (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex + 1).getWidth(), mtextures.get(currenttex + 1).getHeight(), false, false);
            } else {
                spriteBatch.draw(mtextures.get(currenttex + 1), (currentpos.x + xdisplacement + (size.x / 2) - (size.y)) * PPM,
                        (currentpos.y + ydisplacement - (size.y / 2)) * PPM, ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.y * PPM, size.y * PPM, 1.0f, 1.0f,
                        (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex + 1).getWidth(), mtextures.get(currenttex + 1).getHeight(), false, false);
            }
            //draw platform
            spriteBatch.draw(mtextures.get(currenttex),
                    (currentpos.x - (size.x / 2) - 0.5f) * PPM, (currentpos.y - (size.y / 2)) * PPM
                    , ((size.x / 2) * PPM), ((size.y / 2) * PPM), (size.x-0.5f) * PPM, size.y * PPM, 1.0f, 1.0f,
                    (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0,
                    mtextures.get(currenttex).getWidth(), mtextures.get(currenttex).getHeight(), false, false);
        } else if (type == "platformR") {
            float ydisplacement = 0.0f;
            if(currentangle!=0) {
                ydisplacement=+0.40f;
            }//draw end part
            if(size.y<2.0f) {
                spriteBatch.draw(mtextures.get(currenttex+1), (currentpos.x - (size.x / 2)) * PPM, (currentpos.y + ydisplacement - (size.y / 2)) * PPM
                        , ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.y*PPM, size.y*PPM, 1.0f, 1.0f, (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex+1).getWidth(),
                        mtextures.get(currenttex+1).getHeight(), false, false);
            } else {
                spriteBatch.draw(mtextures.get(currenttex+1), (currentpos.x - (size.x / 2)) * PPM, (currentpos.y + ydisplacement  - (size.y / 2)) * PPM
                        , ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.y*PPM, size.y*PPM, 1.0f, 1.0f, (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex+1).getWidth(),
                        mtextures.get(currenttex+1).getHeight(), false, false);
            }
            //draw platform
            spriteBatch.draw(mtextures.get(currenttex), (currentpos.x - (size.x / 2) + (size.y/2.0f)) * PPM, (currentpos.y - (size.y / 2)) * PPM
                    , ((size.x / 2) * PPM), ((size.y / 2) * PPM), (size.x-0.5f) * PPM, size.y * PPM, 1.0f, 1.0f, (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex).getWidth(),
                    mtextures.get(currenttex).getHeight(), false, false);
        } else if(type.contains("post")){
            if(type == "postU") {
                spriteBatch.draw(mtextures.get(currenttex), (currentpos.x - (size.x / 2)) * PPM, (currentpos.y - (1.0f / 2.0f)) * PPM
                        , ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.x * PPM, size.y * PPM, 1.0f, 1.0f, (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex).getWidth(),
                        mtextures.get(currenttex).getHeight(), false, false);
            } else {
                spriteBatch.draw(mtextures.get(currenttex), (currentpos.x - (size.x / 2)) * PPM, (currentpos.y + (1.0f / 2.0f) - size.y) * PPM
                        , ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.x * PPM, size.y * PPM, 1.0f, 1.0f, (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex).getWidth(),
                        mtextures.get(currenttex).getHeight(), false, true);
            }
        } else {
            spriteBatch.draw(mtextures.get(currenttex), (currentpos.x - (size.x / 2)) * PPM, (currentpos.y - (size.y / 2)) * PPM
                        , ((size.x / 2) * PPM), ((size.y / 2) * PPM), size.x * PPM, size.y * PPM, 1.0f, 1.0f, (float) (currentangle * (360.0f / (2.0f * Math.PI))), 0, 0, mtextures.get(currenttex).getWidth(),
                        mtextures.get(currenttex).getHeight(), false, false);
        }
    }
    public void setSize(Vector2 size) {
        this.size=size;
    }
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        Color c = spriteBatch.getColor();
        spriteBatch.setColor(new Color(tint.r, tint.g, tint.b, transparency));

        if (position != null) {
            draw(angle, position, spriteBatch);
        } else {
            draw(mbody.getAngle(), mbody.getPosition(), spriteBatch);
        }
    }
}
