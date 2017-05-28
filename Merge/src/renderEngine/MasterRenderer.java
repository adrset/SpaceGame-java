package renderEngine;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import celestial.Light;
import entities.Camera;
import entities.Camera3D;
import entities.Entity;
import game.Game;
import models.TexturedModel;
import scenes.SceneLoader;
import shaders.StaticShader;
import terrain.Terrain;
import terrain.TerrainShader;

public class MasterRenderer {

	private static final float FOV = 60; // I see a great potential here :D
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 900000000f; // sorry :(
	private static final float RED = 0.2f;
	private static final float GREEN = 0.2f;
	private static final float BLUE = 0.5f;
	private Matrix4f projectionMatrix;

	private boolean instanceRendererPrepared = false;

	// main shader
	private StaticShader shader = new StaticShader();

	// terrain shader - currently not used
	private TerrainShader terrainShader = new TerrainShader();

	// skybox shader - gets cube texture and produces sky (or stars)
	private SkyboxRenderer skyboxRenderer;

	// renderer for all entities
	private EntityRenderer renderer;

	private InstanceRenderer instanceRenderer;

	// renderer for terrains
	private TerrainRenderer terrainRenderer;

	// Map of all entities of all kindMap<TexturedModel, List<Entity>> entities
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(Loader loader, SceneLoader sceneLoader) {
		createProjectionMatrix();
		enableCulling();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix, sceneLoader.getSkyboxTextureNames());
		instanceRenderer = new InstanceRenderer(loader, projectionMatrix);
	}

	public static void enableCulling() {
		// two following lines make unseen vertices not to be rendered
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<Light> lights, Camera3D camera) {
		prepare();

		shader.start();
		// load data into shader
		shader.loadLights(lights);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();

		instanceRenderer.render(camera, lights);

		terrainShader.start();
		// load data into shader
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		skyboxRenderer.render(camera);

	}

	public void render(Camera camera) {
		prepare();
	}

	public void proccessTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void proccessEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		// List <Entity> fellowEntites = entities.get(entityModel);
		if (entities.get(entityModel) != null) {
			System.out.println("add");
			// fellowEntites.add(entity); if the upper comment is a reference
			// then it was ok
			entities.get(entityModel).add(entity);
		} else {
			List<Entity> newFellowEntites = new ArrayList<Entity>();
			newFellowEntites.add(entity);
			entities.put(entityModel, newFellowEntites);
		}
	}

	public void setInstanceEntities(List<Entity> entities) {
		if (!instanceRendererPrepared) {
			instanceRenderer.setEntityList(entities);
			instanceRendererPrepared = true;
		}
	}

	public void prepare() {
		glfwPollEvents();
		// defines which vertices are on top and only these will be shown
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// disable this if you are high
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// isn't needed whilst entire screen is used as render target. Let it
		// be!
		GL11.glClearColor(RED, GREEN, BLUE, 1.0f);

	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	private void createProjectionMatrix() {
		// projection matrix - weird but it works - thanks wikipedia
		float aspectRatio = Game.width / Game.height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix.m33(0);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
