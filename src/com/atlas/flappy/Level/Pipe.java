package com.atlas.flappy.Level;

import com.atlas.flappy.Graphics.Texture;
import com.atlas.flappy.Graphics.VertexArray;
import com.atlas.flappy.Math.Matrix4f;
import com.atlas.flappy.Math.Vector3f;

/**
 * File: Pipe.java
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

public class Pipe {

    private static Texture texture;
    private static VertexArray mesh;
    private static float width = 1.5f, height = 8;
    private Matrix4f ml_matrix;
    private Vector3f position = new Vector3f();

    public Pipe(float x, float y) {
        position.x = x;
        position.y = y;
        ml_matrix = Matrix4f.translate(position);
    }

    public static void create() {
        float[] vertices = new float[] {
                0,      0, 0.1f,
                0, height, 0.1f,
            width, height, 0.1f,
            width,      0, 0.1f
        };
        byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
        float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };
        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("res/pipe.png");
    }

    public float getX() { return position.x; }

    public float getY() { return position.y; }

    public static Texture getTexture() {
        return texture;
    }

    public static VertexArray getMesh() {
        return mesh;
    }

    public static float getWidth() { return width; }

    public static float getHeight() { return height; }

    public Matrix4f getMLMatrix() {
        return ml_matrix;
    }
}