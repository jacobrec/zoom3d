package com.chair49.runner.input;

import com.badlogic.gdx.Gdx;
import com.chair49.runner.model.World;

/**
 * Created by jacob on 31/10/17.
 */
public abstract class Input {
    public abstract boolean shouldFire();
    public abstract boolean shouldJump();
    public abstract boolean isLeftPressed();
    public abstract boolean isRightPressed();
    public abstract boolean shouldReset();


    public void handleInput(World world){
        if(this.isLeftPressed()){
            world.ship.velocity.x += 1;
        }
        if(this.isRightPressed()){
            world.ship.velocity.x -= 1;
        }
        if(this.shouldReset()){
            world.resetWorld();
        }
        if(this.shouldFire()){
            world.ship.pullTrigger();
        }
        if(this.shouldJump()){
            world.nextView();
        }

    }
}
