package entities;
 
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
 
import celestial.CelestialBody;
import gui.Menu;
import input.Keyboard;
import models.TexturedModel;
import scenes.Scene;
import utils.Timer;
import weaponry.Weapon;

/**
 * Player class. Currently there's only one player. This class handles player movement.
 *
 * @author Adrian Setniewski
 *
 */
 
public class Player extends CelestialBody {
 
    private float currentForce = 0;
    private float currentForceUp = 0;
    private float currentSpeedRotateSpeed = 0;
    float val = 0;
    float val2 = 0;
    private static final float RUN_SPEED = 5f;
    private static final float MAX_SPEED = 20f;
    private static final float ROTATE_SPEED = 30;
    private static boolean inertiaDampener = false;
    public boolean force = false;
    private int score = 0;
    private Weapon weapon;
    public Vector3f extraForce=new Vector3f();
   
    public Vector3f getExtraForce(){
        return extraForce;
    }
    
    public void addScore(int number){
    	this.score += number;
    }
    
    public int getScore(){
    	return this.score;
    }
   
    public boolean isInertiaDampenerOn(){
        return inertiaDampener;
    }
 
    public Weapon getWeapon() {
        return weapon;
    }
 
    public Vector3f getDirection() {
        float x = (float) (Math.sin(Math.toRadians(super.getRotationY()))
                * Math.cos(Math.toRadians(super.getRotationX())));
        float z = (float) (Math.cos(Math.toRadians(super.getRotationY()))
                * Math.cos(Math.toRadians(super.getRotationX())));
        float y = (float) (Math.sin(Math.toRadians(super.getRotationX())));
        return new Vector3f(x, y, z);
    }
 
    public Player(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ,
            Vector3f velocity, float speed, float radius, float density) {
        super(model, position, rotationX, rotationY, rotationZ, velocity, radius, density, false, 10000);
        //this.currentSpeed = speed;
        this.currentForce = 0f;
        extraForce=new Vector3f(0f,0f,0f);
    }
 
    public float getSpeed() {
        return currentForce;
    }
 
    public void setWeapon(Weapon w) {
        weapon = w;
    }
 
    public void move() {
 
    	if(health <=0) setDead();
        checkInput();
        super.increaseRotation(0, (float) (currentSpeedRotateSpeed * Timer.getLastLoopTime()), 0);
        float Fx = (float) (currentForce * Math.sin(Math.toRadians(super.getRotationY()))
                * Math.cos(Math.toRadians(super.getRotationX())));
        float Fz = (float) (currentForce * Math.cos(Math.toRadians(super.getRotationY()))
                * Math.cos(Math.toRadians(super.getRotationX())));
        float Fy = (float) (currentForceUp + currentForce * Math.sin(Math.toRadians(super.getRotationX())));
        extraForce=new Vector3f(Fx,Fy,Fz);
        //extraForce2=new Vector3f(Vx,Vy,Vz);
        //super.setVelocity(Vx, Vy, Vz);
        //super.increasePosition(Vx, Vy, Vz, Timer.getLastLoopTime());
 
    }
 
    public void checkInput() {
        if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_Z)) {
            inertiaDampener = inertiaDampener ? false : true;
 
        }
        float multi = 0, multi2 = 0;
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            if (this.currentForce < MAX_SPEED) {
                multi += 0.0002f;
                this.currentForce += Math.exp((multi-10) / RUN_SPEED);
            }
 
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_U)) {
            if (this.currentForce < MAX_SPEED) {
                multi += 0.0002f;
                this.currentForce += Math.exp((multi-10) / RUN_SPEED);
            }
 
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
            multi2 += 0.0002f;
            this.currentForce -= Math.exp((multi2-10) / RUN_SPEED);
        } else {
            if (!inertiaDampener) {
                if (currentForce > 3) {
                    multi2 += 0.0002f;
                    this.currentForce -= Math.exp((multi2-10) / RUN_SPEED);
                } else if (currentForce < -3) {
                    multi2 += 0.0002f;
                    this.currentForce += Math.exp((multi2-10) / RUN_SPEED);
                } else {
                    this.currentForce = 0;
                }
            }
 
        }
 
        float multi1 = 0;
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            if (this.currentForceUp < MAX_SPEED / 3) {
                multi1 += 0.0002f;
                this.currentForceUp += Math.exp((multi1-10) / (RUN_SPEED * 2));
            }
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_C)) {
            if (this.currentForceUp < MAX_SPEED / 3) {
                if (-this.currentForceUp < MAX_SPEED) {
                    multi1 -= 0.0002f;
                    this.currentForceUp -= Math.exp((multi1-10) / (RUN_SPEED * 2));
                }
 
            }
        } else {
            if (!inertiaDampener) {
                if (currentForceUp > 3) {
                    this.currentForceUp -= Math.exp(0.02 / RUN_SPEED);
                } else if (currentForceUp < -3) {
                    this.currentForceUp += Math.exp(0.02 / RUN_SPEED);
                } else {
                    this.currentForceUp = 0;
                }
            }
        }
 
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_Q)) {
            this.currentSpeedRotateSpeed = ROTATE_SPEED;
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_E)) {
            this.currentSpeedRotateSpeed = -ROTATE_SPEED;
        } else {
            this.currentSpeedRotateSpeed = 0;
        }
 
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_L)) {
            super.increaseRotation(-0.7f, 0, 0);
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_O)) {
            super.increaseRotation(0.7f, 0, 0);
        }
 
        if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
            Menu.play = false;
        }
 
        if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_F3)) {
            Scene.isUiVisible = Scene.isUiVisible ? false : true;
        }
 
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_T)) {
            val += 0.001f;
            getModel().getModelTexture().setShineDamper(val);
            System.out.println("shine : " + val);
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_G)) {
            val -= 0.001f;
            getModel().getModelTexture().setShineDamper(val);
            System.out.println("shine : " + val);
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_Y)) {
            val2 += 0.001f;
            getModel().getModelTexture().setReflect(val2);
            System.out.println("reflect : " + val2);
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_H)) {
            val2 -= 0.001f;
            getModel().getModelTexture().setReflect(val2);
            System.out.println("reflect : " + val2);
        }
 
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_M)) {
            this.currentForce = 50;
            super.setVelocity(0f, 0f, 0f);
        }
 
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_F)) {
            weapon.fire(getDirection(), new Vector3f(getPosition()), getVelocity(), new Vector3f(getRotationX(), getRotationY() - 90, getRotationZ()));
        }
        if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_R)) {
            weapon.reload();
        }
    }
 
}