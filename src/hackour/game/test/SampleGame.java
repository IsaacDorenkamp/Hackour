package hackour.game.test;

import hackour.game.*;

public class SampleGame extends Game{
	private GObject g = new TickPrinter();
	
	public SampleGame(){
		super( 100 );
		AddObject( g );
	}
	
	public static void main(String[] args){
		new SampleGame();
	}
}