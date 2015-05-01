package hackour.game;

import hackour.levels.*;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Insets;

import java.util.Iterator;
import java.util.ArrayList;

public class HackourGame extends JFrame implements KeyListener{
	private GameCanvas gc;
	private Game ga;
	private SquareObject sqo;
	
	public static final int UNIT_SIZE = 15;
	public static final int NORMAL_VELOCITY = 4;
	
	public static final int NORTH_Y = 0;
	public static final int SOUTH_Y = 600;
	public static final int WEST_X  = 0;
	public static final int EAST_X  = 1050;
	
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH =  1050;
	
	private void CreateGUI(){
		add( gc );
		setTitle( "Hackour" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setVisible(true);
		
		Insets insets = getInsets();
		setSize( HackourGame.WINDOW_WIDTH + insets.left + insets.right, HackourGame.WINDOW_HEIGHT + insets.bottom + insets.top );
		
		addKeyListener( this );
		
		Block b = new Block( HackourGame.UNIT_SIZE * 2, HackourGame.SOUTH_Y - ( HackourGame.UNIT_SIZE * 3 ), HackourGame.UNIT_SIZE * 4, HackourGame.UNIT_SIZE );
		
		ga.AddObject( sqo );
		ga.AddObject( b );
		
		ga.start();
		
		sqo.setVelocityX( 0 );
		sqo.setVelocityY( 0 );
	}
	
	public HackourGame(){
		ga = new Game( 25 );
		gc = new GameCanvas(){
			@Override
			public void CanvasPaint( Graphics gfx ){
				gfx.drawRect( HackourGame.WEST_X, HackourGame.NORTH_Y, HackourGame.WEST_X + HackourGame.EAST_X, HackourGame.NORTH_Y + HackourGame.SOUTH_Y );
				
				Iterator<GObject> it = ga.GetObjectIterator();
				while( it.hasNext() ){
					GObject go = it.next();
					
					if( go instanceof PhysicalObject ){
						PhysicalObject drawable = (PhysicalObject) go;
						drawable.paint( gfx );
					}
				}
			}
		};
		ga.setCanvas( gc );
		sqo = new SquareObject( gc, ga );
		
		CreateGUI();
	}
	
	public void LoadLayer(Layer l){
		ga.RemoveObject(sqo);
		
	}
	
	//Key Events
	@Override
	public void keyPressed( KeyEvent e ){
		int keyCode = e.getKeyCode();
		
		switch( keyCode ){
		case KeyEvent.VK_UP:
			sqo.jump();
			break;
		case KeyEvent.VK_LEFT:
			sqo.goHorizontalDirection( CollisionDetector.LEFT, -HackourGame.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_RIGHT:
			sqo.goHorizontalDirection( CollisionDetector.RIGHT, HackourGame.NORMAL_VELOCITY );
			break;
		default:
			break;
		}
	}
	public void keyReleased( KeyEvent e ){
		int keyCode = e.getKeyCode();
		
		switch( keyCode ){
		case KeyEvent.VK_LEFT:
			if( sqo.getVelocityX() < 0 ) sqo.setVelocityX( 0 );
			break;
		case KeyEvent.VK_RIGHT:
			if( sqo.getVelocityX() > 0 ) sqo.setVelocityX( 0 );
			break;
		default:
			break;
		}
	}
	public void keyTyped( KeyEvent e ){}
	
	public static void main(String[] args){
		new HackourGame();
	}
}