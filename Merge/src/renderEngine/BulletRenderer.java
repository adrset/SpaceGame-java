package renderEngine;

import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import utils.Maths;
import weaponry.Bullet;

/**
 * BulletRenderer class. Takes care of rendering bullets.
 *
 * @author Adrian Setniewski
 *
 */

public class BulletRenderer {
	private StaticShader shader;

	public BulletRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<Bullet> bullets) {

		prepareTexturedModel(Bullet.bulletModel);
		for (Bullet b : bullets) {
			prepareInstance(b);
			GL11.glDrawElements(GL11.GL_TRIANGLES, Bullet.bulletModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		unbindTexturedModel();

	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();

		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		ModelTexture texture = model.getModelTexture();

		// disable culling for transparent objects
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}

		// some objects look messy when they're ruled by light. Making these
		// object light-proof helps!
		shader.loadFakeLight(texture.isFakeLight());
		// load texture info to shader
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflect());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());

	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Bullet b) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(b.getPosition(), b.getRotation().x,
				b.getRotation().y, b.getRotation().z, Bullet.scale);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
