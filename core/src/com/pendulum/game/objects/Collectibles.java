package com.pendulum.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 12/03/2017.
 */

public class Collectibles {
    private float WORLD_WIDTH, REAL_WORLD_HEIGHT;

    private ArrayList<BallUpgrade> ballupgrades = new ArrayList<BallUpgrade>();
    private ArrayList<Entity> collectibleplatforms = new ArrayList<Entity>();
    private ArrayList<Integer> prices = new ArrayList<Integer>();
    private ArrayList<String> textureballnames = new ArrayList<String>();
    private ArrayList<String> textureropenames = new ArrayList<String>();
    private ArrayList<String> texturerodnames = new ArrayList<String>();

    private com.pendulum.game.utils.Preferences preferences;

    private int selected = 0;

    private int spawnnumber = 0;

    private com.pendulum.game.texture.TextureHolder textureHolder;

    private BitmapFont font;

    public Collectibles(float WORLD_WIDTH, float REAL_WORLD_HEIGHT, com.pendulum.game.texture.TextureHolder textureHolder) {
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;
        this.textureHolder = textureHolder;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(("fonts/OpenSans-Bold.ttf")));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        font = generator.generateFont(parameter);
        generator.dispose();

        preferences = new com.pendulum.game.utils.Preferences();
        preferences.setUnlocked(0, true);

//        for(int i=0; i<50; i++) {
//            preferences.setUnlocked(i, false);
//        }

        prices.add(250); prices.add(500); prices.add(750); prices.add(1000); prices.add(1500); prices.add(2000);
        prices.add(2500); prices.add(5000); prices.add(10000);

        textureballnames.add("orange.png"); textureballnames.add("purple.png"); textureballnames.add("yellow.png");
        textureballnames.add("aztec_1.png"); textureballnames.add("tyre.png"); textureballnames.add("yin_yang.png");
        textureballnames.add("fruit_orange.png"); textureballnames.add("fruit_watermelon.png"); textureballnames.add("fruit_kiwi.png");

        textureropenames.add("orange.png"); textureropenames.add("purple.png"); textureropenames.add("yellow.png");
        textureropenames.add("aztec_1.png"); textureropenames.add("aztec_1.png"); textureropenames.add("aztec_1.png");
        textureropenames.add("orange.png"); textureropenames.add("green.png"); textureropenames.add("brown.png");

