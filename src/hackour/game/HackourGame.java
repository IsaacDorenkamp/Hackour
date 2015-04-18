package hackour.game;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.util.Iterator;
import java.util.ArrayList;

public class HackourGame extends JFrame implements KeyListener{
	private GameCanvas gc;
	private Game ga;
	private SquareObject sqo;
	
	public static final int UNIT_SIZE = 15;
	public static final int NORMAL_VELOCITY = 4;
	public static final int WINDOW_SIZE = 500;
	
	private void CreateGUI(){
		add( gc );
		setTitle( "Hackour" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( HackourGame.WINDOW_SIZE, HackourGame.WINDOW_SIZE );
		setVisible(true);
		
		addKeyListener( this );
		
		Block b = new Block( HackourGame.UNIT_SIZE * 2, gc.getHeight() - ( HackourGame.UNIT_SIZE * 3 ), HackourGame.UNIT_SIZE * 4, HackourGame.UNIT_SIZE );
		
		ga.AddObject( sqo );
		ga.AddObject( b );
		ga.AddObject( gc ); //GameCanvas paints every tick. It must be added to the Game object so that the game object will tick it.
		
		ga.start();
		
		sqo.setVelocityX( 0 );
		sqo.setVelocityY( 0 );
	}
	
	public HackourGame(){
		ga = new Game( 25 );
		gc = new GameCanvas(){
			@Override
			public void CanvasPaint( Graphics gfx ){
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
		sqo = new SquareObject( gc, ga );
		
		CreateGUI();
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
		case KeyEvent.VK_RIGHT:
			sqo.setVelocityX( 0 );
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