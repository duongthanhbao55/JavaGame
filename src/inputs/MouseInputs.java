package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener{
	private GamePanel gamePanel;
	
	
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch(Gamestate.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseDragged(e);			
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseDragged(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(Gamestate.state) {
		case SETNAME:
			gamePanel.getGame().getSetNamePlayer().mouseMoved(e);
			break;
		case REGISTER:
			gamePanel.getGame().getRegister().mouseMoved(e);
			break;
		case LOGIN:
			gamePanel.getGame().getLogin().mouseMoved(e);
			break;
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseMoved(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch(Gamestate.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseClicked(e);			
			break;
		
		default:
			break;
		
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(Gamestate.state) {
		case SETNAME:
			gamePanel.getGame().getSetNamePlayer().mousePressed(e);
			break;
		case REGISTER:
			gamePanel.getGame().getRegister().mousePressed(e);
			break;
		case LOGIN:
			gamePanel.getGame().getLogin().mousePressed(e);
			break;
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);		
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mousePressed(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(Gamestate.state) {
		case SETNAME:
			gamePanel.getGame().getSetNamePlayer().mouseReleased(e);
			break;
		case REGISTER:
			gamePanel.getGame().getRegister().mouseReleased(e);
			break;
		case LOGIN:
			gamePanel.getGame().getLogin().mouseReleased(e);
			break;
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseReleased(e);			
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseReleased(e);
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
