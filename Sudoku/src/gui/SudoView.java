package gui;
import game.Board;

import javax.swing.*;

import java.awt.*;

public class SudoView extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -378046451882394258L;
	private SudoPanel[][] sudoPan;
	private Board board;
	private JButton check;
	private JButton hint;
	private JMenuItem[] menuItems;
	private JTextArea hintCount;
	private JTextArea field;
	private JTextArea timer;
	private JScrollPane sPane;
	
	public SudoPanel[][] getPanel() { return sudoPan; }
	public Board getBoard() { return board; }
	public JButton getCheck() { return check; }
	public JButton getHint() { return hint; }
	public JMenuItem[] getMenuItems() { return menuItems; }
	public JTextArea getHintCount() { return hintCount; }
	public JTextArea getTimer() { return timer; }
	public JTextArea getField() { return field; }
	public JScrollPane getScroll() { return sPane; }
	
	public SudoView(String title){
		super(title);
		
		setLayout(null);
		
		sudoPan = new SudoPanel[3][3];
//		board = new Board(0);
		menuItems = new JMenuItem[3];
		
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				sudoPan[x][y] = new SudoPanel();
				sudoPan[x][y].setLocation(15+(y*150), 15+(x*150));
				add(sudoPan[x][y]);
			}
		}
		
		check = new JButton("Check (C)");
		check.setLocation(500, 60);
		check.setSize(100, 50);
		check.setVisible(true);
		add(check);
		
		hint = new JButton("Hint (H)");
		hint.setLocation(500, 120);
		hint.setSize(100, 50);
		hint.setVisible(true);
		add(hint);
		
		hintCount = new JTextArea();
		hintCount.setLocation(610, 135);
		hintCount.setSize(30, 20);
		hintCount.setEditable(false);
		hintCount.setLineWrap(true);
		hintCount.setWrapStyleWord(true);
		hintCount.setBackground(Color.WHITE);
		hintCount.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hintCount.setFont(new Font("Helvetica", Font.BOLD, 16));
		hintCount.setVisible(true);
		add(hintCount);
		
		field = new JTextArea("- Welcome to Sudoku!\n- Please select \"New Board\" from the Game menu to start a new puzzle");
		field.setEditable(false);
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		field.setBackground(Color.WHITE);
		field.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		field.setVisible(true);
		sPane = new JScrollPane(field);
		sPane.setLocation(500, 315);
		sPane.setSize(250, 155);
		add(sPane);
		
		timer = new JTextArea();
		timer.setLocation(500, 275);
		timer.setSize(100, 30);
		timer.setEditable(false);
		timer.setLineWrap(true);
		timer.setWrapStyleWord(true);
		timer.setBackground(Color.WHITE);
		timer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		timer.setVisible(true);
		add(timer);		
		
		JMenuBar menu = new JMenuBar();
		
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		menu.add(gameMenu);
		JMenuItem newGame = new JMenuItem("New Board");
		gameMenu.add(newGame);
		menuItems[0] = newGame;
		JSeparator sep = new JSeparator();
		gameMenu.add(sep);
		JMenuItem save = new JMenuItem("Save");
		gameMenu.add(save);
		menuItems[1] = save;
		JMenuItem load = new JMenuItem("Load");
		gameMenu.add(load);
		menuItems[2] = load;
		
		setJMenuBar(menu);
	}
	
	public void newBoard(){
		board = new Board();
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++)
				sudoPan[x][y].newPanel(board, x*3, y*3);
		}
	}
	
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch(Exception e) {}
		JFrame.setDefaultLookAndFeelDecorated(true); 
		SudoView frame = new SudoView("Sudoku");
		frame.setSize(890, 620); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}