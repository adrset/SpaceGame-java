package scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import celestial.DataObject;
import celestial.Light;
import entities.Camera3D;
import entities.Entity;
import game.Game;
import input.Keyboard;
import models.TexturedModel;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.ResourceCache;
import textures.ModelTexture;
import utils.Timer;

/**
 * Scene class. Contains UI and takes care of all logic in game.
 *
 * @author Adrian Setniewski
 *
 */

public class Scene {

	// Data
	private DataObject dataObject;

	// Misc
	private Camera3D camera;
	private SceneLoader sceneLoader;
	private String currentLevel;

	ParticleSystem system;

	// Finish booleans
	public static boolean isFinished;
	public static int isAboutEnd = 0;

	public Scene(SceneLoader sceneLoader, String level, DataObject dataObject) {
		this.sceneLoader = sceneLoader;
		this.dataObject = dataObject;
		isFinished = false;
		currentLevel = level;
		init();
	}

	private void loadLevel(String level) {

		sceneLoader.load(level, dataObject);// loads planets, lights and player

		Game.renderer.setPlayer(dataObject.getPlayer());

		// Camera stuff
		camera = new Camera3D(dataObject.getPlayer());

		ParticleTexture tex = new ParticleTexture(ResourceCache.loadTexture("image", Game.loader), 4);
		system = new ParticleSystem(tex, 300, 20, 1f, 4, 4); // max 10000 instances!!
		Random generator = new Random();
		TexturedModel model = new TexturedModel(ResourceCache.loadOBJ("untitled", Game.loader),
				new ModelTexture(ResourceCache.loadTexture("2", Game.loader)));
		
		List<Entity> allEntities = new ArrayList<Entity>();
		
		for (int i = 0; i < 1000; i++) {
			allEntities.add(new Entity(model, new Vector3f((float) (generator.nextInt(1000)), generator.nextInt(10),
					(float) (generator.nextInt(1000))), 0, 0, 0, 5f, new Vector3f()));
		}
		dataObject.setEntities(allEntities);
		Game.renderer.setInstanceEntities(dataObject.getEntities());
	}

	public void init() {

		loadLevel(currentLevel);
	}

	public void cleanUp() {

	}
	
	public void updateLogic() {
		dataObject.getPlayer().move();

		camera.move();

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_1))
			system.generateParticles(dataObject.getPlayer().getPosition());
		
		ParticleMaster.update(camera);
		
		Game.renderer.proccessEntity(dataObject.getPlayer());
	}

	public void loop() {

		
		Timer.begin();

		updateLogic();
		
		render();
		
		checkEnd();
		try {
			Thread.sleep((long) (Timer.end() * 1000)); // bad
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private void render() {
		for (Light light : dataObject.getLights()) {
			Game.renderer.proccessEntity(light);
		}

		Game.renderer.render(dataObject.getLights(), camera);

		ParticleMaster.render(camera);
	}

	private void checkEnd() {
		// TODO
	}

}
