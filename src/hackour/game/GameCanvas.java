package hackour.game;

import javax.swing.JComponent;
import java.awt.Graphics;

public abstract class GameCanvas extends JComponent{
	@Override
	public void paintComponent(Graphics g){
		CanvasPaint( g );
	};
	public abstract void CanvasPaint(Graphics g);
}