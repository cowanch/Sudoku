import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;

public class NumGen{
	private int[][] board;
	
	public int[][] getBoard(){ return board; }

	public NumGen(){
		board = new int[9][9];
		
		Random r = new Random();
		HashSet<Integer> used = new HashSet<Integer>();
		HashSet<Integer> bNum = new HashSet<Integer>();	
		int a = 0;
		int x = 0;
		int y = 0;
		
		while(x<=6 && y<=6){
			for(int x1=x; x1<x+3; x1++){
				for(int y1=y; y1<y+3; y1++){
					a = 0;					
					if(board[x1][y1]==0){
						while(true){
							while(a==0 || used.contains(a))
								a = r.nextInt(9)+1;
							if(check(x1,y1,a)){
								board[x1][y1] = a;
								bNum.add(a);
								break;
							}
							used.add(a);
							if(setFull(used)){
								HashSet<Integer> tempSet = new HashSet<Integer>(bNum);
								while(tempSet.contains(a)){
									a = r.nextInt(9)+1;
									if(!checkRow(x1, a))
										tempSet.add(a);
									if(setFull(tempSet)){
										while(bNum.contains(a))
											a = r.nextInt(9)+1;
										break;
									}
								}
								board[x1][y1] = a;
								bNum.add(a);
								int u = 0;
								if(findY(x1, y1, board[x1][y1]) >= 0)
									u = swapRows(x1, y1, 0);
								if(findX(x1+u, y1, board[x1+u][y1]) >= 0)
									swapCols(x1+u, y1, 0);
								y1=y-1;
								break;
							}
						}
						used.clear();
					}
				}
			}
			bNum.clear();
			if(y==6){
				y=0;
				x+=3;
			}
			else
				y+=3;
		}
	}
	
	private boolean check(int x, int y, int a){
		return checkBlock(x, y, a) && checkRow(x, a) && checkCol(y, a);
	}
	
	private boolean checkBlock(int x, int y, int a){
		int b1;
		int b2;
		
		if(x>=6) b1=6;
		else if(x>=3) b1=3;
		else b1=0;
		
		if(y>=6) b2=6;
		else if(y>=3) b2=3;
		else b2=0;
		
		for(int x1=0; x1<3; x1++){
			for(int y1=0; y1<3; y1++){
				if(board[b1+x1][b2+y1]==a)
					return false;
			}
		}
		
		return true;
	}
	
	private boolean checkRow(int x, int a){
		for(int y1=0; y1<9; y1++){
			if(board[x][y1]==a)
				return false;
		}
		return true;
	}
	
	private boolean checkCol(int y, int a){
		for(int x1=0; x1<9; x1++){
			if(board[x1][y]==a)
				return false;
		}
		return true;
	}
	
	private int swapRows(int x, int y, int a){
		int temp = 0;
		int f;
		if(a==0){
			if(x%3==0)
				a = (checkRow(x+1, board[x][y])) ? 1 : 2;
			else if(x%3==1)
				a = (checkRow(x+1, board[x][y])) ? 1 : -1;
			else
				a = (checkRow(x-1, board[x][y])) ? -1 : -2;
		}
		temp = board[x][y];
		board[x][y] = board[x+a][y];
		board[x+a][y] = temp;
		f = findY(x, y, board[x][y]);
		if(board[x][y] == 0 || f < 0)
			return a;
		if(f >= 0){
			swapRows(x, f, a);
		}
		return a;
	}

	private int swapCols(int x, int y, int a){
		int temp = 0;
		int f;
		if(a==0){
			if(y%3==0)
				a = (checkCol(y+1, board[x][y])) ? 1 : 2;
			else if(y%3==1)
				a = (checkCol(y+1, board[x][y])) ? 1 : -1;
			else
				a = (checkCol(y-1, board[x][y])) ? -1 : -2;
		}
		temp = board[x][y];
		board[x][y] = board[x][y+a];
		board[x][y+a] = temp;
		f = findX(x, y, board[x][y]);
		if(board[x][y] == 0)
			return a;
		if(f >= 0)
			swapCols(f, y, a);
		return 0;
	}
	
	private int findY(int x, int y, int a){
		for(int y1=0; y1<9; y1++){
			if(board[x][y1]==a && y1!=y)
				return y1;
		}
		return -1;
	}
	
	private int findX(int x, int y, int a){
		for(int x1=0; x1<9; x1++){
			if(board[x1][y]==a && x1!=x)
				return x1;
		}
		return -1;
	}
	
	private boolean setFull(HashSet<Integer> s){
		return s.contains(1) && s.contains(2) && s.contains(3) 
				 && s.contains(4) && s.contains(5) && s.contains(6) 
				 && s.contains(7) && s.contains(8) && s.contains(9);
	}
}