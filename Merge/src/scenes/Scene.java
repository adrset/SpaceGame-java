package scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

import audio.AudioManager;
import audio.AudioSource;
import celestial.Asteroid;
import celestial.CollisionDetector;
import celestial.Force;
import celestial.HostileShip;
import celestial.Light;
import celestial.Planet;
import entities.Camera3D;
import entities.Entity;
import entities.Player;
import fontMeshCreator.FontType;
import game.Game;
import gui.UI;
import models.TexturedModel;
import renderEngine.ResourceCache;
import textures.ModelTexture;
import utils.MousePicker;
import utils.Timer;
import weaponry.Weapon;

public class Scene {

	private UI ui;
	private List<Light> lights;
	private Player player;
	private Camera3D camera;
	private List<Asteroid> allAsteroids;
	private List<Planet> allPlanets;
	private List<HostileShip> allHostile;
	private SceneLoader sceneLoader;
	private List<Entity> allEntities;
	public CollisionDetector cDetect;
	private int mainLayerID;
	private Force force;
	private String currentLevel;
	private FontType arial;
	MousePicker picker;
	public static boolean isFinished;
	public static boolean isUiVisible = true;
	public static int isAboutEnd = 0;
	private float endTime = 0;
	private AudioSource source;
	
	public Scene(SceneLoader sceneLoader, String level) {
		this.sceneLoader = sceneLoader;
		isFinished = false;
		allAsteroids = new ArrayList<Asteroid>();
		allEntities = new ArrayList<Entity>();
		allHostile = new  ArrayList<HostileShip>();
		currentLevel = level;
		init();

	}

	private void loadLevel(String level) {
		sceneLoader.load(level);

		allPlanets = sceneLoader.getPlanets();

		lights = sceneLoader.getLights();

		player = sceneLoader.getPlayer();
		player.setWeapon(new Weapon("Gun", 12, allHostile, 120, 100, new TexturedModel(ResourceCache.loadOBJ("b2", Game.loader),
				new ModelTexture(ResourceCache.loadTexture("tex", Game.loader))), player.getPosition(), 0.01f));
		Game.renderer.setPlayer(player);

		Random generator = new Random();
		for (int i = 0; i < 100; i++) {

			allAsteroids.add(new Asteroid(
					new TexturedModel(ResourceCache.loadOBJ("planet", Game.loader),
							new ModelTexture(ResourceCache.loadTexture("2", Game.loader))),
					new Vector3f(-300000 + generator.nextInt(600000), -2060 + generator.nextInt(4120),
							-400000 + generator.nextInt(800000)),
					0, 0, 0, generator.nextInt(600), (float) generator.nextInt(30000000) * 1000));
		}
		
		for(int i=0;i<1000;i++){
			
			allEntities.add(new Entity(new TexturedModel(ResourceCache.loadOBJ("untitled", Game.loader),
					new ModelTexture(ResourceCache.loadTexture("2", Game.loader))),
			new Vector3f((float) (-50000+ generator.nextInt(100000)),0,(float)(-50000+ generator.nextInt(100000))),
			0, 0, 0, 10f, new Vector3f()));
			
		}
		
		for(int i=0;i<20;i++){// float health, float speedMultiplier, int fireRate, float cash
			allHostile.add(new HostileShip(new TexturedModel(ResourceCache.loadOBJ("planet", Game.loader),
					new ModelTexture(ResourceCache.loadTexture("2", Game.loader))),
			new Vector3f(player.getPosition().x +  generator.nextInt(30000), 0, player.getPosition().y + generator.nextInt(30000)),
			0, 0, 0, 10f, new Vector3f(), 800f + (float)generator.nextInt(500), generator.nextInt(2) + generator.nextFloat(), 1, (float)generator.nextInt(500)));
		}	

		cDetect = new CollisionDetector(allPlanets, allAsteroids, lights, allHostile, player);
		cDetect.start();
		force = new Force(allPlanets, allAsteroids, lights);
		force.start();
		camera = new Camera3D(player);
		AudioManager.setListenerData(player.getPosition(), player.getVelocity());
		
		int b = AudioManager.loadSound("res/audio/b.wav");
		source = new AudioSource(new Vector3f());
		source.play(b);
	}

