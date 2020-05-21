package com.pendulum.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.collisions.CollisionDetector;
import com.pendulum.game.state.PlayScreen;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.PPM;
import static com.pendulum.game.utils.Constants.STEP;

/**
 * Created by Harry on 09/01/2017.
 */

public class PlatformHandler {
    private Body[] sides;
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    private float time = 0.0f;

    private String gamemode = "FALL";
    private int score = 0;

    private int level;
    private int totallevel=0;

    private int pellet_count = 0;

    private CollisionDetector collisionDetector;

    private float startingheight = 0.0f;

    private float currentx=0.0f;

    private float WORLD_WIDTH;
    private float REAL_WORLD_HEIGHT;

    private com.pendulum.game.objects.Entity fill_background;

    private boolean isScrolling = false;
    private float scrollto = 0.0f;
    private boolean move = true;

    private float fixedpan = 0.0f;
    private float pan = 0.0f;

    private ArrayList<Color> pf_colors = new ArrayList<Color>();
    private int current_index = 0;
    private int last_step = 0;
    private Color current_tint;

    com.pendulum.game.texture.TextureHolder textureHolder;

    public PlatformHandler(World world, float WORLD_WIDTH,
                           float REAL_WORLD_HEIGHT, com.pendulum.game.texture.TextureHolder textureHolder, int pellet_count, PlayScreen parent, String ropename, String ballname, String rodname) {
        this.textureHolder = textureHolder;
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;
        this.pellet_count = pellet_count;

        collisionDetector = new CollisionDetector(parent);
        world.setContactListener(collisionDetector);

        sides = com.pendulum.game.objects.ObjectHandler.createBackgroundSides(new Vector2(0.0f, -REAL_WORLD_HEIGHT/2/PPM), world, WORLD_WIDTH, REAL_WORLD_HEIGHT);

        pf_colors.add(new Color(106.0f/255.0f, 43.0f/255.0f, 28.0f/255.0f, 1.0f));

        current_tint = pf_colors.get(0);

        level = 0;
        setup(world, 0.0f, ropename, ballname, rodname);
    }

    public void changePelletCount(int change) {
        pellet_count+=change;
    }

