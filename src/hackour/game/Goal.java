package hackour.game;

import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Color;

public class Goal extends PowerableObject{
	private HackourGame hg;
	public Goal( int x, int y, HackourGame hg ){
		super( x, y );
		this.hg = hg;
	}
	public Goal( int x, int y ){
		this( x, y, HackourGame.RUNNING_HOST );
	}
	@Override
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		
		Color use = Color.BLUE ;
		if( powered ) use = Color.CYAN;
		gfx.setColor(use);
		gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		
		gfx.setColor( old );
	}
	@Override
	public void interact(){
		if( powered ){
			JOptionPane.showMessageDialog(null, "You win.");
			hg.reset();
		}
	}
	@Override
	public void onCollision( PhysicalObject po, int d ){}
}