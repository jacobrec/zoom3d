package com.chair49.runner.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jacob on 31/10/17.
 */
public class World {
    public Ship ship;
    public Camera cam;
    public List<Obstacle> obstacles;
    float randomPlacementBounds = 5;


    public World(Camera cam) {
        this.cam = cam;
        resetWorld();
    }

    public void resetWorld() {
        ship = new Ship();
        obstacles = new LinkedList<Obstacle>();
        ship.velocity.y = 10;

        for (int i = 0; i < 400; i++) {
            obstacles.add(new Obstacle(new Vector3(MathUtils.random(-cam.viewportWidth * randomPlacementBounds, cam.viewportWidth * randomPlacementBounds), i / 2 + 10, 0)));
        }
    }

    public void step(float delta) {
        if (ship.isAlive)
            ship.move(delta);

        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).position.y + 10 < ship.position.y) {
                obstacles.get(i).position.y += 200;
                obstacles.get(i).position.x = MathUtils.random(-cam.viewportWidth * randomPlacementBounds, cam.viewportWidth * randomPlacementBounds);
            }
            if (obstacles.get(i).position.x + cam.viewportWidth * randomPlacementBounds < ship.position.x) {
                obstacles.get(i).position.x += cam.viewportWidth * randomPlacementBounds * 2;
            } else if (obstacles.get(i).position.x - cam.viewportWidth * randomPlacementBounds > ship.position.x) {
                obstacles.get(i).position.x -= cam.viewportWidth * randomPlacementBounds * 2;
            }
            if (hitsShip(obstacles.get(i), ship)) {
                ship.isAlive = false;
                obstacles.get(i).isAlive = false;

            }
        }
    }

    private boolean hitsShip(Obstacle obstacle, Ship ship) {
        if (Math.abs(ship.position.y - obstacle.position.y) > 1f || Math.abs(ship.position.x - obstacle.position.x) > 1f) {
            return false;
        }
        for (Vector3 v : ship.collisionPoints) {
            Vector2 shipPoint = new Vector2(v.cpy().add(ship.position).x, v.cpy().add(ship.position).y);
            Vector2 obPoint = new Vector2(obstacle.position.x, obstacle.position.y);
            if (shipPoint.dst(obPoint) <= Obstacle.raduis) {
                return true;
            }
        }
        //return ship.position.dst(obstacle.position) <= (0.25f + 0.1f);
        return false;
    }

    public boolean nextView = false;
    public void nextView() {
        nextView = true;
    }
}
