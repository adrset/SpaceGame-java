package audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

public class AudioSource
{
	private int sourceID;
	
	public void stop(){
		AL10.alSourceStop(sourceID);
	}
	
	public void pause(){
		AL10.alSourcePause(sourceID);
	}
	
	public void resume(){
		AL10.alSourcePlay(sourceID);
	}
	
	public void setPosition(Vector3f position){
		AL10.alSource3f(sourceID, AL10.AL_POSITION, position.x, position.y, position.z);
	}
	
	public void setVelocity(Vector3f velocity){
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	
	public void setVolume(float volume){
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}
	
	public void setLoopMode(boolean state){
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, state ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public AudioSource(Vector3f position)
	{
		sourceID= AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		setPosition(position);
	}

	public void play(final int buffer)
	{
		stop();
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	public void delete()
	{
		stop();
		AL10.alDeleteSources(sourceID);
	}
}
