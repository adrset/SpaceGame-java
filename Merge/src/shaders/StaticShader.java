package shaders;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import celestial.Light;
import entities.Camera3D;
import utils.Maths;

/**
 * StaticShader class. Responsible for loading data do glsl program.
 *
 * @author Adrian Setniewski
 *
 */

public class StaticShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 3;
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLight;
	
	private static final String VERTEX_FILE = "/shaders/mainShader.vert";
	private static final String FRAGMENT_FILE = "/shaders/mainShader.frag";
	public StaticShader(){
		super(VERTEX_FILE, FRAGMENT_FILE);
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");//bind variable from shader
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	 protected void getAllUniformLocations() {
	  location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	  location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	  location_viewMatrix = super.getUniformLocation("viewMatrix");
	  location_shineDamper = super.getUniformLocation("shineDamper");
	  location_reflectivity = super.getUniformLocation("reflectivity");
	  location_useFakeLight = super.getUniformLocation("useFakeLight");

	  location_lightPosition = new int[MAX_LIGHTS];
	  location_lightColor = new int[MAX_LIGHTS];
	  location_attenuation = new int[MAX_LIGHTS];
	  
	  for(int i = 0; i<MAX_LIGHTS;i++){
		  location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
		  location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
		  location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
	  }
	
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
        
    }
	
	public void loadFakeLight(boolean use){
		super.loadBoolean(location_useFakeLight, use);
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	public void loadLights(List<Light> lights){
		 for(int i = 0; i<MAX_LIGHTS;i++){
		 
			 if(i<lights.size()){
				 super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				 super.loadVector(location_lightColor[i], lights.get(i).getColor());
				 super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			 }else{
				 super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				 super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				 super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			 }
			 
		 }

	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera3D camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
