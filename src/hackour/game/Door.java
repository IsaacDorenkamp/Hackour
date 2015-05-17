package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public class Door extends PowerableObject{
	public Door( int x, int y ){
		super( x, y );
	}
	
	@Override
	public void doTick( Game g ){
		super.doTick(g);
		if( powered ) type = PhysicalObject.TYPE_BACKGROUND;
		else type = PhysicalObject.TYPE_STATIC;
	}
	@Override
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		Color use = Color.DARK_GRAY;
		if( type == PhysicalObject.TYPE_BACKGROUND ) use = Color.GRAY;
		gfx.setColor( use );
		
		gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		
		gfx.setColor( old );
	}
	public void interact(){}
	public void onCollision(PhysicalObject other, int d ){}
}