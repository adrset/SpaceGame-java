package scenes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

import audio.AudioManager;
import audio.AudioSource;
import celestial.Asteroid;
import celestial.DataObject;
import celestial.HostileShip;
import celestial.Light;
import celestial.Planet;
import entities.Camera3D;
import entities.Entity;
import entities.Player;
import fontMeshCreator.FontType;
import game.Game;
import gui.UI;
import language.Language;
import models.TexturedModel;
import renderEngine.ResourceCache;
import textures.ModelTexture;
import threads.CollisionDetector;
import threads.Force;
import utils.Logs;
import utils.MousePicker;
import utils.Timer;
import weaponry.Weapon;

public class Scene {

	// Data
	private DataObject dataObject;

	// Misc
	private Camera3D camera;
	private SceneLoader sceneLoader;
	private MousePicker picker;

	// Threads
	public CollisionDetector cDetect;
	private Force force;

	// UI
	private UI ui;
	private int mainLayerID;
	private FontType arial;
	private String currentLevel;
	public static boolean isUiVisible = true;

	// Finish booleans
	public static boolean isFinished;
	public static int isAboutEnd = 0;
	private float endTime = 0;

	// Audio
	private AudioSource source;

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

		List<Entity> allEntities = Collections.synchronizedList(new ArrayList<Entity>());
		List<Asteroid> allAsteroids = Collections.synchronizedList(new ArrayList<Asteroid>());
		List<HostileShip> allHostile = Collections.synchronizedList(new ArrayList<HostileShip>());

		Random generator = new Random();

		for (int i = 0; i < 100; i++) {
			allAsteroids.add(new Asteroid(
					new TexturedModel(ResourceCache.loadOBJ("planet", Game.loader),
							new ModelTexture(ResourceCache.loadTexture("2", Game.loader))),
					new Vector3f(-300000 + generator.nextInt(600000), -2060 + generator.nextInt(4120),
							-400000 + generator.nextInt(800000)),
					0, 0, 0, generator.nextInt(600), (float) generator.nextInt(30000000) * 1000));
		}

		for (int i = 0; i < 100; i++) {
			allEntities.add(new Entity(
					new TexturedModel(ResourceCache.loadOBJ("untitled", Game.loader),
							new ModelTexture(ResourceCache.loadTexture("2", Game.loader))),
					new Vector3f((float) (-50000 + generator.nextInt(100000)), 0,
							(float) (-50000 + generator.nextInt(100000))),
					0, 0, 0, 10f, new Vector3f()));
		}

		for (int i = 0; i < 20; i++) {
			allHostile.add(new HostileShip(
					new TexturedModel(ResourceCache.loadOBJ("shipMapped", Game.loader),
							new ModelTexture(ResourceCache.loadTexture("ship", Game.loader))),
					new Vector3f(dataObject.getPlayer().getPosition().x + generator.nextInt(30000), 0,
							dataObject.getPlayer().getPosition().y + generator.nextInt(30000)),
					0, 0, 0, 1000f, new Vector3f(), 800f + (float) generator.nextInt(500),
					generator.nextInt(2) + generator.nextFloat(), 1, (float) generator.nextInt(500)));
		}

		// Put lists above into dataObject
		dataObject.setAsteroids(allAsteroids);
		dataObject.setEntities(allEntities);
		dataObject.setHostileShips(allHostile);

		// Give player a weapon
		dataObject.getPlayer()
				.setWeapon(new Weapon("Gun", 12, dataObject, 120, 100,
						new TexturedModel(ResourceCache.loadOBJ("b2", Game.loader),
								new ModelTexture(ResourceCache.loadTexture("tex", Game.loader))),
						dataObject.getPlayer().getPosition(), 0.01f));

		// New threads
		cDetect = new CollisionDetector(dataObject);
		cDetect.start();
		force = new Force(dataObject);
		force.start();

		// Camera stuff
		camera = new Camera3D(dataObject.getPlayer());

		// Audio stuff
		AudioManager.setListenerData(dataObject.getPlayer().getPosition(), dataObject.getPlayer().getVelocity());
		int b = AudioManager.loadSound("res/audio/b.wav");
		source = new AudioSource(new Vector3f());
		source.play(b);

