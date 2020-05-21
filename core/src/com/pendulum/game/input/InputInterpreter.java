package com.pendulum.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulum.game.objects.EntityHandler;
import com.pendulum.game.state.PlayScreen;

import java.util.ArrayList;

import static com.pendulum.game.utils.Constants.PPM;

/**
 * Created by Harry on 03/02/2017.
 */

public class InputInterpreter {
    private Vector3 original_location;
    private Vector3 original_press;
    private boolean pan;
    private float WORLD_WIDTH;
    private float REAL_WORLD_HEIGHT;
    private float currentscrollpos;
    private float collectiblescreens = 0.5f;

    private boolean collectible_down = false;

    private final float scroll_speed = -30f;

    public InputInterpreter(float WORLD_WIDTH, float REAL_WORLD_HEIGHT) {
        this.REAL_WORLD_HEIGHT = REAL_WORLD_HEIGHT;
        this.WORLD_WIDTH = WORLD_WIDTH;
    }
    public String handleInput(ArrayList<InputEvent> events, String state, Camera camera, EntityHandler entities, World world, PlayScreen parent) {
        for (int i = 0; i < events.size(); i++) {

            // Input Information
            String type = events.get(i).getTYPE();
            Vector3 position = new Vector3(Gdx.input.getX(),
                    Gdx.input.getY(), 0);

            // Send finger down and finger up information to update UI
            boolean isUIEvent = false;
            if(type == "TOUCH_DOWN") { isUIEvent = isUIEvent || parent.touchDown(position); }
            if(type == "TOUCH_UP") { isUIEvent = isUIEvent || parent.touchUp(position); }

            if(isUIEvent) { break; };

            if (!(parent.tapOnScreenButton(position, type))) {
                if (state == "TITLESCREEN") {
                    if (type == "TAP" || type == "TOUCH_UP") {
                        parent.screenTap(position, type);
                    }
                    if (type == "TOUCH_DOWN" || type == "PAN") {
                        parent.screenDown(position, type);
                    }
                }
                if (state == "PLAY") {
                    if (entities.getGameMode() != "SWINGS") {
                        if (type == "TOUCH_DOWN") {
                            original_location = new Vector3(Gdx.input.getX(),
                                    Gdx.input.getY(), 0);
                            Vector3 position_unproject = camera.unproject(new Vector3(events.get(i).getPOSITION().x, events.get(i).getPOSITION().y, 0));
                            Vector2 playertomouse = new Vector2(position_unproject.x - (entities.getPlayer().getPosition().x * PPM), position_unproject.y - (entities.getPlayer().getPosition().y * PPM));
                            original_press = position_unproject;
                            if (playertomouse.len() > 2 * PPM) {
                                entities.destroyJoint(world);
                                entities.createRope(position_unproject, world);
                            }
                        }
                        if (type == "PAN") {
                            if (original_press != null) {
                                float maxmovement = 0.5f;

                                if (Math.abs(position.x - original_location.x) < PPM * maxmovement) {
                                    entities.moveJoint(new Vector3(original_press.x + (position.x - original_location.x), original_press.y, 0.0f));
                                } else {
                                    if (position.x > original_location.x) {
                                        entities.moveJoint(new Vector3(original_press.x + (maxmovement * PPM), original_press.y, 0.0f));
                                    } else {
                                        entities.moveJoint(new Vector3(original_press.x - (maxmovement * PPM), original_press.y, 0.0f));
                                    }
                                }
                            }
                        }
                        if (type == "TOUCH_UP") {
                            entities.destroyJoint(world);
                        }
                    } else {
                        if (type == "TAP") {
                            entities.destroyConnection(world);
                        }
                    }
                } else if (state == "STARTSCREEN") {
                    if (type == "TOUCH_DOWN") {
                        original_location = new Vector3(Gdx.input.getX(),
                                Gdx.input.getY(), 0);
                        pan = false;
                    }
                    if (type == "TOUCH_UP") {
                        if (state != "GAMEMODE_CHANGE") {
                            state = "GAMEMODE_CHANGE";
                            if (pan && (position.x - original_location.x) > WORLD_WIDTH * (2.0f / 3.0f)) {
                                //SCROLL TO LEFT
                                parent.gamemodeChange("RIGHT");
                            } else if (pan && (position.x - original_location.x) < -WORLD_WIDTH * (2.0f / 3.0f)) {
                                //SCROLL RIGHT
                                parent.gamemodeChange("LEFT");
                            } else if (pan) {
                                //RETURN
                                parent.gamemodeChange("RETURN");
                            } else {
                                state = "PLAY";
                                parent.play();
                                entities.startPlatform(world);
                            }
                        }
                    }
                    if (type == "PAN") {
                        pan = true;
                        //ADD IN FOR GAME MODES
                        parent.setXPan(((position.x - original_location.x) / 4.0f));
                    }
                } else if (state == "GAMEOVER") {
                    if (type == "TOUCH_DOWN") {
                        original_location = new Vector3(Gdx.input.getX(),
                                Gdx.input.getY(), 0);
                        pan = false;
                        parent.screenDown(original_location, "TOUCH_DOWN");
                    }
                    if (type == "TAP") {
                        //SCROLL DOWN TO STARTSCREEN
                        parent.screenTap(position, "TAP");
                    }
                    if (type == "TOUCH_UP") {
                        if (state != "SCROLL") {
                            if (pan && (position.y - original_location.y) > REAL_WORLD_HEIGHT * (2.0f / 3.0f)) {
                                //SCROLL FROM GAMEOVER SCREEN TO COLLECTIBLES
                                state = "SCROLL";
                                collectible_down = false;
                                parent.tap();
                                parent.scroll(-scroll_speed, parent.getGameOverYpos() + REAL_WORLD_HEIGHT, "COLLECTIBLES", "none");
                            } else if (pan && position.y > original_location.y) {
                                //SCROLL BACK TO GAMEOVER SCREEN SCROLL UP WASNT LARGE ENOUGH
                                state = "SCROLL";
                                parent.scroll(scroll_speed, parent.getGameOverYpos(), "GAMEOVER", "none");
                            } else {
                                parent.screenTap(position, "TOUCH_UP");
                            }
                        }
                        parent.pan(0.0f);
                    }
                    if (type == "PAN") {
                        pan = true;
                        if (position.y > original_location.y) {
                            parent.pan(((position.y - original_location.y) / 4.0f));
                            camera.position.set(camera.position.x, parent.getGameOverYpos() + ((position.y - original_location.y) / 4.0f), camera.position.z);
                        } else {
                            parent.screenDown(position, "PAN");
                        }
                        camera.update();
                    }
                } else if (state == "COLLECTIBLES") {
                    if (type == "TOUCH_DOWN") {
                        original_location = new Vector3(Gdx.input.getX(),
                                Gdx.input.getY(), 0);
                        collectible_down = true;
                        pan = false;
                        currentscrollpos = camera.position.y;
                    }
                    if (type == "TAP") {
                        Vector3 realworldmouse = camera.unproject(new Vector3(events.get(i).getPOSITION().x, events.get(i).getPOSITION().y, 0.0f)).scl(1 / PPM);
                        parent.checkCollectibles(realworldmouse);

                        parent.screenTap(position, "TAP");
                    }
                    if (type == "TOUCH_UP") {
                        parent.pan(0.0f);

                        //make sure not buyingif(state == "FALL_DOWN") {
                        //if(state != "SCROLL_2") {
                        if (pan && (currentscrollpos + ((position.y - original_location.y))) - (parent.getGameOverYpos() + REAL_WORLD_HEIGHT) < -REAL_WORLD_HEIGHT / 2f) {
                            state = "SCROLL";
                            parent.scroll(scroll_speed, parent.getGameOverYpos(), "GAMEOVER", "none");
                            parent.tap();
                        } else if (pan && position.y < original_location.y) {
                            state = "SCROLL";
                            parent.scroll(-scroll_speed, parent.getGameOverYpos() + REAL_WORLD_HEIGHT, "COLLECTIBLES", "none");
                        } else if (pan && position.y > original_location.y) {
                            state = "SCROLL";
                            parent.scroll(scroll_speed, (parent.getGameOverYpos() + (REAL_WORLD_HEIGHT * (1.0f + collectiblescreens))), "COLLECTIBLES", "none");
                        } else {
                            parent.screenTap(position, "TOUCH_UP");
                        }
                        // }
                    }
                    if (type == "PAN") {
                        if (collectible_down) {
                            pan = true;

                            if (position.y > original_location.y) {
                                //scroll up
                                if ((currentscrollpos + ((position.y - original_location.y) / 2.0f)) > parent.getGameOverYpos() + (REAL_WORLD_HEIGHT * (1.0f + collectiblescreens))) {
                                    float over = (currentscrollpos + ((position.y - original_location.y) / 2.0f)) - (parent.getGameOverYpos() + (REAL_WORLD_HEIGHT * (1.0f + collectiblescreens)));
                                    camera.position.set(camera.position.x, parent.getGameOverYpos() + (REAL_WORLD_HEIGHT * (1.0f + collectiblescreens)) + (over / 4.0f), camera.position.z);
                                } else {
                                    camera.position.set(camera.position.x, currentscrollpos + ((position.y - original_location.y) / 2.0f), camera.position.z);
                                }
                            } else {
                                //scroll down
                                if ((currentscrollpos + ((position.y - original_location.y) / 2.0f)) < parent.getGameOverYpos() + REAL_WORLD_HEIGHT) {
                                    float under = (currentscrollpos + ((position.y - original_location.y) / 2.0f)) - (parent.getGameOverYpos() + REAL_WORLD_HEIGHT);
                                    parent.pan(under);
                                    camera.position.set(camera.position.x, parent.getGameOverYpos() + REAL_WORLD_HEIGHT + (under / 4.0f), camera.position.z);
                                } else {
                                    camera.position.set(camera.position.x, currentscrollpos + ((position.y - original_location.y) / 2.0f), camera.position.z);
                                }
                            }
                            camera.update();
                        }
                    }
                } else if (state == "PAUSE") {
                    if (type == "TAP") {
                        entities.destroyJoint(world);
                        parent.pause_change();
                    }
                }

            }
        }
        return state;
    }
}