    public Color getCurrent_tint() {
        return current_tint;
    }
    public float getTime() {
        return time;
    }
    public void resetTime() {
        time = 0.0f;
    }
    public int getLevel() {return level;}
    public String getGameMode() {
        return gamemode;
    }
    public int getPelletCount() {
        return pellet_count;
    }
    public void notifyCollision(com.pendulum.game.objects.Entity pellet) {
        pellet_count+=1;
        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).notifyCollision(pellet);
        }
    }
    public void start(World world, String ropename, String ballname, String rodname) {
        score = 0;

        if(fixedpan==0.0f) {
            gamemode="FALL";
        } else if(fixedpan==-1.0f) {
            gamemode="SWINGS";
        } else if(fixedpan==-2.0f) {
            gamemode="SLALOM";
        }

        boolean destroy = false;
        for(int i=0; i<obstacles.size(); i++){
            destroy = false;
            if(gamemode=="FALL") {
                if(!(obstacles.get(i).getType()=="platform" && !obstacles.get(i).isBlocked())) {
                    destroy = true;
                }
            }
            if(gamemode=="SLALOM") {
                if(!(obstacles.get(i).getType()=="platform" && obstacles.get(i).isBlocked()) && obstacles.get(i).getType()!="slalom") {
                    destroy = true;
                }
            }
            if(gamemode=="SWINGS") {
                if(obstacles.get(i).getType()!="swing") {
                    destroy = true;
                }
            }
            if(destroy) {
                obstacles.get(i).dispose(world);
                obstacles.remove(i);
                i-=1;
            }
        }

        for(int i=0; i<4; i++) {
            if(gamemode == "FALL") {
                com.pendulum.game.objects.Platform p = new com.pendulum.game.objects.Platform(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, false, ropename, level+totallevel);
                p.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.4))), world, textureHolder);
                obstacles.add(p);
            } else if(gamemode == "SLALOM") {
                if(level % 3 == 0) {
                    com.pendulum.game.objects.Platform p = new com.pendulum.game.objects.Platform(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, true, ropename, level+totallevel);
                    p.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
                    obstacles.add(p);
                } else {
                    Slalom s = new Slalom(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, ropename);
                    s.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
                    obstacles.add(s);
                }
            } else if (gamemode == "SWINGS") {
                Swing s = new Swing(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, ropename, ballname, rodname);
                s.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
                obstacles.add(s);
            }
            level++;
        }

        world.destroyBody(fill_background.getBody());
        fill_background=null;

        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).setTint(current_tint);
        }
    }
    public void setup(World world, float position, String ropename, String ballname, String rodname) {
        startingheight = position;
        totallevel+=level;
        level = 0;

        fill_background = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2(WORLD_WIDTH/2.0f/PPM, REAL_WORLD_HEIGHT/2.0f/PPM), WORLD_WIDTH/2.0f/PPM, REAL_WORLD_HEIGHT/2.0f/PPM, world),
                textureHolder.getTexture("background.png"), new Vector2(WORLD_WIDTH/PPM, REAL_WORLD_HEIGHT/PPM), "emoji");

        com.pendulum.game.objects.Platform p = new com.pendulum.game.objects.Platform(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, false, ropename, level+totallevel);
        p.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.4))), world, textureHolder);
        obstacles.add(p);

        Swing s = new Swing(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, ropename, ballname, rodname);
        s.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
        obstacles.add(s);

        com.pendulum.game.objects.Platform p_b = new com.pendulum.game.objects.Platform(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, true, ropename, level+totallevel);
        p_b.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
        obstacles.add(p_b);

        p_b.setPosition(new Vector2(p_b.getPosition().x+(WORLD_WIDTH/PPM*2.0f), p_b.getPosition().y));
        s.setPosition(new Vector2(s.getPosition().x+(WORLD_WIDTH/PPM), s.getPosition().y));
        level = 1;

        p_b.removeBlocker(world);
        s.removeBlocker(world);

        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).setTint(current_tint);
        }

        score=0;
        last_step = 0;

        updatePan(0.0f);
    }
    public boolean isScrolling() {
        return isScrolling;
    }
    public void gamemodeChange(String action) {
        isScrolling = true;
        if(action=="LEFT") {
            if(fixedpan-1<-(obstacles.size()-1)) {
                scrollto = fixedpan;
            } else {
                scrollto = fixedpan - 1;
            }
        } else if(action=="RIGHT") {
            if(fixedpan==0.0f) {
                scrollto = fixedpan;
            } else {
                scrollto = fixedpan + 1;
            }
        } else if(action=="RETURN") {
            scrollto = fixedpan;
        }
    }
    public void newCycle(float currentheight, World world) {
        for(int i=0; i<obstacles.size(); i++) {
            if(obstacles.get(i).getLowestY()*PPM<currentheight-(REAL_WORLD_HEIGHT*1.0f)) {
                obstacles.get(i).dispose(world);
                obstacles.remove(i);
                i-=1;
            }
        }
    }
    public void clearPlatforms(World world) {
        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).dispose(world);
        }
        obstacles.clear();
    }
    public void render(SpriteBatch spriteBatch, com.pendulum.game.shaders.ShaderHandler shaderHandler, String state) {
        for(int i=0; i<obstacles.size(); i+=2) {
            obstacles.get(i).render(spriteBatch, shaderHandler);
        }
        for(int i=1; i<obstacles.size(); i+=2) {
            if(state=="STARTSCREEN" || state =="GAMEMODE_CHANGE") {
                fill_background.getBody().setTransform((((0.5f + i + pan + fixedpan) * WORLD_WIDTH / PPM)),
                        (startingheight / PPM) + (float) (0.25f * REAL_WORLD_HEIGHT / PPM), fill_background.getBody().getAngle());
                fill_background.render(spriteBatch, shaderHandler);
            }
            obstacles.get(i).render(spriteBatch, shaderHandler);
        }
    }
    public void destroyConnection(World world) {
        if(gamemode=="SWINGS") {
            for(int i=0; i<obstacles.size(); i++) {
                obstacles.get(i).action("break_rope", world);
            }
        }
    }
    public float getLowestY() {
        for(int i=0; i<obstacles.size(); i++) {
            if(score == obstacles.get(i).getLevel()) {
                return obstacles.get(i).getLowestY();
            }
        }
        System.err.print("ERROR: obstacles not located");
        return 0.0f;
    }

    public void setMovement(boolean move) {
        this.move = move;
    }

    public void updatePan(float xpan) {
        pan = xpan/WORLD_WIDTH;

        for(int i=0; i<obstacles.size(); i++) {
            obstacles.get(i).setPosition(new Vector2(obstacles.get(i).getPosition().x + ((i+pan+fixedpan)*WORLD_WIDTH/PPM), obstacles.get(i).getPosition().y));
        }
    }

    public float getPan() {
        return pan;
    }

    public int getTotalLevel() {
        return level+totallevel;
    }

    public int update(Vector2 position, World world, com.pendulum.game.objects.Entity player, float dt, String ropename, String state, String ballname, String rodname) {
        if (score >= (last_step + 1) * STEP) {
            last_step += 1;
            if(last_step>8) {
                last_step=0;
            }
        }
        if(current_index!=last_step) {
//            if (current_tint.r - 0.01f <= pf_colors.get(last_step).r && current_tint.r <= pf_colors.get(last_step).r + 0.01f &&
//                current_tint.g - 0.01f <= pf_colors.get(last_step).g && current_tint.g <= pf_colors.get(last_step).g + 0.01f &&
//                    current_tint.b - 0.01f <= pf_colors.get(last_step).b && current_tint.b <= pf_colors.get(last_step).b + 0.01f) {
//                        current_tint = pf_colors.get(last_step);
//                        current_index = last_step;
//            } else {
//                current_tint = new Color(current_tint.r + (dt * (pf_colors.get(last_step).r - pf_colors.get(current_index).r)),
//                        current_tint.g + (dt * (pf_colors.get(last_step).g - pf_colors.get(current_index).g)),
//                        current_tint.b + (dt * (pf_colors.get(last_step).b - pf_colors.get(current_index).b)), 1.0f);
//            }
//            for (int i = 0; i < obstacles.size(); i++) {
//                obstacles.get(i).setTint(current_tint);
//            }
        }
        if(isScrolling) {
            if(scrollto>pan+fixedpan) {
                updatePan((pan*WORLD_WIDTH)+20f);
                if(pan+fixedpan>scrollto) {
                    isScrolling=false;
                    fixedpan=scrollto;
                    pan=0.0f;
                    move = true;
                }
            } else {
                updatePan((pan*WORLD_WIDTH)-20f);
                if(pan+fixedpan<scrollto) {
                    isScrolling=false;
                    fixedpan=scrollto;
                    pan=0.0f;
                    move = true;
                }
            }
            if(Math.abs(pan)>0.51f) {
                if (scrollto == 0.0f) {
                    gamemode = "FALL";
                } else if (scrollto == -1.0f) {
                    gamemode = "SWINGS";
                } else if (scrollto == -2.0f) {
                    gamemode = "SLALOM";
                }
            }
        }
        if(move) {
            for (int i = 0; i < obstacles.size(); i++) {
                if (!(obstacles.get(i).getType() == "platform" && state == "PAUSE") && state != "GAMEOVER" && state != "COLLECTIBLES" && state != "SCROLL") {
                    if (obstacles.get(i).update(dt, player, world)) {
                        score += 1;
                        if (gamemode == "SLALOM" || gamemode == "SWINGS") {
                            if(gamemode=="SLALOM") {
                                time += 2.0f;
                            }
                            obstacles.get(i + 1).removeBlocker(world);
                            if (i > 2) {
                                obstacles.get(i - 3).dispose(world);
                                obstacles.remove(i - 3);
                                i -= 1;
                            }
                        }
                        if (gamemode == "FALL") {
                            if (i > 2) {
                                obstacles.get(i - 3).dispose(world);
                                obstacles.remove(i - 3);
                                i -= 1;
                            }
                        }

                        if (gamemode == "FALL") {
                            com.pendulum.game.objects.Platform p = new com.pendulum.game.objects.Platform(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, false, ropename, level+totallevel);
                            p.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.4))), world, textureHolder);
                            obstacles.add(p);
                        } else if (gamemode == "SLALOM") {
                            if (level % 3 == 0) {
                                com.pendulum.game.objects.Platform p = new com.pendulum.game.objects.Platform(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, true, ropename, level+totallevel);
                                p.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
                                obstacles.add(p);
                            } else {
                                Slalom s = new Slalom(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, ropename);
                                s.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
                                obstacles.add(s);
                            }
                        } else if (gamemode == "SWINGS") {
                            Swing s = new Swing(WORLD_WIDTH, REAL_WORLD_HEIGHT, level, ropename, ballname, rodname);
                            s.create((startingheight / PPM) - (float) (REAL_WORLD_HEIGHT / PPM * ((level * 0.5))), world, textureHolder);
                            obstacles.add(s);
                        }

                        for (int k=0; k<obstacles.size(); k++) {
                            obstacles.get(k).setTint(current_tint);
                        }

                        level += 1;
                    }
                }
            }
        }
        sides[0].setTransform(sides[0].getPosition().x, position.y, sides[0].getAngle());
        sides[1].setTransform(sides[1].getPosition().x, position.y, sides[1].getAngle());
        return score;
    }
}
