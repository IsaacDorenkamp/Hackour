package hackour.devtools;

import hackour.game.*;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.*;

import java.util.HashMap;

public class DevTools extends JFrame implements ActionListener{
	private HackourGame dev_on;
	
	private JTextArea logarea = new JTextArea();
	private JTextArea vararea = new JTextArea();
	private JMenuBar mbar = new JMenuBar();
	private JMenu cheats = new JMenu("Cheats");
	private JMenuItem gravity_invert = new JMenuItem("Invert Current Gravity State");
	private JMenuItem edit_mode = new JMenuItem("Switch to Edit Mode");
	private JMenuItem save_level = new JMenuItem("Save Level");
	private JMenuItem load_level = new JMenuItem("Load Level");
	private JMenuItem clear_level = new JMenuItem("Clear Level");
	private void CreateGUI(){
		setTitle("Hackour DevTools");
		setSize( 500, 350 );
		setLayout( new GridLayout(1,2) );
		add( new JScrollPane(logarea) );
		add( new JScrollPane(vararea) );
		logarea.setEditable(false);
		vararea.setEditable(false);
		
		//Menu Stuff
		cheats.add(gravity_invert);
		cheats.add(edit_mode);
		cheats.add(save_level);
		cheats.add(load_level);
		cheats.add(clear_level);
		mbar.add(cheats);
		
		setJMenuBar( mbar );
		
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
	}
	private void InitEvents(){
		gravity_invert.addActionListener(this);
		edit_mode.addActionListener(this);
		save_level.addActionListener(this);
		load_level.addActionListener(this);
		clear_level.addActionListener(this);
	}
	public DevTools(HackourGame hg){
		dev_on = hg;
		
		CreateGUI();
		InitEvents();
	}
	public void log(String msg){
		if( logarea.getText().isEmpty() ){
			logarea.setText(msg);
		}else{
			logarea.append( '\n' + msg );
		}
	}
	
	private HashMap<String,String> vars = new HashMap<>();
	public void log_var( String var, String value ){
		if( !vars.containsKey(var) ){
			vars.put( var, value );
		}else vars.replace(var, value);
		vararea.setText("");
		boolean first = true;
		for( String key : vars.keySet() ){
			if(first){
				vararea.setText( String.format("%s: %s", key, vars.get(key)) );
				first = false;
			}else{
				vararea.append('\n' + String.format("%s: %s", key, vars.get(key)) );
			}
		}
	}
	
	public void set(String val){
		logarea.setText(val);
	}
	
	public void ShowWindow(){
		setVisible(true);
	}
	
	public void actionPerformed( ActionEvent evt ){
		Object src = evt.getSource();
		if( src.equals(gravity_invert) ){
			PhysicsModule.INVERT_GRAVITY = !PhysicsModule.INVERT_GRAVITY;
			log("Gravity inverted.");
		}else if( src.equals(edit_mode) ){
			SquareObject character = dev_on.GetCharacter();
			character.ToggleMode();
			dev_on.setGridlines( !dev_on.getGridlines() );
			if( character.IsEditMode() ){
				log("Switched to Edit Mode.");
				edit_mode.setText("Switch to Play Mode");
			}else{
				log("Switched to Play Mode");
				edit_mode.setText("Switch to Edit Mode");
			}
		}else if( src.equals(save_level) ){
			dev_on.SaveLevel();
		}else if( src.equals(load_level) ){
			String input = JOptionPane.showInputDialog(this,"Load File");
			if( input == null ) return;
			dev_on.LoadLevel( input );
		}else if( src.equals(clear_level) ){
			dev_on.reset();
		}else;
	}
}