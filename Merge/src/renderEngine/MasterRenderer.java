package renderEngine;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import celestial.DataObject;
import celestial.Light;
import entities.Camera3D;
import entities.Entity;
import game.Game;
import models.GameItem;
import models.Mesh;
import shaders.StaticShader;
import utils.Maths;

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
	private final Matrix4f transformationMatrix = new Matrix4f();

	private Matrix4f projectionMatrix;

	// main shader
	private StaticShader shader;

	// skybox shader - gets cube texture and produces sky
	private SkyboxRenderer skyboxRenderer;

	public MasterRenderer() {
		createProjectionMatrix();
		enableCulling();
		shader = new StaticShader();
		shader.loadProjectionMatrix(projectionMatrix);
	}

	public void setSkybox(String[] textures) {
		skyboxRenderer = new SkyboxRenderer(projectionMatrix, textures);
	}

	public static void enableCulling() {
		// two following lines make unseen vertices not to be rendered
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(DataObject dataObject, Camera3D camera) {
		prepare();
		
		List<Light> lights = dataObject.getLights();
		// use this shader
		shader.start();
		// load data into shader
		shader.loadLights(lights);
		// send updated view matrixs
		shader.loadViewMatrix(camera);
		shader.loadProjectionMatrix(projectionMatrix);
		
		for (GameItem g : dataObject.getGameItems()) {
			transformationMatrix.set(Maths.createTransformationMatrix(g.getPosition(), 0, 0, 0, g.getScale()));
			shader.loadTransformationMatrix(transformationMatrix);
			for (Mesh m : g.getMeshes()) {
				m.render();
			}
		}
		
		for (Entity e : dataObject.getEntities()) {
			transformationMatrix.set(Maths.createTransformationMatrix(e.getPosition(), 0, 0, 0, e.getScale()));
			shader.loadTransformationMatrix(transformationMatrix);
			for (Mesh m : e.getMeshes()) {
				m.render();
			}
		}

		shader.stop();
		
		if(skyboxRenderer != null)
			skyboxRenderer.render(camera);

	}


	public void prepare() {
		
		// tell glfw to check for events
		glfwPollEvents();
		
		// enable inbuilt depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// set the background colour
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		
		// clear buffers
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Game.width / (float) Game.height;
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
