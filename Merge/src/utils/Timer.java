package utils;

import game.Game;

/**
 * Timer class. Helps maintaining FPS.
 *
 * @author Adrian Setniewski
 *
 */

public class Timer {
	private static double startTime;
	private static double currentTime;
	private static double lastLoopTime = 0.0166f;
	
	private static double currentFakeTime;
	private static double fps;
	private static double trueFps;
    private static double maxFPS;
    
    public static double getFps() {
		return fps;
	}
    public static double getTrueFPS(){
    	return trueFps;
    }
    
    public static double getCurrentFakeTime(){
    	return currentFakeTime;
    }
    
    public static double getCurrentTime(){
    	return System.nanoTime() / Math.pow(10, 9);
    }
    
    public static double getLastLoopTime() {
        return lastLoopTime;
    }
    
    public static void init(float targetFPS) {
		setMaxFPS(targetFPS);
		currentFakeTime = 0;
	}
    
    public static void setMaxFPS(float targetFPS){
    	maxFPS = targetFPS;
    }
	public static void begin() {
		startTime = getCurrentTime();
	}
	public static float end() {
		//calculateFPS();
		currentTime = getCurrentTime();
		double frameTime = currentTime - startTime;
		
		trueFps = 1/frameTime;
		if(!Game.getVideoSync()){
			if (frameTime < 1/maxFPS) {
				fps = maxFPS;
				lastLoopTime = 1/fps;
				currentFakeTime += lastLoopTime;
				return (float) (1/maxFPS - frameTime);
				
			}else{
				fps = 1/frameTime;
				lastLoopTime = frameTime;
				currentFakeTime += lastLoopTime;
				return 0;
			}
		}else{
			// tricky and not the way it should be xd
			fps = maxFPS;
			trueFps = 1/lastLoopTime;
			lastLoopTime = 1/fps;
			currentFakeTime += lastLoopTime;
			return 0;
		}
		
		
		
		
		
	}
	
}
