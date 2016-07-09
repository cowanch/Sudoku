import javax.swing.*;
import java.awt.*;

public class SudoPanel extends JPanel{
	
	private JButton[][] spaces;
	private Board b;
	
	public JButton[][] getButtons() { return spaces; }
	public Board getBoard() { return b; }
	
	public SudoPanel(){
		spaces = new JButton[3][3];
		
		setLayout(new GridLayout(3,3,-1,-1));
		setSize(150, 150);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				spaces[x][y] = new JButton();
				spaces[x][y].setBackground(new Color(241, 243, 240));
				spaces[x][y].setEnabled(false);
				spaces[x][y].setFont(new Font("Helvetica", Font.PLAIN, 18));
				add(spaces[x][y]);
			}
		}
	}
	
	public void newPanel(Board board, int x1, int y1){
		
		b = board;
		
		setLayout(new GridLayout(3,3,-1,-1));
		setSize(150, 150);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				spaces[x][y].setBackground(Color.WHITE);
				spaces[x][y].setEnabled(true);
				spaces[x][y].setText("");
//				spaces[x][y].setBorder(BorderFactory.createLineBorder(Color.RED));
				if(b.getBoard()[x+x1][y+y1]==1){
					spaces[x][y].setText(""+b.getNums().getBoard()[x+x1][y+y1]);
					spaces[x][y].setBackground(new Color(241, 243, 240));
					spaces[x][y].setEnabled(false);
				}
			}
		}
	}
	
	public static void main(String args[]) { 
		JFrame f = new JFrame("Sudoku Panel Test"); 
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch(Exception e) {}
		JFrame.setDefaultLookAndFeelDecorated(true);
		f.getContentPane().add(new SudoPanel()); 
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		f.setSize(240, 240);  
		f.setVisible(true); 
	}
}