		picker = new MousePicker(camera, Game.renderer.getProjectionMatrix());
		dataObject.getPlayer().passPicker(picker);
	}

	public void init() {

		loadLevel(currentLevel);

		// font
		arial = new FontType(Game.loader.loadTexture("arial"), "arial");

		// menu / UI
		ui = new UI(Game.loader);

		// Layers
		mainLayerID = ui.addLayer(); // returns int if the id is needed

		createUserInterface();
	}

	public void justBeforeLoop() {

	}

	public void cleanUp() {
		ui.cleanUp();
		cDetect.finish();
		force.finish();
	}

	public void loop() {
		Game.renderer.setInstanceEntities(dataObject.getEntities());
		// needs to be repaired, cause it doesn't support vsync
		Timer.begin();
		for (HostileShip hostile : dataObject.getHostileShips()) {
			if (hostile.isAlive()) {
				hostile.chasePlayer(dataObject.getPlayer());
				Game.renderer.proccessEntity(hostile);
			} else {
				Logs.printLog("Removing HostileShip #" + dataObject.getHostileShips().indexOf(hostile));
				hostile.setDead();
			}
		}
		for (Iterator<HostileShip> it = dataObject.getHostileShips().iterator(); it.hasNext();) {

			if (!it.next().isAlive())
				it.remove();
		}
		Random generator = new Random();
		if (dataObject.getHostileShips().size() < 5) {
			System.out.println("Adding some enemies...");
			for (int i = 0; i < 10; i++) {
				dataObject.getHostileShips()
						.add(new HostileShip(
								new TexturedModel(ResourceCache.loadOBJ("shipMapped", Game.loader),
										new ModelTexture(ResourceCache.loadTexture("ship", Game.loader))),
								new Vector3f(dataObject.getPlayer().getPosition().x + generator.nextInt(5000),
										dataObject.getPlayer().getPosition().y
												+ generator.nextInt(5000),
										dataObject.getPlayer().getPosition().y + generator.nextInt(9000)),
								0, 0, 0, 1000f, new Vector3f(), 800f + (float) generator.nextInt(500),
								generator.nextInt(2) + generator.nextFloat(), 1, (float) generator.nextInt(500)));
			}
		}

		if (isAboutEnd == 0) {

			dataObject.getPlayer().move();
			dataObject.getPlayer().getWeapon().refreshBullets();

		}

		camera.move();
		picker.update();
		for (Planet planet : dataObject.getPlanets()) {
			planet.move();
			planet.rotateAround();
			Game.renderer.proccessEntity(planet.getEntity());
		}

		for (Asteroid asteroid : dataObject.getAsteroids()) {
			if (!asteroid.isAlive())
				continue;
			Game.renderer.proccessEntity(asteroid);
		}
		Game.renderer.proccessEntity(dataObject.getPlayer());

		for (Light light : dataObject.getLights()) {
			Game.renderer.proccessEntity(light);
		}
System.out.println("Health " + dataObject.getPlayer().getHealth());
		Game.renderer.render(dataObject.getLights(), camera);

		if (isUiVisible) {
			updateUI();
			ui.render();
		}
		checkEnd();
		try {
			Thread.sleep((long) (Timer.end() * 1000)); // bad
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void checkEnd() {
		if (isAboutEnd == 1) {
			ui.getLayer(mainLayerID).addText("You died", 3.0f, arial, new Vector2f(0.4f, 0.4f), 0.3f, false);
			ui.getLayer(mainLayerID).getGuiText(7).setColor(1, 0, 0);
			isAboutEnd = -1;
			source.stop();
			int b = AudioManager.loadSound("res/audio/gameover.wav");
			source.play(b);
			endTime = (float) Timer.getCurrentFakeTime();
		} else if (isAboutEnd == -1 && Timer.getCurrentFakeTime() - endTime > 5) {
			isFinished = true;
		}

	}

	// User interface methods
	private void createUserInterface() {
		ui.getLayer(mainLayerID).addText("", 1.0f, arial, new Vector2f(0, 0), 0.3f, false);
		ui.getLayer(mainLayerID).addText("", 1.0f, arial, new Vector2f(0, 0.96f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("", 1.0f, arial, new Vector2f(0f, 0.92f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("", 1.0f, arial, new Vector2f(0f, 0.88f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("", 1.0f, arial, new Vector2f(0.88f, 0.92f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("", 1.0f, arial, new Vector2f(0.90f, 0.0f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("", 1.4f, arial, new Vector2f(0.45f, 0.0f), 0.5f, false);
		ui.getLayer(mainLayerID).getGuiText(0).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(1).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(2).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(3).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(4).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(5).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(6).setColor(1, 0, 0);

		ui.getLayer(mainLayerID).addButton(Game.loader.loadTexture("GUI3"), Game.loader.loadTexture("GUI3"),
				new Vector2f(-0.82f, -0.87f), new Vector2f(0.18f, 0.13f), "", arial);

	}

	private void updateUI() {
		ui.getLayer(mainLayerID).getGuiText(0).changeText(new String(String.format("%.0f", Timer.getTrueFPS())));
		ui.getLayer(mainLayerID).getGuiText(1)
				.changeText(new String("[" + String.format("%.0f", dataObject.getPlayer().getVelocity().x) + ", "
						+ String.format("%.0f", dataObject.getPlayer().getVelocity().y) + ", "
						+ String.format("%.0f", dataObject.getPlayer().getVelocity().z) + "] m/s"));
		ui.getLayer(mainLayerID).getGuiText(2)
				.changeText(new String("[" + String.format("%.0f", dataObject.getPlayer().getPosition().x) + ", "
						+ String.format("%.0f", dataObject.getPlayer().getPosition().y) + ", "
						+ String.format("%.0f", dataObject.getPlayer().getPosition().z) + "] m"));
		ui.getLayer(mainLayerID).getGuiText(3).changeText(
				dataObject.getPlayer().isInertiaDampenerOn() ?Language.getLanguageData("ui_dampeners") + ": OFF" : Language.getLanguageData("ui_dampeners") + ": ON");
		ui.getLayer(mainLayerID).getGuiText(4)
				.changeText(Language.getLanguageData("ui_bullets") + ": " + (dataObject.getPlayer().getWeapon().getCurrentAmmunition()) + "/"
						+ (dataObject.getPlayer().getWeapon().getStartAmmo()));
		ui.getLayer(mainLayerID).getGuiText(5).changeText(Language.getLanguageData("ui_score") + ": " + Player.score);
		ui.getLayer(mainLayerID).getGuiText(6).changeText(Language.getLanguageData("ui_health") + ": " + dataObject.getPlayer().getHealth());
	}

}
