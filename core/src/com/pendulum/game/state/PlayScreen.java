package com.pendulum.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.userinterface.UIController;
import com.pendulum.game.utils.Constants;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.PPM;
import static com.pendulum.game.utils.Constants.SCORE_RADIUS;

/**
 * Created by Harry on 07/01/2017.
 */

public class PlayScreen {
    private static float MIN_VELOCITY = -10f;
    private float camera_velocity = MIN_VELOCITY;
    private float camera_acceleration = -0.002f;

    private com.pendulum.game.objects.Entity chaser;
    private float timeperiod = 0.02f;
    private float chaser_time = 0.0f;

    private Texture sound_on_image;
    private Texture sound_off_image;
    private Texture overlay;

    private boolean started = false;

    private float score_opacity = 1.0f;
    private float previous_score_opacity = 1.0f;
    private String score_action = "none";

    private boolean startbutton_down = false;
    private boolean start_pressed = false;

    private final float scroll_speed = -30f;

    private boolean game_on = false;
    private float game_on_transparency = 1.0f;
    private boolean playing = false;

    private int scroll_shop = 0;

    private Color gold = new Color(255.0f/255.0f, 203.0f/255.0f, 0.0f/255.0f, 1.0f);
    private Color blue = new Color(121.0f/255.0f, 201.0f/255.0f, 240.0f/255.0f, 1.0f);
    private Color red = new Color(255.0f/255.0f, 87.0f/255.0f, 34.0f/255.0f, 1.0f);
    private boolean flash = false;
    private Vector3 color_speed;
    private Color current_color;
    private final float color_timeperiod = 1.0f;
    private float color_current_time = 0.0f;
    private ArrayList<com.pendulum.game.objects.ParticleCluster> particleClusters = new ArrayList<com.pendulum.game.objects.ParticleCluster>();

    private ArrayList<Color> bg_colors = new ArrayList<Color>();
    private int last_step = 0;

    private com.pendulum.game.utils.Preferences preferences;

    private float gameoverYpos;

    private final World world;

    private float cameraypos;

    private com.pendulum.game.shaders.ShaderHandler shaderHandler;

    private int score = 0;
    private int pellet_count = 0;

    private Texture balltexture;

    private final float MAX_TIMER = 5.0f;
    private float timer = MAX_TIMER;
    private GlyphLayout timer_layout;
    private float timer_transparency=0.0f;

    private com.pendulum.game.texture.TextureHolder th;

    private float scrolllimit;
    private float scrollvel;
    private String scrollchangeto;
    private String specialstate = "none";

    private float WORLD_WIDTH = 640;
    private float REAL_WORLD_HEIGHT;

    private float DEVICE_WIDTH;
    private float DEVICE_HEIGHT;

    private com.pendulum.game.input.GameInputHandler input;
    private com.pendulum.game.input.GameGestureHandler gesture;
    private Box2DDebugRenderer b2dr;
    private com.pendulum.game.objects.EntityHandler entities;
    private com.pendulum.game.input.InputInterpreter interpreter;

    private String state = "PAUSE";

    private boolean destroyJoint = false;

    private boolean playAgain = false;

    private boolean ishints;

    private boolean loading = true;

    private OrthographicCamera camera;

    private boolean sound_on = true;
    private Sound coin_noise;
    private Sound hit_noise;
    private Sound tap_noise;

    private com.pendulum.game.MyGdxGame app;

    private float fadein_transparency = 1.0f;

    private UIController UserInterface;
    // TODO: UI Crap to move over to the user interface
    private BitmapFont score_font;
    private BitmapFont font;
    private BitmapFont font_small;
    private BitmapFont font_helper;
    private GlyphLayout score_layout;
    private GlyphLayout pellet_layout;
    private GlyphLayout helper_layout;
    private com.pendulum.game.objects.Entity helper;
    private float helpersize;
    private float helperanimationtimer = 0.0f;
    private float helpertimestep = 1f;
    private float helper_transparency = 1.0f;
    private String helper_text;
    private String instruction_1_text;
    private String instruction_2_text;
    private String instruction_3_text;
    private GlyphLayout instruction_1;
    private GlyphLayout instruction_2;
    private GlyphLayout instruction_3;
    private int instruction_fadein = -1;
    private float instruction_timer = 0.0f;
    private boolean instruction_given = false;
    private Texture background;
    private com.pendulum.game.objects.Entity title;
    private com.pendulum.game.objects.Entity playbutton;

    private EventHandler eventHandler = new EventHandler();

    public PlayScreen(com.pendulum.game.MyGdxGame app) {
        // Load in the data
        this.app = app;
        th = new com.pendulum.game.texture.TextureHolder();
        th.loadTextures();

        // Get the size of the screen
        DEVICE_WIDTH = Gdx.graphics.getWidth();
        DEVICE_HEIGHT = Gdx.graphics.getHeight();

        // Create the box2d world
        world = new World(new Vector2(0.0f, -20f), true);

        // Scale the height of the world with the screen coordinates
        REAL_WORLD_HEIGHT = ((DEVICE_HEIGHT/DEVICE_WIDTH)*WORLD_WIDTH);

        // Set up the camera
        camera = new OrthographicCamera();
        this.camera.setToOrtho(false, WORLD_WIDTH, (DEVICE_HEIGHT/DEVICE_WIDTH)*WORLD_WIDTH);
        camera.position.set(camera.position.x, camera.position.y-(REAL_WORLD_HEIGHT/4.0f),
                camera.position.z);
        camera.update();
        cameraypos = camera.position.y;
    }

