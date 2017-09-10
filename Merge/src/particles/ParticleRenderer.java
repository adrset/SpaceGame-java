package particles;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import entities.Camera3D;
import models.RawModel;
import renderEngine.Loader;
import utils.Maths;

public class ParticleRenderer {

	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };

	private RawModel quad;
	private ParticleShader shader;

	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		quad = loader.loadToVAO(VERTICES, 2);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	protected void render(Map<ParticleTexture, List<Particle>> particles, Camera3D camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();

		for (ParticleTexture tex : particles.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0); // Activate texture unit 0
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
			for (Particle p : particles.get(tex)) {
				updateViewModelMatrix(p.getPosition(), p.getRotation(), p.getScale(), viewMatrix);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		
		finishRendering();
	}

	protected void cleanUp() {
		shader.cleanUp();
	}

	private void updateViewModelMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
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

		shader.loadModelViewMatrix(mv);
	}

	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}

	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