	public void init() {

		loadLevel(currentLevel);

		// font
		arial = new FontType(Game.loader.loadTexture("arial"), "arial");

		// menu / UI
		ui = new UI(Game.loader);

		// Layers
		mainLayerID = ui.addLayer(); // returns int if the id is needed

		ui.getLayer(mainLayerID).addText("fps", 1.0f, arial, new Vector2f(0, 0), 0.3f, false);
		ui.getLayer(mainLayerID).addText("Speed", 1.0f, arial, new Vector2f(0, 0.96f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("Location", 1.0f, arial, new Vector2f(0f, 0.92f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("Inertia dampeners: OFF", 1.0f, arial, new Vector2f(0f, 0.88f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("Ammo: ", 1.0f, arial, new Vector2f(0.9f, 0.92f), 0.5f, false);
		ui.getLayer(mainLayerID).addText("Score:" , 1.0f, arial, new Vector2f(0.90f, 0.0f), 0.5f, false);
		ui.getLayer(mainLayerID).getGuiText(0).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(1).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(2).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(3).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(4).setColor(1, 1, 1);
		ui.getLayer(mainLayerID).getGuiText(5).setColor(1, 1, 1);
	
		ui.getLayer(mainLayerID).addButton(Game.loader.loadTexture("GUI3"), Game.loader.loadTexture("GUI3"),
				new Vector2f(-0.82f, -0.87f), new Vector2f(0.18f, 0.13f),"", arial);
		
	
	}
	
	
	public void justBeforeLoop(){
		picker = new MousePicker(camera, Game.renderer.getProjectionMatrix());
	}

	public void cleanUp() {
		ui.cleanUp();
		cDetect.finish();
	}

	public void loop() {
		Game.renderer.setInstanceEntities(allEntities);
		// needs to be repaired, cause it doesn't support vsync
		Timer.begin();
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (HostileShip hostile : allHostile) {
			if(hostile.isAlive()){
				hostile.chasePlayer(player);
				Game.renderer.proccessEntity(hostile);
			}else{
				System.out.println("removing");
				a.add(allHostile.indexOf(hostile));
			}
		}
		for(int b : a){
			allHostile.remove(b);
		}
		
		if (isAboutEnd == 0) {

			player.move();
			player.getWeapon().refreshBullets();
			
		}
		camera.move();
		picker.update();
		for (Planet planet : allPlanets) {
			planet.move();
			planet.rotateAround();
			Game.renderer.proccessEntity(planet.getEntity());
		}

		for (Asteroid asteroid : allAsteroids) {
			Game.renderer.proccessEntity(asteroid);
		}
		Game.renderer.proccessEntity(player);
		
		
		
		//Game.renderer.proccessInstancedEntity(allEntities);
		for (Light light : lights) {
			Game.renderer.proccessEntity(light);
		}

		Game.renderer.render(lights, camera);

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
			ui.getLayer(mainLayerID).getGuiText(6).setColor(1, 0, 0);
			isAboutEnd = -1;
			source.stop();
			int b = AudioManager.loadSound("res/audio/gameover.wav");
			source.play(b);
			endTime = (float) Timer.getCurrentFakeTime();
		} else if (isAboutEnd == -1 && Timer.getCurrentFakeTime() - endTime > 5) {
			isFinished = true;
		}

	}

	private void updateUI() {
		ui.getLayer(mainLayerID).getGuiText(0).changeText(new String(String.format("%.0f", Timer.getTrueFPS())));
		ui.getLayer(mainLayerID).getGuiText(1)
				.changeText(new String("[" + String.format("%.0f", player.getVelocity().x) + ", "
						+ String.format("%.0f", player.getVelocity().y) + ", "
						+ String.format("%.0f", player.getVelocity().z) + "] m/s"));
		ui.getLayer(mainLayerID).getGuiText(2)
				.changeText(new String("[" + String.format("%.0f", player.getPosition().x) + ", "
						+ String.format("%.0f", player.getPosition().y) + ", "
						+ String.format("%.0f", player.getPosition().z) + "] m"));
		ui.getLayer(mainLayerID).getGuiText(3).changeText(player.isInertiaDampenerOn() ? "Inertia dampeners: OFF" :  "Inertia dampeners: ON");
		ui.getLayer(mainLayerID).getGuiText(4).changeText("Bullets: " + (player.getWeapon().getCurrentAmmunition()) + "/" + (player.getWeapon().getStartAmmo()));
		ui.getLayer(mainLayerID).getGuiText(5).changeText("Score: " + Player.score);
	}

}
