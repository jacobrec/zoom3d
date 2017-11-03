package com.chair49.runner.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.chair49.runner.model.Bullets;
import com.chair49.runner.model.Obstacle;
import com.chair49.runner.model.World;
import com.chair49.runner.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 31/10/17.
 */
public class Renderer {

    public Environment environment;
    public Model ship;
    public Model point;
    public Model deadpoint;
    public Model deadbox;
    public Model box;
    public Model ground;
    public Model bullet;
    public ModelBatch modelBatch;
    public ModelInstance shipInstance;
    public ModelInstance groundInstance;
    public int fontSize = 24;
    ShapeRenderer sr;
    Camera cam;
    Camera cam2d;
    BitmapFont guiFont;
    Batch fontBatch;
    Views view = Views.ViewDebug;

    public Renderer(Camera cam) {
        sr = new ShapeRenderer();
        this.cam = cam;
        this.cam.position.z = 10;
        this.cam.near = 1f;
        this.cam.far = 300f;
        this.cam2d = new PerspectiveCamera(67, cam.viewportWidth, cam.viewportHeight);
        sr.setProjectionMatrix(cam.combined);

        setupModels();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        //environment.add(new PointLight().set(1,1,1,new Vector3(10,10,10), 100));


        fontBatch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/lcd.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;

        guiFont = generator.generateFont(parameter);
        generator.dispose();

    }

    private void setupModels() {
        modelBatch = new ModelBatch();

        ModelBuilder modelBuilder = new ModelBuilder();
        box = modelBuilder.createCylinder(Obstacle.raduis * 2, 15, Obstacle.raduis * 2, 100, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        deadbox = modelBuilder.createCylinder(Obstacle.raduis * 2, 15, Obstacle.raduis * 2, 100, new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        ground = modelBuilder.createBox(1000, 1000, 1, new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        bullet = modelBuilder.createCone(Bullets.width*2, Bullets.length, Bullets.width*2, 100, new Material(ColorAttribute.createDiffuse(Color.MAGENTA)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        float sphereSize = 0.5f;
        point = modelBuilder.createSphere(sphereSize, sphereSize, sphereSize, 100, 100, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        deadpoint = modelBuilder.createSphere(sphereSize, sphereSize, sphereSize, 100, 100, new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        groundInstance = new ModelInstance(ground);
        ModelLoader loader = new ObjLoader();

        ship = loader.loadModel(Gdx.files.internal("ship/ship.obj"));

        shipInstance = new ModelInstance(ship);
    }

    public void render(World world){
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        updateCameras(world);

        if (view == Views.View3d || view == Views.ViewBoth || view == Views.ViewDebug || view == Views.View3dTD) {
            draw3D(world);
        }
        if (view == Views.View2d || view == Views.View2dAngle || view == Views.ViewBoth || view == Views.ViewDebug) {
            draw2D(world);
        }


        // Draw GUI
        fontBatch.begin();
        guiFont.draw(fontBatch, String.format("Speed: %.1f KpH", world.ship.velocity.y), 10, fontSize);
        fontBatch.end();
    }

    private void nextView(World world) {

        switch (view) {
            case View2d:
                view = Views.View3dTD;
                break;
            case View3dTD:
                view = Views.View2dAngle;
                break;
            case View2dAngle:
                view = Views.ViewDebug;
                break;
            case ViewDebug:
                view = Views.View3d;
                break;
            case View3d:
                view = Views.ViewBoth;
                break;
            case ViewBoth:
                view = Views.View2d;
                break;
        }
        world.nextView = false;
    }

    private void draw2D(World world) {

        sr.setProjectionMatrix(view == Views.View2d || view == Views.ViewBoth ? cam2d.combined : cam.combined);

        if (!world.ship.isAlive) {
            sr.setColor(1, 0, 0, 1);
        }

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.triangle(world.ship.position.x + world.ship.collisionPoints[0].x, world.ship.position.y + world.ship.collisionPoints[0].y, world.ship.position.x + world.ship.collisionPoints[1].x, world.ship.position.y + world.ship.collisionPoints[1].y, world.ship.position.x + world.ship.collisionPoints[2].x, world.ship.position.y + world.ship.collisionPoints[2].y);
        sr.end();

        sr.setColor(1, 1, 1, 1);

        sr.begin(ShapeRenderer.ShapeType.Line);
        for (Obstacle ob : world.obstacles) {
            if (!ob.isAlive) {
                sr.setColor(1, 0, 0, 1);
                sr.circle(ob.position.x, ob.position.y, Obstacle.raduis, 50);
                sr.setColor(1, 1, 1, 1);
            } else
                sr.circle(ob.position.x, ob.position.y, Obstacle.raduis, 50);
        }

        for (Bullets b : world.ship.bullets) {
            sr.triangle(b.position.x, b.position.y, b.position.x - Bullets.width / 2, b.position.y - Bullets.length, b.position.x + Bullets.width / 2, b.position.y - Bullets.length);

        }
        sr.end();

    }

    private void updateCameras(World world){
        this.cam2d.position.y = world.ship.position.y + 3;
        this.cam2d.position.x += Math.pow(((world.ship.position.x - this.cam2d.position.x) / 9), 3);
        this.cam2d.position.z = 10;
        this.cam2d.update();


        cam.position.y = world.ship.position.y - 8;
        cam.position.z = 4;
        this.cam.position.x += ((world.ship.position.x - this.cam.position.x) / 5);
        cam.lookAt(world.ship.position);
        cam.update();

        if (world.nextView) {
            this.nextView(world);
        }

    }

    private void draw3D(World world) {

        shipInstance.transform.setToRotation(0, 1, 1, 180);
        shipInstance.transform.rotate(Vector3.Z, world.ship.velocity.x);
        shipInstance.transform.setTranslation(world.ship.position.x, world.ship.position.y, world.ship.position.z);

        groundInstance.transform.setTranslation(world.ship.position.x, world.ship.position.y, -1f);




        modelBatch.begin(view == Views.View3dTD ? cam2d : cam);
        modelBatch.render(groundInstance, environment);
        modelBatch.render(shipInstance, environment);
        for (int i = 0; i < world.obstacles.size(); i++) {
            ModelInstance mi;
            if (!world.obstacles.get(i).isAlive) {
                mi = new ModelInstance(deadbox);
            } else {
                mi = new ModelInstance(box);
            }
            mi.transform.setToRotation(1, 0, 0, 90);
            mi.transform.setTranslation(world.obstacles.get(i).position.x, world.obstacles.get(i).position.y, world.obstacles.get(i).position.z);
            modelBatch.render(mi, environment);
        }

        for (Bullets b : world.ship.bullets) {
            ModelInstance mi;
            mi = new ModelInstance(bullet);
            mi.transform.setTranslation(b.position.x, b.position.y, b.position.z);
            modelBatch.render(mi, environment);

        }

        if (GameScreen.debug) {
            ModelInstance mi = new ModelInstance(point);
            if (!world.ship.isAlive) {
                mi = new ModelInstance(deadpoint);
            }
            for (Vector3 testPoint : world.ship.collisionPoints) {

                mi.transform.setTranslation(testPoint.x + world.ship.position.x, testPoint.y + world.ship.position.y, testPoint.z + world.ship.position.z);
                modelBatch.render(mi, environment);
            }

        }

        modelBatch.end();

    }

    enum Views {
        View3d, View2d, View2dAngle, ViewBoth, ViewDebug, View3dTD
    }
}
