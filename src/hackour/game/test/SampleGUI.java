package hackour.game.test;

import hackour.game.*;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.util.Iterator;

public class SampleGUI extends JFrame implements KeyListener{
	private GameCanvas gc;
	private Game ga;
	private SquareObject sqo;
	
	public static final int NORMAL_VELOCITY = 4;
	
	private void CreateGUI(){
		
		add( gc );
		setTitle( "Sample Game GUI" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 500, 500 );
		setVisible(true);
		
		addKeyListener( this );
		
		sqo.setX( (getWidth() / 2) - ( sqo.getSide() / 2 ) );
		sqo.setY( (getHeight() / 2)  - ( sqo.getSide() / 2 ) );
		
		ga.start();
	}
	
	public SampleGUI(){
		ga = new Game( 25 );
		gc = new GameCanvas(){
			@Override
			public void CanvasPaint( Graphics g ){
				Iterator<GObject> it = ga.GetObjectIterator();
				while( it.hasNext() ){
					GObject go = it.next();
					if( go instanceof SquareObject ){
						SquareObject so = (SquareObject) go;
						g.fillRect( so.getX(), so.getY(), so.getSide(), so.getSide() );
					}
				}
			}
		};
		sqo = new SquareObject( gc );
		ga.AddObject( sqo );
		ga.AddObject( gc );
		
		CreateGUI();
	}
	
	//Key Events
	@Override
	public void keyPressed( KeyEvent e ){
		int keyCode = e.getKeyCode();
		
		switch( keyCode ){
		case KeyEvent.VK_UP:
			sqo.setVelocityY( -SampleGUI.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_DOWN:
			sqo.setVelocityY( SampleGUI.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_LEFT:
			sqo.setVelocityX( -SampleGUI.NORMAL_VELOCITY );
			break;
		case KeyEvent.VK_RIGHT:
			sqo.setVelocityX( SampleGUI.NORMAL_VELOCITY );
			break;
		default:
			break;
		}
	}
	public void keyReleased( KeyEvent e ){
		int keyCode = e.getKeyCode();
		
		switch( keyCode ){
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			sqo.setVelocityY( 0 );
			break;
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
		new SampleGUI();
	}
}