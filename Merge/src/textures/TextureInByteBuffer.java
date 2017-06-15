package textures;

import java.nio.ByteBuffer;

/**
 * TextureInByteBuffer class. Holds texture data in a byte buffer.
 *
 * @author ThinMatrix - Karl
 *
 */

public class TextureInByteBuffer {
	private int width;
	private int height;
	private ByteBuffer buffer;
	     
	public TextureInByteBuffer(ByteBuffer buffer, int width, int height){
	 	this.buffer = buffer;
	 	this.width = width;
	 	this.height = height;
	}
	     
	public int getWidth(){
		return width;
	}
	     
	public int getHeight(){
		return height;
	}
	     
	public ByteBuffer getBuffer(){
		return buffer;
	}
}
