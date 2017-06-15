package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera3D;

/**
 * Maths class. Creates needed matrices. Their definitions can be found e.g. on wikipedia.
 *
 * @author Adrian Setniewski
 *
 */

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale ){
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rx), 1.0f, 0.0f, 0.0f);
		matrix.rotate((float) Math.toRadians(ry), 0.0f, 1.0f, 0.0f);
		matrix.rotate((float) Math.toRadians(rz), 0.0f, 0.0f, 1.0f);
		matrix.scale(scale);

		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(translation,0), matrix);
		matrix.scale(new Vector3f(scale.x, scale.y, 1f), matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera3D camera){
		  Matrix4f viewMatrix = new Matrix4f();
		  viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix);
		  viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix);
		  Vector3f cameraPos = camera.getPosition();
		  Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		  viewMatrix.translate(negativeCameraPos, viewMatrix);
		  return viewMatrix;
	}
	

}
