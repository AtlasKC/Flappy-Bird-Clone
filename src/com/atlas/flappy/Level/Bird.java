package com.atlas.flappy.Level;

import com.atlas.flappy.Graphics.Shader;
import com.atlas.flappy.Graphics.Texture;
import com.atlas.flappy.Graphics.VertexArray;
import com.atlas.flappy.Input.Input;
import com.atlas.flappy.Math.Matrix4f;
import com.atlas.flappy.Math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * File: Bird.java
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

public class Bird {

    private VertexArray mesh;
    private Texture texture;
    private Vector3f position = new Vector3f();
    private float rot, SIZE = 1, delta = 0;
    private int size;

    public Bird() {
        float[] vertices = new float[] {
                -SIZE / 2, -SIZE / 2, 0.2f,
                -SIZE / 2,  SIZE / 2, 0.2f,
                 SIZE / 2,  SIZE / 2, 0.2f,
                 SIZE / 2, -SIZE / 2, 0.2f
        };
        byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
        float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };
        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("res/bird.png");
    }

    public void update(boolean control) {
        position.y -= delta;
        if(Input.isKeyDown(GLFW_KEY_SPACE) && control)
            delta = -0.2f;
        else
            delta += 0.01f;
        if(delta > 0.3)
            delta = 0.3f;
        if(position.y > 4.8)
            position.y = 4.8f;
        if(position.y < -4.8) {
            position.y = -4.8f;
            rot = -180;
        }
        else
            rot = -delta * 150;
    }

    public void render() {
        Shader.BIRD.enable();
        Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot, 0, 0, 1)));
        texture.bind();
        mesh.render();
        Shader.BIRD.disable();
    }

    public void fall() {
        delta = 0.3f;
    }

    public float getY() {
        return position.y;
    }

    public int getSize() {
        return size;
    }
}
