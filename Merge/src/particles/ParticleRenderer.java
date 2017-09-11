package particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import entities.Camera;
import entities.Camera3D;
import models.RawModel;
import renderEngine.Loader;
import utils.Maths;

public class ParticleRenderer {

	private static final int BUFFER_INSTANCES = 20000;
	private static final int FLOATS_PER_INSTANCE = 21;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(BUFFER_INSTANCES * FLOATS_PER_INSTANCE);
	
	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };

	private Loader loader;
	private RawModel quad;
	private ParticleShader shader;
	
	private int index = 0;
	private int vbo;

	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		this.vbo = loader.createInstanceVBO(FLOATS_PER_INSTANCE * BUFFER_INSTANCES);
		this.loader = loader;
		quad = loader.loadToVAO(VERTICES, 2);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 1, 4, FLOATS_PER_INSTANCE, 0);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 2, 4, FLOATS_PER_INSTANCE, 4);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 3, 4, FLOATS_PER_INSTANCE, 8);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 4, 4, FLOATS_PER_INSTANCE, 12);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 5, 4, FLOATS_PER_INSTANCE, 16);
		loader.addInstanceAttribute(quad.getVaoID(), vbo, 6, 1, FLOATS_PER_INSTANCE, 20);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	protected void render(Map<ParticleTexture, List<Particle>> particles, Camera3D camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		
		for (ParticleTexture tex : particles.keySet()) {
			bindTexture(tex);
			
			List<Particle> pList = particles.get(tex);
			index = 0;
			
			float[] vboData = new float[pList.size() * FLOATS_PER_INSTANCE];
			for (Particle p : pList) {
				
				updateViewModelMatrix(p.getPosition(), p.getRotation(), p.getScale(), viewMatrix, vboData);
				updateTexCoords(p, vboData);
			}
			loader.updateVBO(vbo, vboData, buffer);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), pList.size());
		}
		
		finishRendering();
	}
	
	private void bindTexture(ParticleTexture tex) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0); // Activate texture unit 0
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
		
		shader.loadNumberOfRows(tex.getRows());
	}

	protected void cleanUp() {
		shader.cleanUp();
	}

	private void updateViewModelMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(position);

		modelMatrix.m00(viewMatrix.m00());
		modelMatrix.m01(viewMatrix.m10());
		modelMatrix.m02(viewMatrix.m20());
		modelMatrix.m10(viewMatrix.m01());
		modelMatrix.m11(viewMatrix.m11());
		modelMatrix.m12(viewMatrix.m21());
		modelMatrix.m20(viewMatrix.m02());
		modelMatrix.m21(viewMatrix.m12());
		modelMatrix.m22(viewMatrix.m22());

		modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));

		modelMatrix.scale(scale);

		Matrix4f mv = new Matrix4f(viewMatrix);
		mv.mul(modelMatrix);
		storeMatrixData(mv, vboData);
		

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
	
	private void updateTexCoords(Particle p, float[] data) {
		data[index++] = p.getTexOffset1().x;
		data[index++] = p.getTexOffset1().y;
		data[index++] = p.getTexOffset2().x;
		data[index++] = p.getTexOffset2().y;
		data[index++] = p.getBlend();
		
	}

	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(false);
	}

	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
