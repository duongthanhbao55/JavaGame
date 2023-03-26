package objects;
import static untilz.Constants.ObjectConstants.*;

import Effect.Animation;
import main.Game;

public class GameContainer extends GameObject{

	Animation Box, Berrel;
	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitBox();
	}

	private void createHitBox() {
		if(objType == BOX) {
			initHitbox(25,18);
			xDrawOffset = (int)(7 * Game.SCALE);
			yDrawOffset = (int)(12 * Game.SCALE); 
		}else {
			initHitbox(23,25);
			xDrawOffset = (int)(8 * Game.SCALE);
			yDrawOffset = (int)(5 * Game.SCALE);
		}
		
	}
	public void update() {
		
	}

}