    /**
     *  UI CALLBACKS
     */
    public boolean touchDown(Vector3 position) { return UserInterface.onTouchDown(convertTouchToScreen(position)); }

    public boolean touchUp(Vector3 position) { return UserInterface.onTouchUp(convertTouchToScreen(position)); }

    public Vector2 convertTouchToScreen(Vector3 mousePosition) {
        return new Vector2(
            mousePosition.x,
            DEVICE_HEIGHT-mousePosition.y
        );
    }

    public void create() {
        state="FADE_IN";

        Box2D.init();
        b2dr = new Box2DDebugRenderer();

        InputMultiplexer multiplexer = new InputMultiplexer();
        input = new com.pendulum.game.input.GameInputHandler();
        gesture = new com.pendulum.game.input.GameGestureHandler();
        multiplexer.addProcessor(input);
        multiplexer.addProcessor(new GestureDetector(gesture));
        Gdx.input.setInputProcessor(multiplexer);

        shaderHandler = new com.pendulum.game.shaders.ShaderHandler();
        shaderHandler.create(DEVICE_WIDTH, DEVICE_HEIGHT);

        preferences = new com.pendulum.game.utils.Preferences();
        interpreter = new com.pendulum.game.input.InputInterpreter(WORLD_WIDTH, REAL_WORLD_HEIGHT);

        // Setting up the fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(("fonts/Choco-Bear.ttf")));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        generator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        parameter.size = (int)(160.0f*(Gdx.graphics.getHeight()/2000.0f));;
        parameter.color = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        score_font = generator.generateFont(parameter);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_score = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter_score.size = (int)(80.0f*(Gdx.graphics.getHeight()/2000.0f));
        parameter_score.color = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        font = generator.generateFont(parameter_score);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter_small= new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter_small.size = (int)(30.0f*(Gdx.graphics.getHeight()/2000.0f));;
        font_small = generator.generateFont(parameter_small);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter_helper= new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter_helper.size = (int)(52.0f*(Gdx.graphics.getHeight()/2000.0f));;
        font_helper = generator.generateFont(parameter_helper);

        generator.dispose();

        // TODO: Create the score user interface elements
        score_layout = new GlyphLayout();
        score_action = "increase";
        score_layout.setText(score_font, "0");
        timer_layout = new GlyphLayout();
        timer_layout.setText(score_font, String.format("%.2f", timer), new Color(1.0f, 1.0f, 1.0f, timer_transparency), timer_layout.width, 1, false);
        pellet_layout = new GlyphLayout();
        pellet_layout.setText(font, String.valueOf(preferences.getPellets()));

        helpersize = Constants.PLAYER_RADIUS*2.0f;

        helper_layout = new GlyphLayout();
        // ADD IN FOR GAME MODES
        if(preferences.getInstruction(preferences.INSTRUCTION)==1) {helper_text="swipe for game modes";} else {helper_text="tap to release";}
        helper_text="tap to release";
        helper_layout.setText(font_helper, helper_text);

        // BACKGROUND
        bg_colors.add(new Color(100.0f/255.0f, 204.0f/255.0f, 223.0f/255.0f, 1.0f));
        bg_colors.add(new Color(81.0f/255.0f, 69.0f/255.0f, 255.0f/255.0f, 1.0f));
        bg_colors.add(new Color(254.0f/255.0f, 51.0f/255.0f, 68.0f/255.0f, 1.0f));
        bg_colors.add(new Color(235.0f/255.0f, 6.0f/255.0f, 145.0f/255.0f, 1.0f));
        bg_colors.add(new Color(62.0f/255.0f, 213.0f/255.0f, 51.0f/255.0f, 1.0f));
        bg_colors.add(new Color(215.0f/255.0f, 64.0f/255.0f, 223.0f/255.0f, 1.0f));
        bg_colors.add(new Color(255.0f/255.0f, 151.0f/255.0f, 34.0f/255.0f, 1.0f));
        bg_colors.add(new Color(241.0f/255.0f, 235.0f/255.0f, 10.0f/255.0f, 1.0f));
        bg_colors.add(new Color(215.0f/255.0f, 65.0f/255.0f, 223.0f/255.0f, 1.0f));

        this.blue = bg_colors.get(0);

        current_color = new Color(Color.WHITE);

        loading = false;
    }
    public void tap() {
        if(sound_on) {
            tap_noise.play();
        }
    }
    public void screenTap(Vector3 mousepos, String type) {
        if(state=="GAMEOVER" || state == "COLLECTIBLES") {
            int instruction = entities.screenTap(mousepos, state, type);

            if (instruction == 1) {
                tap();
                playAgain();
            } else if(instruction==2) {
                tap();
                scroll_shop = 1;
            } else if(instruction==3) {
                tap();
                scroll_shop = 2;
            }
        } else if(state=="TITLESCREEN") {
            Vector2 button_pos = new Vector2(WORLD_WIDTH/2.0f, 0.25f*REAL_WORLD_HEIGHT);

            if(button_pos.sub(new Vector2(mousepos.x*(WORLD_WIDTH/Gdx.graphics.getWidth()), REAL_WORLD_HEIGHT -  (mousepos.y*(REAL_WORLD_HEIGHT/Gdx.graphics.getHeight())))).len() < (playbutton.getSize().x/2.0f* PPM)) {
                if(start_pressed || state=="TAP") {
                    tap();
                    initialStart();
                }
            }
        }
        start_pressed = false;
        startbutton_down = false;
    }
    public void screenDown(Vector3 mousepos, String type) {
        if(state=="TITLESCREEN") {
            Vector2 button_pos = new Vector2(WORLD_WIDTH/2.0f, 0.25f*REAL_WORLD_HEIGHT);

            if(button_pos.sub(new Vector2(mousepos.x*(WORLD_WIDTH/Gdx.graphics.getWidth()),
                    REAL_WORLD_HEIGHT -  (mousepos.y*(REAL_WORLD_HEIGHT/Gdx.graphics.getHeight())))).len()
                    < (playbutton.getSize().x/2.0f* PPM)) {
                if(type=="TOUCH_DOWN") {
                    start_pressed = true;
                    startbutton_down = true;
                } else {
                    if(start_pressed) {
                        startbutton_down = true;
                    }
                }
            } else {
                startbutton_down = false;
            }
        } else if(state=="GAMEOVER") {
            entities.screenDown(mousepos, type, state);
        }
    }
    public void initialStart() {
        game_on = true;
    }
    public void playAgain() {
        playAgain = true;
    }
    public void setScore(int score) {
        score_layout.setText(score_font, String.valueOf(score));
        this.score = score;
    }
    public void setPelletCount(int pellet_count) {pellet_layout.setText(font, String.valueOf(pellet_count)); this.pellet_count=pellet_count;}
    public float getGameOverYpos() {
        return gameoverYpos;
    }
    public String getScrollchangeto() {
        return scrollchangeto;
    }
    public void scroll(float vel, float limit, String change, String special) {
        scrollvel = vel;
        scrolllimit = limit;
        scrollchangeto = change;
        specialstate = special;
    }

