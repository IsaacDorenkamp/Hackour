package hackour.game;

import java.awt.Graphics;
import java.awt.Color;
public class Clock extends PowerableObject{
	public static final int HERTZ = 50;
	
	private int counter = 0;
	private int max;
	public Clock( int x, int y, int tick_interval ){
		super( x, y );
		max = tick_interval;
	}
	public Clock( int x, int y ){
		this( x, y, Clock.HERTZ );
	}
	public void paint( Graphics gfx ){
		super.paint( gfx );
		Color old = gfx.getColor();
		gfx.setColor( Color.BLUE );
		gfx.drawRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		gfx.setColor( old );
	}
	@Override
	public void doTick(Game g){
		counter = ( counter + 1 ) % max;
		if( counter == 0 ) powered = !powered;
	}
	public void interact(){}
	public void onCollision( PhysicalObject other, int d ){}
}