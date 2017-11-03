package com.chair49.runner.model;

import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jacob on 31/10/17.
 */
public class Ship {
    public Vector3 position;
    public Vector3 velocity;
    public Vector3 acceleration;

    public Vector3 collisionPoints[];

    public boolean isAlive;

    public List<Bullets> bullets;


    public Ship(){
        this.position = new Vector3();
        this.velocity = new Vector3();
        this.acceleration = new Vector3(0,1,0);
        isAlive = true;
        collisionPoints = new Vector3[3];
        collisionPoints[0] = new Vector3(0,0.5f,0);
        collisionPoints[1] = new Vector3(-0.6f,0,0);
        collisionPoints[2] = new Vector3(0.6f,0,0);

        shotTime = System.currentTimeMillis();
        bullets = new LinkedList<Bullets>();
    }

    public void move(float delta) {
        this.velocity.add(this.acceleration.cpy().scl(delta));

        this.velocity.x *= (1-delta);
        this.position.add(this.velocity.cpy().scl(delta));

        for(Bullets b : bullets){
            b.position.add(b.velocity.cpy().scl(delta));
        }
    }


    public static final long bulletCooldown = 200;
    public long shotTime;
    public void pullTrigger() {
         if((System.currentTimeMillis() - shotTime) > bulletCooldown){
            shotTime = System.currentTimeMillis();
            bullets.add(new Bullets(this.collisionPoints[0].cpy().add(this.position), this.velocity.cpy().add(0,this.velocity.y * 3,0)));
        }
    }
}