    public void pan(float ypan) {
        entities.pan(ypan);
    }

    public void handleInput(ArrayList<com.pendulum.game.input.InputEvent> events) {
        state = interpreter.handleInput(events, state, camera, entities, world, this);
    }
    public void play() {
        cameraypos = entities.getLastDeathHeight()-(REAL_WORLD_HEIGHT*3.0f);
        camera_velocity = MIN_VELOCITY;

        started = true;
        if(entities.getGameMode()=="FALL") {
            instruction_1_text="press and hold to create a rope";
            instruction_2_text="swing through the gaps";
            instruction_3_text="tap to continue";
        } else if(entities.getGameMode()=="SWINGS") {
            instruction_1_text="tap to release the ball";
            instruction_2_text="swing to the next area";
            instruction_3_text="tap to continue";
        } else if(entities.getGameMode()=="SLALOM") {
            instruction_1_text = "go through the gates";
            instruction_2_text = "in the direction shown by the arrows";
            instruction_3_text="tap to continue";
        }
        instruction_1 = new GlyphLayout();
        instruction_2 = new GlyphLayout();
        instruction_3 = new GlyphLayout();

        instruction_1.setText(font, instruction_1_text, new Color(1.0f, 1.0f, 1.0f, 0.0f), instruction_1.width, 1, false);
        instruction_2.setText(font, instruction_2_text, new Color(1.0f, 1.0f, 1.0f, 0.0f), instruction_2.width, 1, false);
        instruction_3.setText(font, instruction_3_text, new Color(1.0f, 1.0f, 1.0f, 0.0f), instruction_3.width, 1, false);

        if(preferences.getInstruction(preferences.INSTRUCTION)==0) {
            instruction_given = false;
        } else {
            instruction_given = true;
        }
    }
    public void loadStartScreen() {
        started = false;
        score_action = "decrease"; entities.loadStartScreen(world);
    }
    public void pause_change() {
        if(instruction_fadein<3 && instruction_fadein!=-1) {
            instruction_fadein+=1;
        } else {
            instruction_given=true;
            if(entities.getGameMode()=="FALL") {
                preferences.setInstruction(preferences.INSTRUCTION, 1);
            }
            if(entities.getGameMode()=="SWINGS") {
                preferences.setInstruction(preferences.SWINGS_INSTRUCTION, 1);
            }
            if(entities.getGameMode()=="SLALOM") {
                preferences.setInstruction(preferences.SLALOM_INSTRUCTION, 1);
            }
        }
    }
    public void notifyCollision(Contact contact, boolean ispellet, com.pendulum.game.objects.Entity pellet) {
        if(state == "PLAY") {
            if (ispellet) {
                if(sound_on) {
                    coin_noise.play();
                }
//                color_speed = new Vector3(gold.r-blue.r, gold.g-blue.g, gold.b-blue.b).scl(1.0f/color_timeperiod);
//                color_current_time = 0.0f;
//                flash = true;
                entities.notifyCollision(pellet);
                particleClusters.add(new com.pendulum.game.objects.ParticleCluster(contact.getWorldManifold().getPoints()[0], entities.getPlayer().getLinearVelocity().len()*10));

            } else {
                float strength = entities.getPlayer().getLinearVelocity().len()*10;
                if(strength > 50) {
                    if(sound_on) {
                        long id = hit_noise.play();
                        float volume = (strength-50)/100;
                        if(volume>1.0f) {volume = 1.0f;};
                        hit_noise.setVolume(id, volume);
                    }
                    color_speed = new Vector3(1.0f-blue.r, 1.0f-blue.g, 1.0f-blue.b).scl(1.0f/color_timeperiod);
                    color_current_time = 0.0f;
                    flash = true;
                    gameover();
                }
            }
        }
    }
    public void checkCollectibles (Vector3 mousepos) {
        String selected = entities.checkCollectibles(mousepos);
        if(!selected.equals("-1")) {
            tap();
        }
    }
    public void update(float dt) {

        // Trigger any state updates
        eventHandler.update();

        if (!flash && score >= ((last_step + 1) * Constants.STEP)) {
//            Color prev_color = new Color(blue);
//            last_step += 1;
//            if(last_step>8) {
//                last_step=0;
//            }
//            this.blue = bg_colors.get(last_step);
//            color_speed = new Vector3(blue.r-prev_color.r, blue.g-prev_color.g,blue.b-prev_color.b).scl(1.0f/color_timeperiod);
//            color_current_time = 0.0f;
//            flash = true;
        }

        //CHANGE CAMERA
        if(state == "PLAY") {
            if((entities.getPlayer().getPosition().y * PPM)<entities.getLastDeathHeight()-(3.5f*REAL_WORLD_HEIGHT) && !instruction_given) {
                if((entities.getGameMode()=="FALL" && preferences.getInstruction(preferences.INSTRUCTION)==0) || (entities.getGameMode()=="SWINGS" && preferences.getInstruction(preferences.SWINGS_INSTRUCTION)==0)
                        || (entities.getGameMode()=="SLALOM" && preferences.getInstruction(preferences.SLALOM_INSTRUCTION)==0))
                {
                    instruction_fadein = 1;
                    state = "PAUSE";
                    scrollchangeto = "PLAY";
                    entities.destroyJoint(world);
                    instruction_1.setText(font_small, instruction_1_text, new Color(1.0f, 1.0f, 1.0f, 0.0f), instruction_1.width, 1, false);
                    instruction_2.setText(font_small, instruction_2_text, new Color(1.0f, 1.0f, 1.0f, 0.0f), instruction_2.width, 1, false);
                    instruction_3.setText(font_small, instruction_3_text, new Color(1.0f, 1.0f, 1.0f, 0.0f), instruction_3.width, 1, false);
                }
            }
            if(entities.getGameMode() == "FALL") {
                if((((entities.getPlayer().getPosition().y * PPM)+(WORLD_WIDTH*1.25f)) < cameraypos + (camera_velocity * PPM * dt))) {
                    cameraypos = ((entities.getPlayer().getPosition().y * PPM)+(WORLD_WIDTH*1.25f));
                    camera.position.set(camera.position.x, ((entities.getPlayer().getPosition().y * PPM)+(WORLD_WIDTH/4.0f)),
                            camera.position.z);
                } else if (((entities.getPlayer().getPosition().y * PPM)+(WORLD_WIDTH/4.0f)) < cameraypos + (camera_velocity * PPM * dt)) {
                    cameraypos += (camera_velocity * PPM * dt);
                    camera.position.set(camera.position.x, ((entities.getPlayer().getPosition().y * PPM)+(WORLD_WIDTH/4.0f)),
                            camera.position.z);
                } else {
                    cameraypos += (camera_velocity * PPM * dt);
                    camera.position.set(camera.position.x, cameraypos,
                            camera.position.z);
                }
            } else {
                camera.position.set(camera.position.x, (entities.getPlayer().getPosition().y * PPM),
                        camera.position.z);
                cameraypos = (entities.getPlayer().getPosition().y * PPM);
            }
            System.out.println(camera_velocity);
            camera.update();
            if(camera_velocity>-15f) {
                camera_velocity += camera_acceleration * PPM * dt;
            }
            if (((entities.getPlayer().getPosition().y * PPM) > camera.position.y + (camera_velocity * PPM * dt)
                    + (REAL_WORLD_HEIGHT / 2))) {
                gameover();
            }
    }
        if(state=="SCROLL") {
            camera_velocity=scrollvel;
            if((scrollvel>0 && camera.position.y>scrolllimit) || (scrollvel<0 && camera.position.y<scrolllimit)) {
                state=scrollchangeto;
                if(scrollchangeto=="GAMEOVER" && entities.getGameMode() == "SLALOM") {timer = MAX_TIMER; timer_layout.setText(score_font, String.format("%.2f", timer), new Color(1.0f, 1.0f, 1.0f, timer_transparency), timer_layout.width, 1, false);}
                if(scrollchangeto=="STARTSCREEN") {
                    entities.play(world);
                }
                if(specialstate=="respawn") {respawn(); specialstate="none";}
                if(specialstate=="reload game") {ishints = false; entities.reloadGame(world); specialstate="none"; gameoverYpos= camera.position.y;}
            } else {
                camera.position.set(camera.position.x, camera.position.y + (camera_velocity * PPM * dt),
                        camera.position.z);
                cameraypos = camera.position.y + (camera_velocity * PPM * dt);
                camera.update();
            }
        }
        handleInput(input.update());
        handleInput(gesture.update());
        input.clear();
        gesture.clear();
//        if(state=="PLAY" && entities.getGameMode()=="FALL") {
//            chaser.getBody().setTransform(new Vector2(WORLD_WIDTH/2.0f/PPM, ((cameraypos+(REAL_WORLD_HEIGHT*0.4f))/PPM)-chaser.getSize().y/2.0f), chaser.getBody().getAngle());
//            chaser_time += dt;
//            if(chaser_time>timeperiod) {
//                if(chaser.getTextureid()<7) {
//                    chaser.setTextureid(chaser.getTextureid() + 1);
//                } else {
//                    chaser.setTextureid(0);
//                }
//                chaser_time = 0.0f;
//            }
//        }
        if(instruction_fadein!=-1) {
            instruction_timer+=dt;
            if(instruction_timer>=1.0f) {
                instruction_timer=1.0f;
            }

            if(instruction_fadein==1) {
                    instruction_1.setText(font_small, instruction_1_text, new Color(1.0f, 1.0f, 1.0f, instruction_timer), instruction_1.width, 1, false);
            } else if(instruction_fadein==2) {
                    instruction_1.setText(font_small, instruction_1_text, new Color(1.0f, 1.0f, 1.0f, 1.0f), instruction_1.width, 1, false);
                    instruction_2.setText(font_small, instruction_2_text, new Color(1.0f, 1.0f, 1.0f, instruction_timer), instruction_2.width, 1, false);
            } else {
                    instruction_1.setText(font_small, instruction_1_text, new Color(1.0f, 1.0f, 1.0f, 1.0f), instruction_1.width, 1, false);
                    instruction_2.setText(font_small, instruction_2_text, new Color(1.0f, 1.0f, 1.0f, 1.0f), instruction_2.width, 1, false);
                    instruction_3.setText(font_small, instruction_3_text, new Color(1.0f, 1.0f, 1.0f, instruction_timer), instruction_3.width, 1, false);
            }

            if(instruction_timer==1.0f) {
                if(instruction_fadein<3) {
                    instruction_timer=0.0f;
                    instruction_fadein+=1;
                } else {
                    instruction_fadein=-1;
                }
            }
        }
        if(instruction_given && state == "PAUSE") {
            state="PLAY";
        }
        if(entities.getGameMode()=="SLALOM") {
            boolean update = timer_transparency!=entities.getGamemodeTransparency();
            timer_transparency = entities.getGamemodeTransparency();
            if(update) {
                timer_layout.setText(score_font, String.format("%.2f", timer), new Color(1.0f, 1.0f, 1.0f, timer_transparency), timer_layout.width, 1, false);
            }
        }
        if(state=="GAMEMODE_CHANGE") {
            if(!entities.isScrolling()) {
                state = "STARTSCREEN";
            }
        }
        if(score_action == "increase") {
            score_opacity += dt;
            if(score_opacity >= 1.0f) {
                score_opacity = 1.0f;
                score_action = "none";
            }
        } else if(score_action=="decrease") {
            score_opacity -= dt;
            if(score_opacity <= 0.0f) {
                score_opacity = 0.0f;
                setScore(0);
                score = 0;
                score_action = "none";
            }
        }
        if(entities.getGameMode()=="SWINGS" && state == "PLAY") {
            if(entities.getPlayer().getPosition().y < entities.getLowestY()) {
                if (score > preferences.getHighScore()) {
                    preferences.setHighScore(score);
                }
                entities.died(world, camera);
                state = "SCROLL";
                scrollvel = -15f;
                scrollchangeto = "GAMEOVER";
                scrolllimit = entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 1.5f);
                specialstate = "reload game";
            }
        }

