package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Statemethods {// class we made must have all methods in this interface when it implements this interface

	public void update(long currTime);

	public void render(Graphics g);

	public void mouseClicked(MouseEvent e);

	public void mousePressed(MouseEvent e);

	public void mouseReleased(MouseEvent e);

	public void mouseMoved(MouseEvent e);

	public void keyPressed(KeyEvent e);

	public void keyReleased(KeyEvent e);
}
