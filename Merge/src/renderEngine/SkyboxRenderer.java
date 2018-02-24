package renderEngine;

import org.joml.Matrix4f;

import entities.Camera3D;
import skybox.SkyboxShader;

/**
 * SkyboxRenderer class. Renders all entities that are not meant to be rendered using instanced rendering.
 *
 * @author ThinMatrix - Karl
 *
 */

public class SkyboxRenderer {

private static final float SIZE = 10000000f;
	
	//Just a box without indices
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,//1
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,//2
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,//3
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,//4
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,//5
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,//6
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private int texID;
	private SkyboxShader shader;
	
	public SkyboxRenderer(Matrix4f projMatrix, String[] textures){
		//model = loader.loadToVAO(VERTICES, 3);
		//texID = loader.loadCubeMap(textures);
		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(projMatrix);
		shader.stop();
	}
	
	public void render(Camera3D camera){
		/*shader.start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();*/
		
	}
	
}
