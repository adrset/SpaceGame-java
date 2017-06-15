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
import entities.Player;
import game.Game;
import models.TexturedModel;
import scenes.SceneLoader;
import shaders.StaticShader;

/**
 * MasterRenderer class. Takes control over every renderer in the game. Creates
 * projection matrix and passes them to other renderers.
 *
 * @author Adrian Setniewski
 *
 */

public class MasterRenderer {

	private static final float FOV = 60; // I see a great potential here :D
	private static final float NEAR_PLANE = 1f;
	private static final float FAR_PLANE = 100000000f; // sorry :(
	private Player player;

	private Loader loader;
	private Matrix4f projectionMatrix;

	private boolean instanceRendererPrepared = false;

	// main shader
	private StaticShader shader = new StaticShader();

	// skybox shader - gets cube texture and produces sky (or stars)
	private SkyboxRenderer skyboxRenderer;

	// renderer for all entities
	private EntityRenderer renderer;

	private BulletRenderer bulletRenderer;

	private InstanceRenderer instanceRenderer;

	// Map of all entities of all kindMap<TexturedModel, List<Entity>> entities
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	public MasterRenderer(Loader loader) {
		this.loader = loader;
		createProjectionMatrix();
		enableCulling();
		renderer = new EntityRenderer(shader, projectionMatrix);
		bulletRenderer = new BulletRenderer(shader, projectionMatrix);
		instanceRenderer = new InstanceRenderer(loader, projectionMatrix);

	}

	public void setSkybox(SceneLoader sceneLoader) {
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix, sceneLoader.getSkyboxTextureNames());
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

		// use this shader
		shader.start();
		// load data into shader
		shader.loadLights(lights);
		// send updated view matrixs
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		// bulletRenderer shares shader with EntityRenderer
		bulletRenderer.render(player.getWeapon().getBullets());
		shader.stop();
		entities.clear();

		// InstanceRenderer has inbuilt shader
		instanceRenderer.render(camera, lights);
		// So has SkyboxRenderer
		skyboxRenderer.render(camera);

	}

	public void render(Camera camera) {
		prepare();
	}

	public void setPlayer(Player p) {
		this.player = p;
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

	}

	public void cleanUp() {
		shader.cleanUp();
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
