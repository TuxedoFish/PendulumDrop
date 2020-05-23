package com.pendulum.game.utils;

/**
 * Created by Harry on 27/12/2016.
 */

public final class Constants {
    public static final float PPM = 32;

    //USEFUL CONVERTER FOR DECIMAL TO SIGNED SHORT FORMAT http://www.binaryconvert.com/result_signed_short.html?decimal=054052

    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_JOINTS = 0x0002;
    public static final short CATEGORY_ROPE = 0x0004;
    public static final short CATEGORY_SCENERY = 0x0008;
    public static final short CATEGORY_TEXT = 0x0010;
    public static final short CATEGORY_PELLET = 0x0020;
    public static final short CATEGORY_PARTICLE = 0x0040;
    public static final short CATEGORY_POST = 0x0080;
    public static final short CATEGORY_SIDES = 0x0100;

    public static final short MASK_PLAYER = CATEGORY_SCENERY | CATEGORY_PELLET | CATEGORY_JOINTS | CATEGORY_SIDES;
    public static final short MASK_JOINTS = CATEGORY_PLAYER;
    public static final short MASK_ROPE = -1;
    public static final short MASK_SCENERY = CATEGORY_PLAYER | CATEGORY_PARTICLE;
    public static final short MASK_TEXT = CATEGORY_TEXT;
    public static final short MASK_PARTICLE = CATEGORY_SCENERY;
    public static final short MASK_PELLET = CATEGORY_PLAYER;
    public static final short MASK_POST = CATEGORY_TEXT;

    public static final float FREQUENCY = (float)(Math.PI*2.0f)/50.0f;

    public static final int STEP = 25;

    public static final float PLAYER_RADIUS = 2f;
    public static final float JOINT_RADIUS = 2f;
    public static final float SCORE_RADIUS = 1.8f;
}
