package hackour.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;

public class XORGate extends ElectricGate{
	public XORGate( int x, int y ){
		super( x, y, "XOR.png" );
	}
	
	@Override
	public void paint( Graphics gfx ){
		if( image != null ){
			gfx.drawImage(image, x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, null);
		}else{
			Color old = gfx.getColor();
			gfx.setColor( Color.BLUE );
			
			gfx.fillRect( x * HackourGame.UNIT_SIZE, y * HackourGame.UNIT_SIZE, width, height );
			gfx.setColor( old );
		}
	}
	@Override
	public void doTick( Game g ){
		CheckInputs();
		out = !(in1 && in2) && ( in1 || in2 );
	}
}