package com.pendulum.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.state.PlayScreen;
import com.pendulum.game.utils.Preferences;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.CATEGORY_JOINTS;
import static com.pendulum.game.utils.Constants.CATEGORY_PLAYER;
import static com.pendulum.game.utils.Constants.JOINT_RADIUS;
import static com.pendulum.game.utils.Constants.MASK_JOINTS;
import static com.pendulum.game.utils.Constants.MASK_PLAYER;
import static com.pendulum.game.utils.Constants.PLAYER_RADIUS;
import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 24/01/2017.
 */

public class EntityHandler {
    private BitmapFont font;
    private BitmapFont futuristicfont;
    private BitmapFont gamemodefont;
    private GlyphLayout highscore_word;
    private GlyphLayout highscore;
    private GlyphLayout game_word;
    private GlyphLayout over_word;
    private GlyphLayout gamemode_word;
    private GlyphLayout gamemode_word_static;
    private String gamemode;
    private float gamemode_pan = 0.0f;
    private float gamemode_transparency = 1.0f;
    private Preferences preferences;

    private ArrayList<com.pendulum.game.objects.Entity> rope;
    private com.pendulum.game.objects.Entity rod;
    private com.pendulum.game.objects.Entity player;
    private com.pendulum.game.objects.Entity joint_initial;
    private Vector2 joint_initial_location;
    private com.pendulum.game.objects.Entity joint;

    private float radius = 7.0f;

    private com.pendulum.game.objects.Entity button_replay;

    private float pan = 0.0f;
    private com.pendulum.game.objects.Entity shop_icon;
    private com.pendulum.game.objects.Entity swipe_icon;
    private com.pendulum.game.objects.Entity swipe_icon_down;
    private com.pendulum.game.objects.Entity shop_leave_icon;
    private float time_gameover = 0.0f;

    private boolean button_down = false;
    private boolean button_pressed = false;
    private boolean button_toshop = false;
    private boolean button_togameover = false;

    private boolean gameover = false;
    private boolean gameoverrender = false;
    private float player_transparency;
    public Collectibles collectibles;

    private boolean loaded = false;

    private float timeperiod = 2.0f;
    private float angularspeed = (float)((2.0f*Math.PI)/timeperiod);
    private float amplitude = (float)(Math.PI/4.0f);

    private float WORLD_WIDTH;
    private float REAL_WORLD_HEIGHT;

    private float gameover_angle;

    private float distance;
    private float time = 0.0f;

    private float lastdeathheight = 0.0f;

    private PlatformHandler platformHandler;

    private com.pendulum.game.texture.TextureHolder textureHolder;

