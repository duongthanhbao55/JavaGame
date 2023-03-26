package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {
	
	private JFrame jframe;
	public GameWindow(GamePanel gamePanel)
	{
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);		
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);//center of deskop
		jframe.setVisible(true);//it should be at the bottom
		jframe.addWindowFocusListener(new WindowFocusListener() {//do when change application
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().WindowFocusLost();
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}