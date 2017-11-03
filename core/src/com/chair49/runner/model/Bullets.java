package com.chair49.runner.model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by jacob on 01/11/17.
 */
public class Bullets {
    public static float width = 0.1f;
    public static float length = 0.6f;
    public Vector3 position;
    public Vector3 velocity;

    public Bullets(Vector3 position, Vector3 velocity) {
        this.position = position;
        this.velocity = velocity;
    }
}
