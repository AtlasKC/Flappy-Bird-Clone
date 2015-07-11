package com.atlas.flappy;

import com.atlas.flappy.Graphics.Shader;
import com.atlas.flappy.Input.Input;
import com.atlas.flappy.Level.Level;
import com.atlas.flappy.Math.Matrix4f;

import java.io.File;
import java.nio.ByteBuffer;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * File: Main.java
 * Created by Atlas IND on 3/6/15.
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

public class Main implements Runnable {

    private Level level;
    private Thread thread;
    private GLFWKeyCallback keyCallback;
    private int width = 1280, height = 720;
    private long window;
    private boolean running;

    public void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        if(glfwInit() != GL_TRUE) {
            System.out.println("Error: Main - init() Couldn't initialize GLFW.");
            return;
        }
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, "Flappy Bird Clone by Atlas IND", NULL, NULL);
        if(window == NULL) {
            System.out.println("Error: Main - init() Couldn't create window.");
            return;
        }
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
        glfwSetKeyCallback(window, keyCallback = new Input());
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GLContext.createFromCurrent();
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        Shader.loadAll();
        Matrix4f pr_matrix = Matrix4f.orthographic(-10, 10, -10 * 9 / 16, 10 * 9 / 16, -1, 1);
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);
        Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BIRD.setUniform1i("tex", 1);
        Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PIPE.setUniform1i("tex", 1);
        level = new Level();
    }

    public void run() {
        init();
        long start = System.nanoTime();
        long timer = System.currentTimeMillis();
        double ns = 1000000000 / 60;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now - start) / ns;
            start = now;
            if(delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + "ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            if(glfwWindowShouldClose(window) == GL_TRUE)
                running = false;
        }
        keyCallback.release();
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void update() {
        glfwPollEvents();
        level.update();
        if(level.gameOver() && Input.isKeyDown(GLFW_KEY_ENTER))
            level = new Level();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        if (glGetError() != GL_NO_ERROR)
            System.out.println(glGetError());
        glfwSwapBuffers(window);
    }

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        new Main().start();
    }
}