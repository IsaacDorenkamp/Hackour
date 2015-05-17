package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public class BlockWire extends PowerableObject{
	public BlockWire(int x, int y){
		super(x,y,PhysicalObject.TYPE_STATIC);
	}
	@Override
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		
		gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		
		Color use = PowerableObject.COPPER ;
		if( powered ) use = Color.YELLOW;
		gfx.setColor(use);
		gfx.fillRect( ( x * HackourGame.UNIT_SIZE ) + 5, ( y * HackourGame.UNIT_SIZE ) + 5, 5, 5 );
		gfx.setColor( old );
	}
	@Override
	public void interact(){}
	@Override
	public void onCollision( PhysicalObject other, int d ){}
}