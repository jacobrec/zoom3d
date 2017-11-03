package com.chair49.runner.model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by jacob on 31/10/17.
 */
public class Obstacle {
    public Vector3 position;
    public boolean isAlive;

    public static float raduis = 0.5f;

    public Obstacle(Vector3 position) {
        this.position = position;
        isAlive = true;
    }
}
