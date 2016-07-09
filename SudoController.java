import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.io.*;

// hint button (limit on hints = 5?)
// save / load button
// new board button (load with empty board and start / reset timer when new button is pressed)
// timer
// option to set a time limit (new board with time limit button, click board to begin)
// built in gui yes/no check
// counter for how many checks have been used (reset when new button is pressed)
// win message (type in your name and add to leaderboard)
// if you are going to do a reset, you need to lock out completed sections
// "win" sound (ideas: FF Victory)
// leaderboards (name, time, # of checks, # of hints)
// menu bar to access said leaderboard (possibly new, save / load as well)
// press the 'c' key for check and 'h' for hint, etc.
// when window is closed, ask "Would you like to save?"
// Add an ability to note each square for "possible numbers"
// Add a colour ability to the notes (2 colours?)

public class SudoController extends JFrame{
	private SudoView view;
	private int[][] graph;
	private Timer timer;
	private TimerUpdate tUp;
	private int hCount;				//hint counter
	
	private ActionListener selectionListener;
	private ActionListener checkListener;
	private ActionListener hintListener;
	private ActionListener newListener;
	private ActionListener saveListener;
	private ActionListener loadListener;
	private KeyListener keyListener;
	
	public SudoController(String title){
		view = new SudoView(title);
		graph = new int[9][9];
		hCount = 5;
		
		selectionListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { makeSelection((JButton)e.getSource()); }
		};
		
		checkListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { check(); }
		};
		
		hintListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { hint(); }
		};
		
		newListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { newBoard(); }
		};

		saveListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { save(); }
		};

		loadListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { load(); }
		};

		keyListener = new KeyListener() {
			public void keyPressed (KeyEvent e) { 
				arrowMove(e); 
				addNumber(e);
			}
			public void keyReleased (KeyEvent e) { }
			public void keyTyped (KeyEvent e) { }
		};
		
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				for(int a=0; a<3; a++){
					for(int c=0; c<3; c++){
						view.getPanel()[x][y].getButtons()[a][c].addActionListener(selectionListener);
						view.getPanel()[x][y].getButtons()[a][c].addKeyListener(keyListener);
					}
				}
			}
		}
		
		view.getCheck().addActionListener(checkListener);
		view.getCheck().setEnabled(false);
		view.getHint().addActionListener(hintListener);
		view.getHint().setEnabled(false);
		view.getMenuItems()[0].addActionListener(newListener);
		view.getMenuItems()[1].addActionListener(saveListener);
		view.getMenuItems()[2].addActionListener(loadListener);
		
		view.getTimer().setText("00:00:00");
		view.getHintCount().setText("  "+hCount);
		
		timer = new Timer();
		tUp = new TimerUpdate(view.getTimer());
		timer.schedule(tUp, new Date().getDate(), 1000);
		
		view.setVisible(true);
		view.setSize(890, 620);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		updateGraph();
	}
	
	public void makeSelection(JButton b){
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				for(int a=0; a<3; a++){
					for(int c=0; c<3; c++){
						if(view.getPanel()[x][y].getButtons()[a][c] == b){
							view.getPanel()[x][y].getButtons()[a][c].setSelected(true);
							updateSelected(b);
						}
					}
				}
			}
		}
	}
	
	public int[] getSelected(){
		int n1=-1, n2=-1, n3=-1, n4=-1;
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				for(int a=0; a<3; a++){
					for(int b=0; b<3; b++){
						if(view.getPanel()[x][y].getButtons()[a][b].isSelected()){
							n1=x;
							n2=y;
							n3=a;
							n4=b;
						}
					}
				}
			}
		}	
		int[] a = {n1, n2, n3, n4};
		return a;
	}
	
	public JButton getButton(int x, int y, int a, int b){
		return view.getPanel()[x][y].getButtons()[a][b];
	}
	
	public void arrowMove(KeyEvent k){
		int[] a = getSelected();
		if(a[0]!=-1 && a[1]!=-1 && a[2]!=-1 && a[3]!=-1){	
			if (k.getKeyCode()==KeyEvent.VK_UP){
				if(a[2]-1<0){
					a[2]=2;
					if(a[0]-1<0)
						a[0]=2;
					else
						a[0]--;
				}
				else
					a[2]--;
			}
			if (k.getKeyCode()==KeyEvent.VK_DOWN){
				if(a[2]+1>2){
					a[2]=0;
					if(a[0]+1>2)
						a[0]=0;
					else
						a[0]++;
				}
				else
					a[2]++;
			}
			if (k.getKeyCode()==KeyEvent.VK_LEFT){
				if(a[3]-1<0){
					a[3]=2;
					if(a[1]-1<0)
						a[1]=2;
					else
						a[1]--;
				}
				else
					a[3]--;			
			}
			if (k.getKeyCode()==KeyEvent.VK_RIGHT){
				if(a[3]+1>2){
					a[3]=0;
					if(a[1]+1>2)
						a[1]=0;
					else
						a[1]++;
				}
				else
					a[3]++;
			}
			getButton(a[0], a[1], a[2], a[3]).setSelected(true);
			updateSelected(getButton(a[0], a[1], a[2], a[3]));
		}
	}
	
	public void addNumber(KeyEvent k){
		int[] a = getSelected();
		if(a[0]!=-1 && a[1]!=-1 && a[2]!=-1 && a[3]!=-1){	
			if(getButton(a[0], a[1], a[2], a[3]).isEnabled()){
				switch(k.getKeyCode()){
					case KeyEvent.VK_1: 	getButton(a[0], a[1], a[2], a[3]).setText("1");
												break;
					case KeyEvent.VK_2: 	getButton(a[0], a[1], a[2], a[3]).setText("2");
												break;
					case KeyEvent.VK_3: 	getButton(a[0], a[1], a[2], a[3]).setText("3");
												break;
					case KeyEvent.VK_4: 	getButton(a[0], a[1], a[2], a[3]).setText("4");
												break;
					case KeyEvent.VK_5: 	getButton(a[0], a[1], a[2], a[3]).setText("5");
												break;
					case KeyEvent.VK_6: 	getButton(a[0], a[1], a[2], a[3]).setText("6");
												break;
					case KeyEvent.VK_7: 	getButton(a[0], a[1], a[2], a[3]).setText("7");
												break;
					case KeyEvent.VK_8: 	getButton(a[0], a[1], a[2], a[3]).setText("8");
												break;
					case KeyEvent.VK_9: 	getButton(a[0], a[1], a[2], a[3]).setText("9");
												break;
					case KeyEvent.VK_BACK_SPACE: 	getButton(a[0], a[1], a[2], a[3]).setText("");
															break;
					case KeyEvent.VK_H: 	hint();
												break;
					default:	break; 
				}
				updateGraph();
				winCheck();
			}
			if(k.getKeyCode()==KeyEvent.VK_C) 	
				check();
		}
	}
	
	public void check(){
		int i = 0;
		String t;
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				if(graph[x][y] != view.getBoard().getNums().getBoard()[x][y] && graph[x][y]!=0)
					i++;
			}
		}
		t = view.getField().getText()+"\n- There ";
		if(i==1)
			t = t+"is "+i+" error";
		else
			t = t+"are "+i+" errors";
		t = t+" on this board";
		view.getField().setText(t);
	}
	
	public void hint(){
		if(hCount > 0){
			int[] a = getSelected();
			if(a[0]!=-1 && a[1]!=-1 && a[2]!=-1 && a[3]!=-1){	
				JButton b = getButton(a[0], a[1], a[2], a[3]);
				if(b.isEnabled()){
					b.setEnabled(false);
					b.setBackground(new Color(241, 243, 240));
					b.setText(""+view.getBoard().getNums().getBoard()[(a[0]*3)+a[2]][(a[1]*3)+a[3]]);
				}
				updateGraph();
				view.getBoard().getBoard()[(a[0]*3)+a[2]][(a[1]*3)+a[3]] = 1;
				view.getHintCount().setText("  "+--hCount);
			}
			else{
				String t = view.getField().getText() + "\n- You must select a square to use the hint option";
				view.getField().setText(t);
			}
		}
		else{
			String t = view.getField().getText() + "\n- There are no hints remaining";
			view.getField().setText(t);
		}
	}
	
	public void newBoard(){
		view.newBoard();
		updateGraph();
		hCount = 5;
		tUp.reset();
		tUp.start();
		view.getCheck().setEnabled(true);
		view.getHint().setEnabled(true);
		view.getHintCount().setText("  "+hCount);
		view.getField().setText("- New board");
	}
	
	public void save(){
		if(view.getBoard()!=null){
			File f;
			String saveTo = JOptionPane.showInputDialog("Please make a file name");
			f = new File("Saves/"+saveTo+".txt");
			try{
				PrintWriter aFile = new PrintWriter(new FileWriter(f));
				saveTo(aFile);
				aFile.close();
			}
			catch(IOException e){}
		}
	}
	
	public void saveTo(PrintWriter aFile){
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				aFile.print(""+graph[x][y]);
				aFile.print(""+view.getBoard().getNums().getBoard()[x][y]);
				aFile.println(""+view.getBoard().getBoard()[x][y]);
			}
		}
		view.getField().setText(view.getField().getText()+"\n- Game saved");
	}
	
	public void load(){
		JFileChooser chooser = new JFileChooser("./Saves");
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if(chooser.getSelectedFile().getAbsolutePath().contains("Sudoku/Saves/")){
				try{
					BufferedReader aFile = new BufferedReader(new FileReader(chooser.getSelectedFile()));
					loadFrom(aFile);
					aFile.close();
				}
				catch(IOException e){}
			}
		}
	}
	
	public void loadFrom(BufferedReader aFile){
		try{
			newBoard();
			for(int x=0; x<9; x++){
				for(int y=0; y<9; y++){
					graph[x][y] = aFile.read()-48;
					view.getBoard().getNums().getBoard()[x][y] = aFile.read()-48;
					view.getBoard().getBoard()[x][y] = aFile.read()-48;
					aFile.skip(1);
				}
			}
			updateView();
			view.getField().setText("- Loaded game from file");
		}
		catch(IOException e){ System.out.println("FAIL"); }
	}
	
	public void winCheck(){
		boolean win = true;
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				if(graph[x][y] != view.getBoard().getNums().getBoard()[x][y])
					win = false;
			}
		}
		if(win){
			tUp.stop();
			Object[] options = {"OK", "New Board", "Exit"}; 
			
			int ans = JOptionPane.showOptionDialog(null, 
				"YOU WIN\nCompletion Time: "+view.getTimer().getText(),
				"Congratulations!",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]);
				
			if(ans == 1)
				newBoard();
			else if(ans == 2)
				System.exit(0);
				
			for(int x=0; x<3; x++){
				for(int y=0; y<3; y++){
					for(int a=0; a<3; a++){
						for(int b=0; b<3; b++){
							view.getPanel()[x][y].getButtons()[a][b].setEnabled(false);
							view.getPanel()[x][y].getButtons()[a][b].setSelected(false);
							updateSelected(new JButton());
						}
					}
				}
			}
			
		}
	}
	
	public void updateSelected(JButton b){
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				for(int a=0; a<3; a++){
					for(int c=0; c<3; c++){
						if(view.getPanel()[x][y].getButtons()[a][c] == b && b.isSelected()){
							view.getPanel()[x][y].getButtons()[a][c].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
						}
						else{
							view.getPanel()[x][y].getButtons()[a][c].setSelected(false);
							view.getPanel()[x][y].getButtons()[a][c].setBorder(BorderFactory.createLineBorder(Color.BLACK));
						}
					}
				}
			}
		}		
	}
	
	public void updateGraph(){
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				for(int a=0; a<3; a++){
					for(int b=0; b<3; b++){
						if(view.getPanel()[x][y].getButtons()[a][b].getText() != "")
							graph[x*3+a][y*3+b] = Integer.parseInt(view.getPanel()[x][y].getButtons()[a][b].getText());
						else
							graph[x*3+a][y*3+b] = 0;
					}
				}
			}
		}
	}	
	
	public void updateView(){
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				view.getPanel()[x][y].newPanel(view.getBoard(), x*3, y*3);
				for(int a=0; a<3; a++){
					for(int b=0; b<3; b++){
						if(graph[a+(x*3)][b+(y*3)]!=0)
							view.getPanel()[x][y].getButtons()[a][b].setText(""+graph[a+(x*3)][b+(y*3)]);
					}
				}
			}
		}
	}
	
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch(Exception e) {}
		new SudoController("Sudoku");
	}
}