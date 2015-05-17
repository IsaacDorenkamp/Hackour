package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public class Teleporter extends PowerableObject{
	private int dx = 0;
	private int dy = 0;
	public Teleporter( int x, int y, int dx, int dy ){
		super( x, y );
		this.dx = dx;
		this.dy = dy;
	}
	public Teleporter( int x, int y ){
		this( x, y, 0, 0 );
	}
	
	public void set_dx( int d ){
		dx = d;
	}
	public void set_dy( int d ){
		dy = d;
	}
	public int get_dx(){
		return dx;
	}
	public int get_dy(){
		return dy;
	}
	public void interact(){
		if( powered ){
			HackourGame.RUNNING_HOST.teleport( dx, dy );
		}
	}
	
	@Override
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		
		gfx.setColor( Color.BLUE );
		gfx.fillRect( ( x * HackourGame.UNIT_SIZE ), ( y * HackourGame.UNIT_SIZE ), width, height );
		
		Color use = Color.GRAY;
		if ( powered ) use = Color.WHITE;
		
		gfx.setColor( use );
		gfx.fillOval( ( x * HackourGame.UNIT_SIZE ), ( y * HackourGame.UNIT_SIZE ), width, height );
		gfx.setColor( old );
	}
	
	public void onCollision( PhysicalObject other, int d ){}
}