    public EntityHandler(float WORLD_WIDTH, float REAL_WORLD_HEIGHT, World world, com.pendulum.game.texture.TextureHolder textureHolder) {
        rope = new ArrayList<com.pendulum.game.objects.Entity>();

        this.WORLD_WIDTH = WORLD_WIDTH;
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;

        this.textureHolder = textureHolder;

        lastdeathheight = (REAL_WORLD_HEIGHT * 3.5f);

        collectibles = new Collectibles(WORLD_WIDTH, REAL_WORLD_HEIGHT, textureHolder);

        player = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(WORLD_WIDTH/PPM/2, REAL_WORLD_HEIGHT/PPM*0.75f),
                PLAYER_RADIUS, world, BodyDef.BodyType.DynamicBody, CATEGORY_PLAYER, MASK_PLAYER), textureHolder.getTexture("ball_fred.png"), new Vector2(PLAYER_RADIUS, PLAYER_RADIUS), "player");
        player.addTex(textureHolder.getTexture("ball_purple.png")); player.addTex(textureHolder.getTexture("ball_yellow.png"));
        player.addTex(textureHolder.getTexture("aztec_1.png")); player.addTex(textureHolder.getTexture("ball_tyre.png")); player.addTex(textureHolder.getTexture("yin_yang.png"));
        player.addTex(textureHolder.getTexture("fruit_orange.png")); player.addTex(textureHolder.getTexture("fruit_watermelon.png")); player.addTex(textureHolder.getTexture("fruit_kiwi.png"));
    }
    public void setSkin(int selected) {
        collectibles.setSkin(selected);
        player.setTextureid(selected);
    }
    public String checkCollectibles (Vector3 mousepos) {
        String selected = collectibles.isTouching(mousepos, platformHandler.getPelletCount());
        if(selected.contains("false")) {
            platformHandler.changePelletCount(-(collectibles.getPrice(Integer.valueOf(selected.substring(selected.length() - 1)))));
        }
        return selected;
    }
    public void pan(float ypan) {
        this.pan = ypan;
    }
    public float getTime() {
        return platformHandler.getTime();
    }
    public void resetTime() {
        platformHandler.resetTime();
    }
    public void notifyCollision(com.pendulum.game.objects.Entity pellet) {
        platformHandler.notifyCollision(pellet);
    }
    public void moveJoint(Vector3 pos) {
        if(joint != null) {
            joint.getBody().setTransform(pos.x / PPM, pos.y / PPM, joint.getBody().getAngle());
        }
    }
    public int getPelletCount() {
        return platformHandler.getPelletCount();
    }
    public void create(World world, int pellet_count, PlayScreen parent) {
        preferences = new Preferences();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(("fonts/OpenSans-Bold.ttf")));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        generator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        parameter.size = (int)(80.0f*(Gdx.graphics.getHeight()/2000.0f));
        parameter.color = new Color(174.0f/255.0f, 129.0f/255.0f, 255.0f/255.0f, 1.0f);
        parameter.borderColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        parameter.borderWidth = (int)(5f*(Gdx.graphics.getHeight()/2000.0f));
        font = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter_gamemode = new FreeTypeFontGenerator.FreeTypeFontParameter();
        generator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        parameter_gamemode.size = (int)(50.0f*(Gdx.graphics.getHeight()/2000.0f));
        parameter_gamemode.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        parameter_gamemode.borderWidth = 0;
        gamemodefont = generator.generateFont(parameter_gamemode);

        generator.dispose();

        highscore = new GlyphLayout();
        highscore.setText(font, String.valueOf(preferences.getHighScore()));
        highscore_word = new GlyphLayout();
        highscore_word.setText(font, "highscore");

        FreeTypeFontGenerator generator_futuristic = new FreeTypeFontGenerator(Gdx.files.internal(("fonts/Anurati-Regular.otf")));
        generator_futuristic.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);

        FreeTypeFontGenerator.FreeTypeFontParameter gameoverparameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        gameoverparameter.size = (int)(120.0f*(Gdx.graphics.getHeight()/2000.0f));
        gameoverparameter.color = new Color(174.0f/255.0f, 129.0f/255.0f, 255.0f/255.0f, 1.0f);
        gameoverparameter.borderColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        gameoverparameter.borderWidth = (int)(8.0f*(Gdx.graphics.getHeight()/2000.0f));;

        futuristicfont = generator_futuristic.generateFont(gameoverparameter);
        generator_futuristic.dispose();

        game_word = new GlyphLayout();
        game_word.setText(futuristicfont, "GAME");
        over_word = new GlyphLayout();
        over_word.setText(futuristicfont, "OVER");

        player.getBody().setActive(true);
        setupStartScreen(0.0f, world);

        platformHandler = new PlatformHandler(world, WORLD_WIDTH, REAL_WORLD_HEIGHT, textureHolder, pellet_count, parent, collectibles.getRopeName(), collectibles.getBallName(), collectibles.getRodName());
        gamemode_word_static = new GlyphLayout();
        gamemode_word_static.setText(gamemodefont, "Game Mode");
        gamemode_word = new GlyphLayout();
        gamemode_word.setText(gamemodefont, "< " + platformHandler.getGameMode().substring(0, 1) + platformHandler.getGameMode().toLowerCase().substring(1) + " >");
    }
    public float getLowestY() {
        return platformHandler.getLowestY();
    }
    public String getGameMode() {
        return platformHandler.getGameMode();
    }
    public void start(World world) {
        float angle = (float)(amplitude*Math.cos(angularspeed*time));
        float radius = (float)distance/2;
        float velocity = radius*(float)(-amplitude*angularspeed*(Math.sin(angularspeed*time)));

        world.destroyJoint(player.getBody().getJointList().get(0).joint);
        world.destroyBody(rod.getBody());
        rod=null;

        player.getBody().setActive(true);

        player.getBody().setLinearVelocity((float)(velocity*Math.cos(angle)), (float)(velocity*Math.sin(angle)));

        collectibles.dispose(world);

        //CLEAR THE COLLECTIBLES STUFF
        if(swipe_icon_down!=null) {world.destroyBody(swipe_icon_down.getBody());}
        swipe_icon_down= null;
        if(shop_leave_icon!=null) {world.destroyBody(shop_leave_icon.getBody());}
        swipe_icon_down= null;
        if(shop_icon!=null) {world.destroyBody(shop_icon.getBody());}
        shop_icon= null;
        if(swipe_icon!=null) {world.destroyBody(swipe_icon.getBody());}
        swipe_icon= null;

    }
    public Body getPlayer() {
        return player.getBody();
    }
    public void respawn(World world) {
        collectibles.transpose();
    }
    public void reloadGame(World world) {
        clearPlatforms(world);
        setupcollectibles(lastdeathheight - (REAL_WORLD_HEIGHT * 1.0f), world);
    }
    public void loadStartScreen(World world) {
        destroyJoint(world);
        setupStartScreen(lastdeathheight - (REAL_WORLD_HEIGHT * 3.5f), world);
        setup(world, lastdeathheight - (REAL_WORLD_HEIGHT * 3.5f));
    }
    public void died(World world, Camera camera) {
        lastdeathheight = camera.position.y;
        newCycle(lastdeathheight, world);
        // setupGameoverScreen(lastdeathheight - (REAL_WORLD_HEIGHT*2.0f), world);
        destroyJoint(world);
        gameover=true;
        highscore.setText(font, String.valueOf(preferences.getHighScore()));
    }

    public float getLastDeathHeight() {
        return lastdeathheight;
    }

    public void setupcollectibles(float ypos, World world) {
        collectibles.setupcollectibles(ypos, world, platformHandler.getCurrent_tint());

        shop_leave_icon = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2((WORLD_WIDTH/PPM*0.8f), ((lastdeathheight - (REAL_WORLD_HEIGHT*2.0f))/PPM) + (REAL_WORLD_HEIGHT/PPM*1.3f)), 2.5f, 2.5f, world), textureHolder.getTexture("slider.png"),
                new Vector2(2.5f, 2.5f), "emoji");
        swipe_icon_down = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2((WORLD_WIDTH/PPM*0.8f), ((lastdeathheight - (REAL_WORLD_HEIGHT*2.0f))/PPM) + (REAL_WORLD_HEIGHT/PPM*1.3f) - 2.5f), 5.0f/3.0f, 5f, world), textureHolder.getTexture("arrow_invert.png"),
                new Vector2(5.0f/3.0f, 5f), "emoji");
    }

    public void createRope(Vector3 position, World world) {
        joint = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(position.x / PPM,
                        position.y / PPM),
                JOINT_RADIUS, world, BodyDef.BodyType.StaticBody, CATEGORY_JOINTS, MASK_JOINTS), textureHolder.getTexture("holder.png"), new Vector2(JOINT_RADIUS, JOINT_RADIUS), "joint");

        rope = com.pendulum.game.objects.ObjectHandler.createRope(joint.getBody(), player.getBody(), world, "ROPE", textureHolder, 0.0f, "rope_" + collectibles.getRopeName());
    }

    public void startPlatform(World world) {
        start(world);

        platformHandler.start(world, collectibles.getRopeName(), collectibles.getBallName(), collectibles.getRodName());
    }

    public void destroyJoint(World world) {
        if(joint!=null) {
            world.destroyBody(joint.getBody());
            joint=null;
        }
        if(rope.size()!=0) {
            for (int j = 0; j < rope.size(); j++) {
                for(int k=0; k<rope.get(j).getBody().getJointList().size; k++) {
                    world.destroyJoint(rope.get(j).getBody().getJointList().get(k).joint);
                }
            }
            for (int j = 0; j < rope.size(); j++) {
                world.destroyBody(rope.get(j).getBody());
            }
            rope.clear();
        }
    }

    public void setupStartScreen(float topy, World world) {

        joint_initial = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(WORLD_WIDTH/PPM/2, (topy/PPM) + REAL_WORLD_HEIGHT/PPM/2),
                PLAYER_RADIUS, world, BodyDef.BodyType.StaticBody, CATEGORY_PLAYER, MASK_PLAYER), textureHolder.getTexture("holder.png"), new Vector2(JOINT_RADIUS, JOINT_RADIUS), "joint");

        joint_initial_location = new Vector2(joint_initial.getBody().getPosition());
        distance = 8.0f;
        double angle = Math.PI/4;

        player.getBody().setTransform((float)(joint_initial.getBody().getPosition().x + (distance*Math.cos(angle))),
                (float)(joint_initial.getBody().getPosition().y - (distance*Math.sin(angle))), 0);
        player.getBody().setLinearVelocity(0.0f, 0.0f);
        player.getBody().setFixedRotation(false);

        // Get the rod name
        String rodName = collectibles.getRodName();

        this.rod = com.pendulum.game.objects.ObjectHandler.createRope(joint_initial.getBody(), player.getBody(),
                world, "ROD", textureHolder, 0.0f, rodName).get(0);

        rod.getBody().setActive(false);
        player.getBody().setActive(false);
        joint_initial.getBody().setActive(false);
        joint_initial.getBody().setActive(false);
        time=0;
        player.setTextureid(collectibles.getSelected());
        player.setTransparency(1.0f);
        player_transparency = 1.0f;
    }

    public int getTotalLevel() {
        return platformHandler.getTotalLevel();
    }

    public void setupGameoverScreen(float topy, World world) {
        gameoverrender = true;

        shop_icon = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2((WORLD_WIDTH/PPM/2.0f), (topy/PPM) + (REAL_WORLD_HEIGHT/PPM*0.85f)), 2.5f, 2.5f, world), textureHolder.getTexture("shop.png"),
                new Vector2(2.5f, 2.5f), "emoji");
        swipe_icon = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2((WORLD_WIDTH/PPM/2.0f), (topy/PPM) + (REAL_WORLD_HEIGHT/PPM*0.85f) - 2.5f), 5.0f/3.0f, 5f, world), textureHolder.getTexture("arrow.png"),
                new Vector2(5.0f/3.0f, 5f), "emoji");

        radius = (REAL_WORLD_HEIGHT/PPM*0.2f);
        radius-=0.5f;

        button_replay = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2((WORLD_WIDTH/PPM/2.0f), (topy/PPM) + (REAL_WORLD_HEIGHT/PPM*0.2f)), radius, radius, world), textureHolder.getTexture("replay.png"),
                new Vector2(2*radius, 2*radius), "emoji");
    }
    public void disposeGameoverScreen(World world) {
//        world.destroyBody(shop_icon.getBody());
//        world.destroyBody(swipe_icon.getBody());
//        world.destroyBody(button_replay.getBody());
//        world.destroyBody(shop_leave_icon.getBody());
//        world.destroyBody(swipe_icon_down.getBody());
//        shop_icon = null;
//        swipe_icon = null;
//        button_replay = null;
//        shop_leave_icon = null;
//        swipe_icon_down = null;
    }

    public float getGamemodeTransparency() {
        return gamemode_transparency;
    }

    public int update(Vector2 position, World world, float dt, String state) {
        if(gamemode_pan!=platformHandler.getPan()) {
            gamemode_pan=platformHandler.getPan();
            if(Math.abs(gamemode_pan)>0.5f) {
                gamemode_transparency=(Math.abs(gamemode_pan)-0.6f)*2.5f;
                if(gamemode_transparency<0.0f) {gamemode_transparency=0.0f;}
            } else {
                gamemode_transparency=1.0f-(Math.abs(gamemode_pan)*3.0f);
                if(gamemode_transparency<0.0f) {gamemode_transparency=0.0f;}
            }
            gamemode_word.setText(gamemodefont, "< " + platformHandler.getGameMode().substring(0, 1) + platformHandler.getGameMode().toLowerCase().substring(1) + " >", new Color(1.0f, 1.0f, 1.0f, gamemode_transparency), gamemode_word.width, 1, false);
            gamemode_word_static.setText(gamemodefont, "Game Mode", new Color(1.0f, 1.0f, 1.0f, gamemode_transparency), gamemode_word_static.width, 1, false);
        }
        if(gameover) {
            // MAKES PLAYER DISSAPPEAR ON GAME OVER
//            player_transparency -= dt*3.0f;
//            if(player_transparency<0.0f) {
//                player_transparency = 0.0f;
//                gameover = false;
//                player.getBody().setActive(false);
//            }
//            player.setTransparency(player_transparency);
        }
        if(gameoverrender) {
            time_gameover += dt;
            if(time_gameover>=timeperiod) {
                time_gameover = 0.0f;
            }
            gameover_angle = (float)(amplitude*Math.cos(angularspeed*time_gameover));
        }
        if(platformHandler.getLevel()-5>2 && joint_initial!=null) {
            world.destroyBody(joint_initial.getBody());
            joint_initial = null;
        }
        return platformHandler.update(position, world, player, dt, collectibles.getRopeName(), state, collectibles.getBallName(), collectibles.getRodName());
    }
    public void setup(World world, float position) {
        this.platformHandler.setup(world, position, collectibles.getRopeName(), collectibles.getBallName(), collectibles.getRodName());
    }
    public void clearPlatforms(World world) {
        this.platformHandler.clearPlatforms(world);
    }
    public void newCycle(float currentheight, World world) {
        this.platformHandler.newCycle(currentheight, world);
    }
    public void controlStartingRod(float dt) {
        time += dt;
        if(time>=timeperiod) {
            time = 0.0f;
        }
        float angle = (float)(amplitude*Math.cos(angularspeed*time));

        joint_initial.getBody().setTransform(joint_initial_location, joint_initial.getBody().getAngle());
        rod.getBody().setTransform((float)(joint_initial.getBody().getPosition().x+(distance/2.0f*Math.sin(angle))), (float)(joint_initial.getBody().getPosition().y-(distance/2.0f*Math.cos(angle))), angle);
        player.getBody().setTransform((float) (joint_initial.getBody().getPosition().x + (distance * Math.sin(angle))), (float) (joint_initial.getBody().getPosition().y - (distance * Math.cos(angle))), player.getBody().getAngle());
    }
    public void controlCollectibleBalls(float dt, World world) {
        collectibles.controlCollectibleBalls(dt, world, textureHolder);
    }
    public void destroyConnection(World world) {
        platformHandler.destroyConnection(world);
    }
    public void play(World world) {
        gameoverrender = false;
        disposeGameoverScreen(world);
    }
     public void gamemodeChange(String action) {
        platformHandler.gamemodeChange(action);
    }
    public boolean isScrolling() {
        return platformHandler.isScrolling();
    }
    public void setXPan(float xpan) {
        platformHandler.setMovement(false);
        platformHandler.updatePan(xpan);
    }
    public float getAngle() {
        return (float)(amplitude*Math.cos(angularspeed*time));
    }
    public int render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler, String state) {
        int selected = -1;

        collectibles.render(spriteBatch, shaderHandler);

        if(player.getTextureid()!=collectibles.getSelected()) {
            selected = collectibles.getSelected();
            player.setTextureid(collectibles.getSelected());
        }

        if(button_replay!=null) {
            if (button_down) {
                button_replay.setSize(new Vector2((radius * 2.0f) - 2.0f, (radius * 2.0f) - 2.0f));
            } else {
                button_replay.setSize(new Vector2(radius * 2.0f, radius * 2.0f));
            }
        }
        if(gameoverrender) {
            if(swipe_icon_down!=null) {
                if(pan<=0.0f) {
                    shop_leave_icon.getBody().setTransform(new Vector2(shop_leave_icon.getBody().getPosition().x,
                            (swipe_icon_down.getBody().getPosition().y + (shop_leave_icon.getSize().y/2.0f)- (swipe_icon_down.getSize().y/2.0f)) - (float) (100.0f / PPM / 2.0f * Math.cos(gameover_angle)) - (pan / 3.0f / PPM)), shop_icon.getBody().getAngle());
                }
                swipe_icon_down.render(spriteBatch, shaderHandler);
                shop_leave_icon.render(spriteBatch, shaderHandler);
            }
            button_replay.getBody().setTransform(button_replay.getBody().getPosition(), (gameover_angle+amplitude)*2.5f);
            button_replay.render(spriteBatch, shaderHandler);
            swipe_icon.render(spriteBatch, shaderHandler);
            if(pan>=0.0f) {
                shop_icon.getBody().setTransform(new Vector2((WORLD_WIDTH/PPM/2.0f), swipe_icon.getBody().getPosition().y + shop_icon.getSize().y - (float) (100.0f / PPM / 2.0f * Math.cos(gameover_angle)) - (pan / 3.0f / PPM)), shop_icon.getBody().getAngle());
            }
            shop_icon.render(spriteBatch, shaderHandler);
            font.draw(spriteBatch, highscore_word, (WORLD_WIDTH/2.0f)-(highscore_word.width/2),
                    ((button_replay.getBody().getPosition().y)*PPM) + (REAL_WORLD_HEIGHT*0.35f) + (highscore.height*2.0f)-(float)(100.0f/2.0f*Math.cos(gameover_angle)));
            font.draw(spriteBatch, highscore, (WORLD_WIDTH/2.0f)-(highscore.width/2),
                    ((button_replay.getBody().getPosition().y)*PPM) + (REAL_WORLD_HEIGHT*0.35f) - (float)(100.0f/2.0f*Math.cos(gameover_angle)));
//            futuristicfont.draw(spriteBatch, game_word,
//                    ((gameover_position.x)*PPM)-(game_word.width/2)+(float)(150.0f/2.0f*Math.sin(gameover_angle)), (gameover_position.y)*PPM-(float)(100.0f/2.0f*Math.cos(gameover_angle)));
//            futuristicfont.draw(spriteBatch, over_word,
//                    ((gameover_position.x)*PPM)-(over_word.width/2)+(float)(150.0f/2.0f*Math.sin(gameover_angle)), (gameover_position.y)*PPM - game_word.height*1.5f-(float)(100.0f/2.0f*Math.cos(gameover_angle)));
        }

        platformHandler.render(spriteBatch, shaderHandler, state);

        //ADD IN FOR GAMEMODE SUPPORT
//        gamemodefont.draw(spriteBatch, gamemode_word, WORLD_WIDTH/2.0f - (gamemode_word.width/2.0f), (lastdeathheight - (REAL_WORLD_HEIGHT * 3.5f)) + (REAL_WORLD_HEIGHT*0.7f));
//        gamemodefont.draw(spriteBatch, gamemode_word_static, WORLD_WIDTH/2.0f - (gamemode_word_static.width/2.0f),
//                (lastdeathheight - (REAL_WORLD_HEIGHT * 3.5f)) + (REAL_WORLD_HEIGHT*0.7f) + (gamemode_word.height*1.5f));

        if(rod!=null) {rod.render(spriteBatch, shaderHandler);}
        for(int i=0; i<rope.size(); i++) {
            rope.get(i).render(spriteBatch, shaderHandler);
        }
        if(joint_initial!=null) {joint_initial.render(spriteBatch, shaderHandler);}
        if(joint!=null) {joint.render(spriteBatch, shaderHandler);}
        player.render(spriteBatch, shaderHandler);

        return selected;
    }
}