        texturerodnames.add("rope_orange.png"); texturerodnames.add("rope_purple.png"); texturerodnames.add("rope_yellow.png");
        texturerodnames.add("rod_aztec_1.png"); texturerodnames.add("rod_aztec_1.png"); texturerodnames.add("rod_aztec_1.png");
        texturerodnames.add("rope_orange.png"); texturerodnames.add("rope_green.png"); texturerodnames.add("rope_brown.png");
    }

    public void transpose() {

    }

    public int getSelected() {
        return selected;
    }

    public String getBallName() {
        return textureballnames.get(selected);
    }

    public String getRopeName() {
        return textureropenames.get(selected);
    }

    public String getRodName() {
        return texturerodnames.get(selected);
    }

    public void setupcollectibles(float ypos, World world, Color tint) {
        spawnnumber = 0;
        Vector2 platformpos = new Vector2(0.0f, (ypos/PPM)+(float)(REAL_WORLD_HEIGHT/PPM*0.4f));
        float platformwidth = WORLD_WIDTH/PPM*0.6f;
        float ballheight = 5f;
        float angle = (float)(Math.PI/8.0f);
        addCollectibleBranch(world, platformpos, angle, platformwidth, ballheight, false);

        platformpos = new Vector2(WORLD_WIDTH/PPM-(float)((platformwidth*0.85f)*Math.cos(angle)), (ypos/PPM)+(float)(REAL_WORLD_HEIGHT/PPM*0.85f));
        angle = -(float)(Math.PI/8.0f);
        addCollectibleBranch(world, platformpos, angle, platformwidth, ballheight, true);

        platformpos = new Vector2(0.0f, (ypos/PPM)+(float)(REAL_WORLD_HEIGHT/PPM*1.35f));
        angle = (float)(Math.PI/8.0f);
        addCollectibleBranch(world, platformpos, angle, platformwidth, ballheight, false);

        for(int i=0; i<collectibleplatforms.size(); i++) {
            collectibleplatforms.get(i).setTint(tint.r, tint.g, tint.b);
        }
        ballupgrades.get(selected).enable();
    }
    public void setSkin(int selected) {
        this.selected = selected;
    }
    public void dispose(World world) {
        for(int i=0; i<collectibleplatforms.size(); i++) {
            world.destroyBody(collectibleplatforms.get(i).getBody());
        }
        collectibleplatforms.clear();
        for(int i=0; i<ballupgrades.size(); i++) {
            ballupgrades.get(i).dispose(world);
        }
        ballupgrades.clear();
    }
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler) {
        for(int i=0; i<ballupgrades.size(); i++) {
            ballupgrades.get(i).render(spriteBatch, shaderHandler);
        }
        for(int i=0; i<collectibleplatforms.size(); i++) {
            collectibleplatforms.get(i).render(spriteBatch, shaderHandler);
        }
    }
    public void controlCollectibleBalls(float dt, World world, com.pendulum.game.texture.TextureHolder textureHolder) {
        for(int i=0; i<ballupgrades.size(); i++) {
            ballupgrades.get(i).update(dt, world, textureHolder);
        }
    }
    public String isTouching(Vector3 mousepos, int pellets) {
        boolean isunlocked;
        for(int i=0; i<ballupgrades.size(); i++) {
            isunlocked=ballupgrades.get(i).isUnlocked();
            if(ballupgrades.get(i).isTouching(mousepos, pellets)) {
                selected = i;
                for(int j=0; j<ballupgrades.size(); j++) {
                    if(j!=selected) {
                        ballupgrades.get(j).disable();
                    }
                }
                preferences.setUnlocked(i, true);
                if(isunlocked) {
                    return "1";
                } else {
                    return String.valueOf(ballupgrades.get(i).isUnlocked()) + String.valueOf(i);
                }
            }
        }
        return "-1";
    }
    public int getPrice(int i) {
        return ballupgrades.get(i).getPrice();
    }
    public void addCollectibleBranch(World world, Vector2 platformpos, float angle, float platformwidth, float ballheight, boolean right) {
        float xdisplacement = 0.0f;
        if(right) {xdisplacement = 3f;} else {xdisplacement = 5f;}

        Entity platform;
        if(right) {
            platform = new Entity(ObjectHandler.createBackgroundBody(platformpos,
                    world, platformwidth, !right, true, 1.0f), textureHolder.getTexture("platform.png"), new Vector2(platformwidth, 2f), "platformR");
        } else {
            platform = new Entity(ObjectHandler.createBackgroundBody(platformpos,
                    world, platformwidth, !right, true, 1.0f), textureHolder.getTexture("platform.png"), new Vector2(platformwidth, 2f), "platformL");
        }
        platform.getBody().setTransform(platform.getBody().getPosition().x+(WORLD_WIDTH/PPM/4), platform.getBody().getPosition().y, angle);
        ballupgrades.add(new BallUpgrade(world, platformpos, platformwidth, xdisplacement, angle, textureHolder, platform, ballheight, font, prices.get(spawnnumber)
                ,textureballnames.get(spawnnumber), textureropenames.get(spawnnumber), preferences.getUnlocked(spawnnumber)));
        spawnnumber += 1;
        if(spawnnumber >= textureballnames.size()) {spawnnumber=0;}

        if(right) {xdisplacement = -1.0f;} else {xdisplacement = 1.0f;}
        ballupgrades.add(new BallUpgrade(world, platformpos, platformwidth, xdisplacement, angle, textureHolder, platform, ballheight, font, prices.get(spawnnumber)
                ,textureballnames.get(spawnnumber), textureropenames.get(spawnnumber), preferences.getUnlocked(spawnnumber)));
        spawnnumber += 1;
        if(spawnnumber >= textureballnames.size()) {spawnnumber=0;}

        if(right) {xdisplacement = -5.0f;} else {xdisplacement = -3.0f;}
        ballupgrades.add(new BallUpgrade(world, platformpos, platformwidth, xdisplacement, angle, textureHolder, platform, ballheight, font, prices.get(spawnnumber)
                ,textureballnames.get(spawnnumber), textureropenames.get(spawnnumber), preferences.getUnlocked(spawnnumber)));
        spawnnumber += 1;
        if(spawnnumber >= textureballnames.size()) {spawnnumber=0;}

        collectibleplatforms.add(platform);
        platform.addTex(textureHolder.getTexture("platformend.png"));
    }
}
