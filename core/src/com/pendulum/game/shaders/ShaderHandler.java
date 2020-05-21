package com.pendulum.game.shaders;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Harry on 01/02/2017.
 */

public class ShaderHandler {
    ShaderProgram blurShader;

    final String VERT =
            "attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
            "attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
            "attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 vColor;\n" +
            "varying vec2 vTexCoord;\n" +
            "void main () { \n"+
                "vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                "vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
                "gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
            "}";

    final String FRAG =
            "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" +
            "varying vec4 vColor;\n" +
            "varying vec2 vTexCoord;\n" +
            "uniform vec4 Color;\n" +
            "uniform int tex;\n" +
            "uniform float transparency;\n" +
            "uniform sampler2D u_texture; \n" +
            "void main () { \n"+
                "if (tex==1) {\n" +
                "vec4 color = texture2D(u_texture, vTexCoord); if(color.w==0.0) {gl_FragColor = color;} else {gl_FragColor = vec4(color.x, color.y, color.z, transparency);}} else {\n" +
                "gl_FragColor = Color;} \n" +
            "}";

    public ShaderProgram getBlurShader() {
        return blurShader;
    }
    public void create(float DEVICE_WIDTH, float DEVICE_HEIGHT) {
        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        blurShader = new ShaderProgram(VERT, FRAG);
        if (!blurShader.isCompiled()) {
            System.err.println(blurShader.getLog());
            System.exit(0);
        }
    }
}
