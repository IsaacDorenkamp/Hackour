package hackour.game.test;

import hackour.game.*;

public class TickPrinter implements GObject{
	private String msg;
	public TickPrinter(String message){
		msg = message;
	}
	public TickPrinter(){
		this("Tick.");
	}
	
	public void doTick( Game g ){
		System.out.println( msg );
	}
}