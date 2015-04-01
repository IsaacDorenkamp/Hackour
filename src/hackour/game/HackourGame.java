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
		
		for( int i = HackourGame.UNIT_SIZE ; i < HackourGame.UNIT_SIZE * 20; i += HackourGame.UNIT_SIZE ){
			Block b = new Block( i, gc.getHeight() - 90 );
			ga.AddObject( b );
		}
		
		ga.AddObject( sqo );
		ga.AddObject( gc );
		
		ga.start();
		
		sqo.setVelocityX( 0 );
		sqo.setVelocityY( 0 );
	}
	
	public HackourGame(){
		ga = new Game( 25 );
		gc = new GameCanvas(){
			@Override
			public void CanvasPaint( Graphics g ){
				Iterator<GObject> it = ga.GetObjectIterator();
				
				Iterator<GObject> col_arr_it = ga.GetObjectIterator();
				ArrayList<PhysicalObject> pobjs = new ArrayList<>();
				while( col_arr_it.hasNext() ){
					GObject cur = col_arr_it.next();
					if( cur instanceof PhysicalObject ){
						pobjs.add( (PhysicalObject) cur );
					}
				}
				
				CollisionDetector.CheckCollisions( pobjs.iterator() );
				while( it.hasNext() ){
					GObject go = it.next();
					
					if( go instanceof SquareObject ){
						SquareObject so = (SquareObject) go;
						g.fillRect( so.getX(), so.getY(), so.getSide(), so.getSide() );
					}else if( go instanceof Block ){
						Block b = (Block) go;
						b.draw( g );
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