        if(entities.getGameMode()=="SLALOM" && state =="PLAY") {
            if (timer > 0.0f) {
                timer -= dt;
                if(timer<0.0f) { timer = 0.0f; }
                timer_layout.setText(score_font, String.format("%.2f", timer), new Color(1.0f, 1.0f, 1.0f, timer_transparency), timer_layout.width, 1, false);
            } else {
                if (score > preferences.getHighScore()) {
                    preferences.setHighScore(score);
                }
                entities.died(world, camera);
                state = "SCROLL";
                scrollvel = -15f;
                scrollchangeto = "GAMEOVER";
                scrolllimit = entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 1.5f);
                specialstate = "reload game";
                timer = 0.0f;
                timer_layout.setText(score_font, String.format("%.2f", timer), new Color(1.0f, 1.0f, 1.0f, timer_transparency), timer_layout.width, 1, false);
            }
        }
        if(flash) {
            double mid = color_timeperiod/2.0f;

            if (color_current_time < mid) {
                fadein_transparency = (float) ( ((mid-color_current_time)/mid) );
            } else {
                // fadein_transparency = (float) ( 1.0f - ((color_timeperiod-color_current_time)/color_timeperiod) );
            }

            color_current_time+=dt;
            if(color_current_time>color_timeperiod) {
                color_current_time=0.0f;
                fadein_transparency = 0.0f;
                flash = false;
            }
        }
