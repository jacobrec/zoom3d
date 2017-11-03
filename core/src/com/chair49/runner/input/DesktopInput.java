package com.chair49.runner.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.chair49.runner.model.World;

/**
 * Created by jacob on 31/10/17.
 */
public class DesktopInput extends Input {
    public DesktopInput(Camera cam) {
        super();
    }

    @Override
    public boolean shouldFire() {
        return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
    }

    @Override
    public boolean shouldJump() {
        return Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W);
    }

    @Override
    public boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT);
    }

    @Override
    public boolean isRightPressed() {
        return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT);
    }

    @Override
    public boolean shouldReset() {
        return Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R);
    }

}
