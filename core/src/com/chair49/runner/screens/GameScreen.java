package com.chair49.runner.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.chair49.runner.input.DesktopInput;
import com.chair49.runner.input.Input;
import com.chair49.runner.model.World;
import com.chair49.runner.view.Renderer;

/**
 * Created by jacob on 31/10/17.
 */
public class GameScreen implements Screen {

    public static final boolean debug = false;


    Camera cam;

    World world;
    Input input;
    Renderer renderer;

    @Override
    public void show() {
        cam = new PerspectiveCamera(50,16, 9);

        world = new World(cam);
        renderer = new Renderer(cam);
        input = new DesktopInput(cam);
    }

    @Override
    public void render(float delta) {
        input.handleInput(world);
        world.step(delta);
        renderer.render(world);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
