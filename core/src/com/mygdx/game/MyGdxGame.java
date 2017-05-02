package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch; //images uploaded
	TextureRegion stand, jump; //load in sprite two regions of texture jumping and standing
	Animation walk;
	float time; // need to figure out which key frame to display

	float x, y, xv, yv; //movement
	boolean canJump, faceRight = true;

	static final int WIDTH = 18; //two constants to represent the sprite in the game
	static final int HEIGHT = 26; //split texture by 18 and 26 chunks
	static final int DRAW_WIDTH = WIDTH*3;
	static final int DRAW_HEIGHT = HEIGHT*3; ///make bear bigger
	static final float MAX_VELOCITY = 500; //movement
	static final float MAX_JUMP_VELOCITY = 2000;
	static final int GRAVITY = -50;


	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture sheet = new Texture("koalio.png"); //entire sprite region of texture//regions of our original texture
		TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT); //takes in texture and splits into 2 demination array of textures into another array
		stand = tiles[0][0];
		jump = tiles[0][1];
		walk = new Animation(0.2f, tiles[0][2], tiles[0][3], tiles[0][4]);
	}

	@Override
	public void render () {
		time += Gdx.graphics.getDeltaTime(); //time spent between render call // pass in anmination to get key frame
		move();

		TextureRegion img;
		if (y > 0) {
			img = jump;
		}
		else if (xv != 0) {
			img = (TextureRegion) walk.getKeyFrame(time, true);
		}
		else {
			img = stand; //if not jumping or moving left or right display "stand"
		}

		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1); //background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (faceRight) {
			batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);
		}
		else {
			batch.draw(img, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
		}
		//batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);//added draw with height
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	float decelerate(float velocity) {
		float deceleration = 0.95f; // the closer to 1, the slower the deceleration
		velocity *= deceleration;
		if (Math.abs(velocity) < 1) {
			velocity = 0;
		}
		return velocity;
	}

	void move() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && canJump) {
			yv = MAX_JUMP_VELOCITY;
			canJump = false;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			yv = MAX_VELOCITY * -1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			xv = MAX_VELOCITY;
			faceRight = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			xv = MAX_VELOCITY * -1;
			faceRight = false;
		}

		yv += GRAVITY; //acceleration to gravity

		y += yv * Gdx.graphics.getDeltaTime();//calculates new positions x and y for velocities and time
		x += xv * Gdx.graphics.getDeltaTime();
		//if hes below or at x and y
		//set him to y = 0
		if (y < 0) {
			y = 0;
			canJump = true;
		}

		yv = decelerate(yv);
		xv = decelerate(xv);
	}
}
