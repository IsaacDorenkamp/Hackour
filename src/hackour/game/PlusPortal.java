package hackour.game;

import java.awt.Graphics;
import java.awt.Color;

public class PlusPortal extends PowerableObject{
	public PlusPortal( int x, int y ){
		super(x, y);
	}
	@Override
	public void interact(){
		if( powered ) HackourGame.RUNNING_HOST.PlusLayer();
	}
	@Override
	public void paint( Graphics gfx ){
		Color old = gfx.getColor();
		
		Color use;
		if( powered ) use = Color.CYAN;
		else use = Color.BLUE;
		gfx.setColor(use);
		gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
		gfx.setColor( Color.WHITE );
		gfx.drawLine( ( x * HackourGame.UNIT_SIZE ) , ( y * HackourGame.UNIT_SIZE ) + 7,( x * HackourGame.UNIT_SIZE ) + 14, ( y * HackourGame.UNIT_SIZE ) + 7 );
		gfx.drawLine( ( x * HackourGame.UNIT_SIZE ) + 7, ( y * HackourGame.UNIT_SIZE ), ( x * HackourGame.UNIT_SIZE ) + 7, ( y * HackourGame.UNIT_SIZE ) + 14 );
		
		gfx.setColor( old );
	}
	@Override
	public void onCollision( PhysicalObject other, int d ){}
}