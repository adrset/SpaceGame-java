package renderEngine;

import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import celestial.Light;
import entities.Camera3D;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.InstanceShader;
import textures.ModelTexture;
import utils.Maths;

/**
 * InstanceRenderer class. Renders all entities that are meant to be rendered using instanced rendering.
 *
 * @author Adrian Setniewski
 *
 */

public class InstanceRenderer {
	private static final int BUFFER_INSTANCES = 10000;
	private static final int FLOATS_PER_INSTANCE = 16;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(BUFFER_INSTANCES * FLOATS_PER_INSTANCE);
	List<Entity> entities;
	private RawModel quad;
	public InstanceShader shader;
	private Loader loader;
	private int vbo;
	private int index = 0;

	public InstanceRenderer(Loader loader, Matrix4f projectionMatrix) {
		this.loader = loader;
		this.vbo = loader.createInstanceVBO(FLOATS_PER_INSTANCE * BUFFER_INSTANCES);

		shader = new InstanceShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void setEntityList(List<Entity> entities){
		this.entities = entities;
		quad = entities.get(0).getModel().getRawModel();
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 3, 4, FLOATS_PER_INSTANCE, 0);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 4, 4, FLOATS_PER_INSTANCE, 4);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 5, 4, FLOATS_PER_INSTANCE, 8);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 6, 4, FLOATS_PER_INSTANCE, 12);
	}

	public void render(Camera3D camera, List<Light> lights) {
		prepare();
		shader.loadViewMatrix(camera);
		shader.loadLights(lights);

		TexturedModel model = entities.get(0).getModel();
		bindTexture(model.getModelTexture());
		index = 0;
		float[] data = new float[entities.size() * FLOATS_PER_INSTANCE];
		for (Entity entity : entities) {
			updateTransformationMatrix(data, entity);
		}
		loader.updateVBO(vbo, data, buffer);
		GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, quad.getVertexCount(), GL11.GL_UNSIGNED_INT, 0,
				entities.size());

		end();
	}

	private void updateTransformationMatrix(float[] data, Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(),
				entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		storeMatrixData(transformationMatrix, data);
	}

	private void storeMatrixData(Matrix4f matrix, float[] data) {
		// column order
		data[index++] = matrix.m00();
		data[index++] = matrix.m01();
		data[index++] = matrix.m02();
		data[index++] = matrix.m03();
		data[index++] = matrix.m10();
		data[index++] = matrix.m11();
		data[index++] = matrix.m12();
		data[index++] = matrix.m13();
		data[index++] = matrix.m20();
		data[index++] = matrix.m21();
		data[index++] = matrix.m22();
		data[index++] = matrix.m23();
		data[index++] = matrix.m30();
		data[index++] = matrix.m31();
		data[index++] = matrix.m32();
		data[index++] = matrix.m33();
	}

	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		for (int i = 0; i < 7; i++)
			GL20.glEnableVertexAttribArray(i);

	}

	private void bindTexture(ModelTexture texture) {
		shader.loadFakeLight(texture.isFakeLight());
		// load texture info to shader
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflect());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}

	private void end() {
		MasterRenderer.enableCulling();
		for (int i = 0; i < 7; i++)
			GL20.glDisableVertexAttribArray(i);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