//        else if (timer<3.0f && state == "PLAY") {
//            flash = true;
//            start_color = new Color(current_color);
//            changeto_color = new Color(red);
//        }
        if(state != "PAUSE") {
            if (state == "STARTSCREEN" || state=="TITLESCREEN" || state=="FADE_IN_GAME" || state=="FADE_IN" || (state == "SCROLL" && scrollchangeto=="STARTSCREEN" || state == "GAMEMODE_CHANGE")) { entities.controlStartingRod(dt); }
            if (state == "COLLECTIBLES" || state=="GAMEOVER" || (state=="SCROLL" && (scrollchangeto=="COLLECTIBLES" || scrollchangeto=="GAMEOVER"))) { entities.controlCollectibleBalls(dt, world); }
            for(int i=0; i<particleClusters.size(); i++) {
                particleClusters.get(i).update(dt, world);
                if(!particleClusters.get(i).isCreated()){particleClusters.get(i).create(world, th);}
            }
        }
        if(state == "PLAY" || state == "STARTSCREEN" || state == "PAUSE") {
            helperanimationtimer+=dt;
            if(helperanimationtimer>0.5f) {
                helper_transparency=(helperanimationtimer-0.6f)*2.5f;
                if(helper_transparency<0.0f) {helper_transparency=0.0f;}
            } else {
                helper_transparency=1.0f-(helperanimationtimer*3.0f);
                if(helper_transparency<0.0f) {helper_transparency=0.0f;}
            }
            //ADD IN FOR MULTIPLE GAME MODES
//            if(helperanimationtimer<0.5f+(dt/2) && helperanimationtimer>0.5f-(dt/2)) {
//                if(preferences.getInstruction(preferences.INSTRUCTION)==2) {
//                    if(helper_text=="swipe for game modes") {
//                        helper_text = "tap to release";
//                    } else {
//                        helper_text = "swipe for game modes";
//                    }
//                }
//            }
            helper_layout.setText(font_helper, helper_text, new Color(1.0f, 1.0f, 1.0f, helper_transparency), helper_layout.width, 1, false);
            if (helperanimationtimer >= helpertimestep) {
                helperanimationtimer = 0.0f;
                helper.nextTex();
            }
        }
        int pellet_count_new = entities.getPelletCount();
        if(pellet_count_new != pellet_count) { setPelletCount(pellet_count_new); preferences.setPellets(pellet_count_new);}
        int newscore = entities.update(entities.getPlayer().getPosition(), world, dt, state);
        if (newscore != score && score_opacity != 0.0f && state == "PLAY") {
            setScore(newscore);
        }
        if(playAgain) {
            state = "SCROLL";
            scroll(scroll_speed, entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 3.0f), "STARTSCREEN", "respawn");
            loadStartScreen();
            playAgain = false;
        }
        if(state!="PAUSE") {
            world.step(dt, 6, 2);
        }
        if(game_on && !playing) {
            state = "FADE_IN_GAME";
            game_on=false;
        }
        if(state=="FADE_IN") {
            fadein_transparency-=dt*2.0f;
            if(fadein_transparency<=0.0f) {
                fadein_transparency = 0.0f;
                state="TITLESCREEN";
            }
        }
        if(state=="FADE_IN_GAME") {
            game_on_transparency-=dt*2.0f;
            title.setTransparency(game_on_transparency);
            playbutton.setTransparency(game_on_transparency);
            camera.translate(0.0f, (REAL_WORLD_HEIGHT*0.25f)*dt*2.0f);
            camera.update();
            cameraypos=camera.position.y;
            if(game_on_transparency<=0.0f) {
                game_on_transparency = 0.0f;
                playing=true;
                state="STARTSCREEN";
            }
        }
        if(scroll_shop==1) {
            state="SCROLL";
            scroll(30f, getGameOverYpos() + REAL_WORLD_HEIGHT, "COLLECTIBLES", "none");
            scroll_shop=0;
        } else if(scroll_shop==2) {
            state="SCROLL";
            scroll(-30f, getGameOverYpos(), "GAMEOVER", "none");
            scroll_shop=0;
        }
    }
    public void respawn() {
        entities.respawn(world);
        score_action = "increase";
        camera_velocity = MIN_VELOCITY;
        if(preferences.getInstruction(preferences.INSTRUCTION)==0) {
            preferences.setInstruction(preferences.INSTRUCTION, 1);
        } else {
            preferences.setInstruction(preferences.INSTRUCTION, 2);
        }
        //ADD IN FOR GAME MODES
//      if(preferences.getInstruction(preferences.INSTRUCTION)==1) {helper_text="swipe for game modes";} else {helper_text="tap to release";}
        helper_text="tap to release";
    }
    public void resize(int width, int height) {
        Vector2 position = new Vector2(camera.position.x, camera.position.y);
        camera.setToOrtho(false, WORLD_WIDTH, ((float)height/(float)width)*WORLD_WIDTH);
        camera.position.set(camera.position.x, position.y, 0.0f);
        camera.update();
    }
    public void setTransparency(float transparency) {
        shaderHandler.getBlurShader().setUniformf("transparency", transparency);
    }
    public void setXPan(float xpan) {
        entities.setXPan(xpan);
    }
    public void gamemodeChange(String action) {
        entities.gamemodeChange(action);
    }
    public boolean isLoaded() {
        if(th.isFinishedLoading()) {
            // Set up the User Interfaces
            Vector2 dimensions = new Vector2(DEVICE_WIDTH, DEVICE_HEIGHT);
            UserInterface = new UIController(dimensions, th);

            background = th.getTexture("background.png");
            playbutton = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2(WORLD_WIDTH/2.0f/ PPM, 0.0f), 7.0f, 7.0f, world), th.getTexture("play.png"),
                    new Vector2(14.0f, 14.0f), "emoji");
            title = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2((WORLD_WIDTH*0.5f/ PPM), REAL_WORLD_HEIGHT*0.65f/ PPM), WORLD_WIDTH*0.8f/ PPM/2.0f, (WORLD_WIDTH*0.8f)/2.0f*(170.0f/847.0f)/ PPM, world), th.getTexture("title.png"),
                    new Vector2(WORLD_WIDTH*0.8f/ PPM, (WORLD_WIDTH*0.8f)*(170.0f/847.0f)/ PPM), "emoji");

            balltexture = th.getTexture("score.png");

            helper = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createCollisionBody(new Vector2(helpersize, DEVICE_HEIGHT/ PPM*0.75f),
                    Constants.PLAYER_RADIUS, world, BodyDef.BodyType.StaticBody, Constants.CATEGORY_PLAYER, Constants.MASK_PLAYER), th.getTexture("tap_down.png"), new Vector2(helpersize, helpersize), "emoji");
            helper.getBody().setActive(false);
            helper.addTex(th.getTexture("tap_up.png"));

            chaser = new com.pendulum.game.objects.Entity(com.pendulum.game.objects.ObjectHandler.createTextBox(new Vector2(0.0f, DEVICE_HEIGHT/ PPM*2.0f), WORLD_WIDTH/2.0f/ PPM, REAL_WORLD_HEIGHT/60.0f/ PPM, world), th.getTexture("chaser1.png"),
                    new Vector2(WORLD_WIDTH/ PPM, REAL_WORLD_HEIGHT/30.0f/ PPM), "chaser");
            chaser.addTex(th.getTexture("chaser2.png")); chaser.addTex(th.getTexture("chaser3.png")); chaser.addTex(th.getTexture("chaser4.png"));
            chaser.addTex(th.getTexture("chaser5.png")); chaser.addTex(th.getTexture("chaser6.png")); chaser.addTex(th.getTexture("chaser7.png")); chaser.addTex(th.getTexture("chaser8.png"));

            entities = new com.pendulum.game.objects.EntityHandler(WORLD_WIDTH, REAL_WORLD_HEIGHT, world, th);
            entities.setSkin(preferences.getSelected());
            entities.create(world, preferences.getPellets(), this);

            coin_noise = th.getCoin_noise();
            hit_noise = th.getHit_noise();
            tap_noise = th.getTap_noise();

            sound_on_image = th.getTexture("sound_on.png");
            sound_off_image = th.getTexture("sound_off.png");

            return true;
        }
        return false;
    }

    public void gameover() {
        eventHandler.addEvent(new StateEvent() {
            @Override
            public void triggerEvent() {
                if(score>preferences.getHighScore()) {
                    preferences.setHighScore(score);
                }
                entities.died(world, camera);
                state = "SCROLL";
                scrollvel=scroll_speed;
                scrollchangeto = "GAMEOVER";
                scrolllimit = entities.getLastDeathHeight()-(REAL_WORLD_HEIGHT*1.5f);
                specialstate = "reload game";
            }
        });
    }

    public boolean tapOnScreenButton(Vector3 mousepos, String type) {
        float size = 5f;
        Vector2 sound_pos = new Vector2(size*0.5f* PPM, size*0.5f* PPM);

        if(state!="TITLESCREEN" && state!="FADE_IN" && state!="FADE_IN_GAME") {
             if (sound_pos.sub(new Vector2(mousepos.x * (WORLD_WIDTH / Gdx.graphics.getWidth()), REAL_WORLD_HEIGHT - (mousepos.y * (REAL_WORLD_HEIGHT / Gdx.graphics.getHeight())))).len() < (size / 2.0f * PPM)) {
                if (type == "TOUCH_UP") {
                    System.out.println("CHANGE");
                    sound_on = !sound_on;
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    public void render() {
        timer += entities.getTime();
        entities.resetTime();
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);

        if(!loading) {
            app.overlay.begin();
            app.overlay.draw(background, 0.0f, 0.0f, DEVICE_WIDTH, DEVICE_HEIGHT);
            app.overlay.setShader(null);
            app.overlay.end();

            //draw entities
            app.toworld.begin();
            app.toworld.enableBlending();
            app.toworld.setProjectionMatrix(camera.combined);
            int selected = entities.render(app.toworld, shaderHandler, state);
            if(selected!=-1) {
                preferences.setSelected(selected);
            }
            for (int i = 0; i < particleClusters.size(); i++) {
                if (particleClusters.get(i).isCreated()) {
                    particleClusters.get(i).render(app.toworld, shaderHandler);
                }
            }
            if ((state == "PAUSE" || state == "STARTSCREEN") && !started) {
                font_helper.draw(app.toworld, helper_layout, (WORLD_WIDTH / 2.0f) - (helper_layout.width / 2.0f), entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 3.3f));
            }
            if (state == "PAUSE" && started) {
                font_small.draw(app.toworld, instruction_1, (WORLD_WIDTH / 2.0f) - (instruction_1.width / 2.0f), entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 3.6f));
                font_small.draw(app.toworld, instruction_2, (WORLD_WIDTH / 2.0f) - (instruction_2.width / 2.0f), entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 3.6f) - (instruction_1.height * 4f));
                font_small.draw(app.toworld, instruction_3, (WORLD_WIDTH / 2.0f) - (instruction_3.width / 2.0f), entities.getLastDeathHeight() - (REAL_WORLD_HEIGHT * 3.6f) - (instruction_1.height * 8f));
            }
            if(!playing) {
                if(startbutton_down) {
                    playbutton.setSize(new Vector2(12.0f, 12.0f));
                } else {
                    playbutton.setSize(new Vector2(14.0f, 14.0f));
                }
                playbutton.render(app.toworld, shaderHandler);
                title.render(app.toworld, shaderHandler);
                //playbutton.getBody().setTransform(playbutton.getBody().getPosition(), (entities.getAngle()/2.0f));
                title.getBody().setTransform(title.getBody().getPosition().x, REAL_WORLD_HEIGHT * 0.65f / PPM + (entities.getAngle() * 1.5f), title.getBody().getAngle());
            }
            app.toworld.setShader(null);
            app.toworld.end();
            //draw score on
            if(state!="TITLESCREEN" && state!="FADE_IN_GAME") {
                app.overlay.begin();
                if (entities.getGameMode() == "SLALOM" && (state == "PLAY" || state == "PAUSE" || state == "STARTSCREEN")) {
                    score_font.draw(app.overlay, timer_layout, (DEVICE_WIDTH / 2.0f) - (timer_layout.width / 2.0f), (DEVICE_HEIGHT * 0.95f) - (score_layout.height * 1.5f));
                }

                app.overlay.draw(balltexture,
                        (DEVICE_WIDTH*0.95f) - pellet_layout.width - (SCORE_RADIUS*2*PPM),
                        (DEVICE_HEIGHT * 0.94f) - (pellet_layout.height / 2) - (SCORE_RADIUS * PPM / 2),
                        SCORE_RADIUS * PPM,
                        SCORE_RADIUS * PPM);

                font.draw(app.overlay, pellet_layout,
                        (DEVICE_WIDTH*0.95f) - pellet_layout.width,
                        (DEVICE_HEIGHT * 0.94f)
                );

                score_font.draw(app.overlay, score_layout, (DEVICE_WIDTH / 2.0f) - (score_layout.width / 2.0f), (DEVICE_HEIGHT * 0.95f));
                app.overlay.end();

                if (previous_score_opacity != score_opacity) {
                    score_layout.setText(score_font, String.valueOf(score), new Color(1.0f, 1.0f, 1.0f, score_opacity), score_layout.width, 1, false);
                    previous_score_opacity = score_opacity;
                }
            }

            app.overlay.begin();
            // Render UI Elements
            UserInterface.render(app.overlay);

            Color c = app.overlay.getColor();
            app.overlay.setColor(current_color.r, current_color.g, current_color.b, fadein_transparency);
            app.overlay.draw(background, 0.0f, 0.0f, DEVICE_WIDTH, DEVICE_HEIGHT);
            app.overlay.setColor(c.r, c.g, c.b, c.a);

            // TODO: Remove old sound image
//            float size = 5f;
//            if(state!="TITLESCREEN" && state!="FADE_IN" && state!="FADE_IN_GAME") {
//                if (sound_on) {
//                    app.overlay.draw(sound_on_image, size * 0.5f * Constants.PPM, (size * 0.5f * Constants.PPM), size * Constants.PPM, size * Constants.PPM);
//                } else {
//                    app.overlay.draw(sound_off_image, size * 0.5f * Constants.PPM, (size * 0.5f * Constants.PPM), size * Constants.PPM, size * Constants.PPM);
//                }
//            }
            app.overlay.end();

            //debug map at centre of screen
            //b2dr.render(world, camera.projection);
            //debug box2d
            //b2dr.render(world, camera.combined.cpy().scl(PPM));
        }
    }
    public void dispose() {
        world.dispose();
    }
}
