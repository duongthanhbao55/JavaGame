package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyBoardInputs;
import inputs.MouseInputs;

import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;



public class GamePanel extends JPanel{
	
	private MouseInputs mouseInputs;
	private Game game;
	public GamePanel(Game game)
	{
		this.game = game;
		mouseInputs = new MouseInputs(this);
		
		setPanelSize();
		addKeyListener(new KeyBoardInputs(this));
		addMouseListener(new MouseInputs(this));
		addMouseMotionListener(mouseInputs); 
	}
	
	
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
		
		setPreferredSize(size);

	}
	
	public void updateGame()
	{
		
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		game.render(g);
	}
	
	public Game getGame()
	{
		return game;
	}
}
