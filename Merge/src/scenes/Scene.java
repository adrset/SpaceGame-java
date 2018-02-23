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
import models.GameItem;
import models.Mesh;
import models.MeshLoader;
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

	private Vector3f tempLen = new Vector3f();
	private float lenSq = 0f;

	// Misc
	private Camera3D camera;
	private String currentLevel;

	private float timeElapsed = 0f;
	private float timeChanged = 0f;
	ParticleSystem system;

	private int numCollisions = 0;
	// Finish booleans
	public static boolean isFinished;
	public static int isAboutEnd = 0;

	public Scene (String level, DataObject dataObject) {
		this.dataObject = dataObject;
		isFinished = false;
		currentLevel = level;
		init();
	}

	private void loadLevel(String level) {

		SceneLoader.load(level, dataObject);// loads planets, lights and player
		
		// TODO: to much dirty static calls
		Game.renderer.setSkybox(SceneLoader.getSkyboxTextureNames());
		// Camera stuff
		camera = new Camera3D(dataObject.getPlayer());

		ParticleTexture tex = new ParticleTexture(ResourceCache.loadTexture("smoky", Game.loader), 4);
		system = new ParticleSystem(tex, 300, 20, 1f, 4, 4); // max 10000 instances!!
		//Random generator = new Random();
		TexturedModel model = new TexturedModel(ResourceCache.loadOBJ("untitled", Game.loader),
				new ModelTexture(ResourceCache.loadTexture("2", Game.loader)));

		List<Entity> allEntities = new ArrayList<Entity>();

		for (int i = 0; i < 100; i++) {
		
					allEntities.add(new Entity(model,
							new Vector3f((float) i * 0.5f), 0, 0, 0, 1f, null));
		

		}

		dataObject.setEntities(allEntities);
		
		
		List<GameItem> gameItems = new ArrayList<GameItem>();
		
		dataObject.setGameItems(new ArrayList<GameItem>());
		
		try {
			dataObject.getGameItems().add(new GameItem(MeshLoader.load("maya")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
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

		for (int i = 0; i < dataObject.getEntities().size(); i++) {
			if (dataObject.getEntities().get(i).checkCollision(dataObject.getPlayer())) {
				dataObject.getEntities().remove(i);
			}

		}

		//Game.renderer.proccessEntity(dataObject.getPlayer());
		
		if (timeElapsed > Math.pow(10, 9)) {
			timeChanged = (float) (System.nanoTime());
			timeElapsed = 0f;
		} else {
			timeElapsed = (float) (System.nanoTime() - timeChanged);
		}
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
		/*for (Light light : dataObject.getLights()) {
			Game.renderer.proccessEntity(light);
		}*/

		Game.renderer.render(dataObject, camera);

		ParticleMaster.render(camera);
	}

	private void checkEnd() {
		if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
			Game.windowShouldClose = true;
		}
	}

}
