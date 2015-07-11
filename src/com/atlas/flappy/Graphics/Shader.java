package com.atlas.flappy.Graphics;

import com.atlas.flappy.Math.Matrix4f;
import com.atlas.flappy.Math.Vector3f;
import com.atlas.flappy.Utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * File: Shader.java
 * Created by Atlas IND on 3/7/15.
 * [2014] - [2015] Rostiss Development
 * All rights reserved.
 * <p/>
 * NOTICE:  All information contained herein is, and remains
 * the property of Rostiss Development and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Rostiss Development
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Rostiss Development.
 */

public class Shader {

    private Map<String, Integer> locationCache = new HashMap<String, Integer>();
    private final int ID;
    private boolean enabled = false;

    public static final int VERTEX_ATTRIB = 0, TCOORD_ATTRIB = 1;
    public static Shader BG, BIRD, PIPE, FADE;

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BG = new Shader("shaders/bg.rostiss_vs", "shaders/bg.rostiss_fs");
        BIRD = new Shader("shaders/bird.rostiss_vs", "shaders/bird.rostiss_fs");
        PIPE = new Shader("shaders/pipe.rostiss_vs", "shaders/pipe.rostiss_fs");
        FADE = new Shader("shaders/fade.rostiss_vs", "shaders/fade.rostiss_fs");
    }

    public int getUniform(String name) {
        if(locationCache.containsKey(name))
            return locationCache.get(name);
        int result = glGetUniformLocation(ID, name);
        if(result == -1)
            System.err.println("Error: Shader -> getUniform() - Could not find uniform \"" + name + "\".");
        else
            locationCache.put(name, result);
        return result;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if (!enabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) enable();
        glUniformMatrix4(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }
}