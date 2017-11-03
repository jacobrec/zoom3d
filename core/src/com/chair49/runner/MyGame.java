package com.chair49.runner;

import com.badlogic.gdx.Game;
import com.chair49.runner.screens.GameScreen;

public class MyGame extends Game {

	public void create() {
		this.setScreen(new GameScreen());
	}
}
