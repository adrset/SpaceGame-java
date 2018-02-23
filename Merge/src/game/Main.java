package game;

/**
 * Main class.
 *
 * @author Adrian Setniewski
 *
 */

public class Main {

	public static void main(String[] args) {
		//that simple???
		if(args.length >=3){
			int width = Integer.parseInt(args[0]);
			int height = Integer.parseInt(args[1]);
			new Game("SpaceGame", width, height, Integer.parseInt(args[2]) == 1 ? true : false);
		}else if(args.length == 1){
			new Game("SpaceGame", 800, 600, Integer.parseInt(args[2]) == 1 ? true : false);
		}else{
			new Game("SpaceGame", 800, 600, false );
		}
		

	}

}
