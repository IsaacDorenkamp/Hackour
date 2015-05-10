package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public class Switch extends PowerableObject{
	public void onCollision(PhysicalObject other, int d){}
	
	public Switch( int x, int y ){
		super(x,y);
	}
	
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		
		if( powered ) gfx.setColor( Color.GREEN );
		else gfx.setColor( Color.RED );
		
		gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		
		gfx.setColor( old );
	}
	public void interact(){
		powered = !powered;
	}
}