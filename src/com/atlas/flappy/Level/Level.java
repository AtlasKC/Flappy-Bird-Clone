package com.atlas.flappy.Level;

import com.atlas.flappy.Graphics.Shader;
import com.atlas.flappy.Graphics.Texture;
import com.atlas.flappy.Graphics.VertexArray;
import com.atlas.flappy.Math.Matrix4f;
import com.atlas.flappy.Math.Vector3f;
import org.lwjgl.Sys;

import java.util.Random;

/**
 * File: Level.java
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

public class Level {

    private Bird bird;
    private Texture bgTexture;
    private VertexArray background, fade;
    private Pipe[] pipes = new Pipe[5 * 2];
    private Random random = new Random();
    private int xScroll = 0, map = 0, index = 0;
    private float OFFSET = 5, time = 0;
    private boolean control = true;

    public Level() {
        float[] vertices = new float[] {
            -10, -10 * 9 / 16, 0,
            -10,  10 * 9 / 16, 0,
              0,  10 * 9 / 16, 0,
              0, -10 * 9 / 16, 0
        };
        byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
        float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };
        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/bg.jpeg");
        fade = new VertexArray(6);
        bird = new Bird();
        createPipes();
    }

    public void update() {
        if(control) {
            xScroll--;
            if (-xScroll % 335 == 0) map++;
            if (-xScroll > 250 && -xScroll % 120 == 0)
                updatePipes();
        }
        bird.update(control);
        if(control && collision()) {
            control = false;
            bird.fall();
        }
        time += 0.01f;
    }

    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        Shader.BG.setUniform2f("bird", 0, bird.getY());
        background.bind();
        for(int i = map; i < map + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0, 0)));
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();
        renderPipes();
        bird.render();
        Shader.FADE.enable();
        Shader.FADE.setUniform1f("time", time);
        fade.render();
        Shader.FADE.disable();
    }

    public boolean gameOver() {
        return !control;
    }

    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0, 0)));
        Shader.PIPE.setUniform2f("bird", 0, bird.getY());
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();
        for(int i = 0; i < 5 * 2; i++) {
            if(i % 2 == 0)
                Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getMLMatrix().multiply(Matrix4f.rotate(180, 0, 0, 1)).multiply(Matrix4f.translate(new Vector3f(-Pipe.getWidth(), -Pipe.getHeight(), 0))));
            else
                Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getMLMatrix());
            Pipe.getMesh().draw();
        }
        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
    }

    private void createPipes() {
        Pipe.create();
        for(int i = 0; i < 5 * 2; i += 2) {
            pipes[i] = new Pipe(OFFSET + index * 3, random.nextFloat() * 4);
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.5f);
            index += 2;
        }
    }

    private void updatePipes() {
        pipes[index % 10] = new Pipe(OFFSET + index * 3, random.nextFloat() * 4);
        pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 11.5f);
        index += 2;
    }

    private boolean collision() {
        for(int i = 0; i < 5 * 2; i++) {
            float bx = -xScroll * 0.05f;
            float by = bird.getY();
            float px = pipes[i].getX();
            float py = pipes[i].getY();
            float bx0 = bx - bird.getSize() / 2;
            float bx1 = bx + bird.getSize() / 2;
            float by0 = by - bird.getSize() / 2;
            float by1 = by + bird.getSize() / 2;
            float px0 = px;
            float px1 = px + Pipe.getWidth();
            float py0 = py;
            float py1 = py + Pipe.getHeight();
            if(bx1 > px0 && bx0 < px1)
                if(by1 > py0 && by0 < py1)
                    return true;
        }
        return false;
    }
}