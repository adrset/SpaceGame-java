package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import entities.Camera3D;
import game.Game;
import input.MouseCursor;

public class MousePicker {

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Vector3f ray;
	
	private Camera3D camera;
	
	public MousePicker(Camera3D camera, Matrix4f projectionMatrix){
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		viewMatrix = Maths.createViewMatrix(camera);
	}
	
	public Vector3f getRay(){
		return ray;
	}
	
	public void update(){
		viewMatrix = Maths.createViewMatrix(camera);
		ray = calculateRay();
	}
	
	private Vector2f getGlCursor(float x, float y){
		return new Vector2f((x * 2)/ Game.width - 1, (y * 2)/ Game.height - 1);
	}
	
	private Vector3f calculateRay(){
		float mouseX = (float) MouseCursor.getPosX();
		float mouseY = (float) MouseCursor.getPosY();
		//convert to openGL coordinate system (-1,-1) : (1,1)
		Vector2f glCursorPos = getGlCursor(mouseX, mouseY);
		//-1f to make it point into the screen
		Vector4f vec = new Vector4f(glCursorPos.x, glCursorPos.y, -1f, 1f);
		vec = viewCoords(vec);
		Vector3f worldRay = toWorldCoords(vec);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords){
		Matrix4f invertedViewMatrix = new Matrix4f(viewMatrix);
		invertedViewMatrix.invert();
		Vector4f rayWorld = invertedViewMatrix.transform(eyeCoords);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
		
	}
	
	private Vector4f viewCoords(Vector4f vec){
		Matrix4f inversedProjection = new Matrix4f(projectionMatrix);
		inversedProjection.invert();
		Vector4f eyeCoords = inversedProjection.transform(vec);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
}
