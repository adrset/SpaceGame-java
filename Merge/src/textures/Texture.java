package textures;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;;
public class Texture {

	private int textureID;
	public int getID() {
		return textureID;
	}

	public int getTextureWidth() {
		return textureWidth;
	}

	public int getTextureHeight() {
		return textureHeight;
	}

	private int textureWidth;
	private int textureHeight;
	
	
	public void bindTexture(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
	}
	
	public Texture(String fileName){
		textureID = GL11.glGenTextures();
		loadTexture(fileName);
	}
	
	private void uploadTextureData(ByteBuffer pixelData){
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelData);
	}
	private void setTextureParameter(int name, int state) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, name, state);
	}
	
	private int loadTexture(String filePath){
		
		try(InputStream in = Class.class.getResourceAsStream("/res/textures/" + filePath + ".png")){
			//Create the PNGDecoder object and decode the texture to a buffer
		    PNGDecoder decoder = new PNGDecoder(in);
		    textureWidth = decoder.getWidth();
		    textureHeight = decoder.getHeight();
		    
		    ByteBuffer pixelData = BufferUtils.createByteBuffer(4 * textureWidth * textureHeight);
		    decoder.decode(pixelData, 4*textureWidth, Format.RGBA);
		    pixelData.flip();
		    //Generate and bind the texture
		    bindTexture();
		    //Upload the buffer's content to the VRAM
		    uploadTextureData(pixelData);
		    //Apply filters
		    setTextureParameter(GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
		    setTextureParameter(GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		    //Important!! without lines underneath model looks like shit
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		    //Unbind texture
		    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		    return 1;
		}catch(IOException e){
		    e.printStackTrace();
		    return 0;
		}
	}
}

