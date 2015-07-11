package com.atlas.flappy.Math;

import com.atlas.flappy.Utils.BufferUtils;

import java.nio.FloatBuffer;

import static java.lang.Math.*;

/**
 * File: Matrix4f.java
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

public class Matrix4f {

    private static final int SIZE = 4 * 4;

    public float[] elements = new float[SIZE];

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();
        for (int i = 0; i < SIZE; i++) {
            result.elements[i] = 0;
        }
        result.elements[0 + 0 * 4] = 1;
        result.elements[1 + 1 * 4] = 1;
        result.elements[2 + 2 * 4] = 1;
        result.elements[3 + 3 * 4] = 1;
        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = identity();
        result.elements[0 + 0 * 4] = 2 / (right - left);
        result.elements[1 + 1 * 4] = 2 / (top - bottom);
        result.elements[2 + 2 * 4] = 2 / (near - far);
        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);
        return result;
    }

    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = identity();
        result.elements[0 + 3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;
        return result;
    }

    public static Matrix4f rotate(float angle, float x, float y, float z) {
        Matrix4f result = identity();
        float radians = (float) toRadians(angle);
        float cos = (float) cos(radians);
        float sin = (float) sin(radians);
        float omc = 1 - cos;
        result.elements[0 + 0 * 4] = x * omc + cos;
        result.elements[1 + 0 * 4] = y * x * omc + z * sin;
        result.elements[2 + 0 * 4] = x * z * omc - y * sin;
        result.elements[0 + 1 * 4] = x * y * omc - z * sin;
        result.elements[1 + 1 * 4] = y * omc + cos;
        result.elements[1 + 1 * 4] = cos;
        result.elements[2 + 1 * 4] = y * z * omc + x * sin;
        result.elements[0 + 2 * 4] = x * z * omc + y * sin;
        result.elements[1 + 2 * 4] = y * z * omc - x * sin;
        result.elements[2 + 2 * 4] = z * omc  + cos;
        return result;
    }

    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f result = new Matrix4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sum = 0;
                for (int elem = 0; elem < 4; elem++) {
                    sum += elements[col + elem * 4] * matrix.elements[elem + row * 4];
                }
                result.elements[col + row * 4] = sum;
            }
        }
        return result;
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}