package com.atlas.flappy.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * File: ShaderUtils.java
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

public class ShaderUtils {

    private ShaderUtils() {}

    public static int load(String vPath, String fPath) {
        String vert = FileUtils.loadAsString(vPath);
        String frag = FileUtils.loadAsString(fPath);
        return create(vert, frag);
    }

    public static int create(String vert, String frag) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);
        glCompileShader(vertID);
        if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error: ShaderUtils.create() - Failed to compile vertex shader.");
            System.err.println(glGetShaderInfoLog(vertID));
            return -1;
        }
        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error: ShaderUtils.create() - Failed to compile fragment shader.");
            System.err.println(glGetShaderInfoLog(fragID));
            return -1;
        }
        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);
        glDeleteShader(vertID);
        glDeleteShader(fragID);
        return program;
    }
}
