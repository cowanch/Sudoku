import java.util.ArrayList;
import java.util.HashSet;

public class SudoSolver{
	private Board board;
	private int[][] graph;
	private ArrayList<HashSet<Integer>> lists;
	
	public SudoSolver(Board b){
		board = b;
		board.disBoard();
		System.out.println();
		graph = new int[9][9];
		lists = new ArrayList<HashSet<Integer>>();
		setupGraph();
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				HashSet set = new HashSet<Integer>();
				if(graph[x][y]==0){
					for(int z=1; z<=9; z++){
						if(checkFor(z, x, y))
							set.add(z);
					}
				}
				lists.add(set);
			}
		}
		updateLists();
		solve();
//		while(!solve()){
//			board = new Board();
//			setupGraph();
//			updateLists();
//		}
	}
	
	public void setupGraph(){
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				if(board.getBoard()[x][y] == 1)
					graph[x][y] = board.getNums().getBoard()[x][y];
				else
					graph[x][y] = 0;
			}
		}
	}
	
	public boolean checkFor(int i, int x, int y){
		int x1;
		int y1;
		for(int a=0; a<9; a++){
			if(graph[x][a] == i)
				return false;
			if(graph[a][y] == i)
				return false;
		}
		if(x>=6) x1=6;
		else if(x>=3) x1=3;
		else x1=0;
		
		if(y>=6) y1=6;
		else if(y>=3) y1=3;
		else y1=0;
		
		for(int a=0; a<3; a++){
			for(int b=0; b<3; b++){
				if(graph[x1+a][y1+b]==i)
					return false;
			}
		}
		return true;
	}
	
	public boolean solve(){
		boolean sol = true;
		while(sol){
			sol = checkBlocks();
			if(!sol)
				sol = checkRowsCols();
			if(!sol)
				sol = checkSS();
			if(!sol){
				lineCancel();
				sol = checkBlocks();
			}
			if(!sol)
				sol = checkRowsCols();
			if(!sol)
				sol = checkSS();
			if(sol){
				updateLists();
				System.out.println();
				disGraph();
			}
		}
		if(graph != board.getNums().getBoard())
			return false;
		return true;
	}
	
	public boolean checkBlocks(){
		boolean found = false;
		int f;
		int fa = 0, fb = 0;
		for(int x=0; x<9; x+=3){
			for(int y=0; y<9; y+=3){
				for(int i=1; i<=9; i++){
					f = 0;
					for(int a=x; a<(x+3); a++){
						for(int b=y; b<(y+3); b++){
							if(lists.get((a*9)+b).contains(i)){
								if(!found && f==0){
									found = true;
									f = i;
									fa = a;
									fb = b;
								}
								else
									found = false;
							}
						}
					}
					if(found){
						graph[fa][fb] = i;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkRowsCols(){
		boolean foundR = false;
		boolean foundC = false;
		int r;
		int c;
		int a1 = 0;
		for(int x=0; x<9; x++){
			for(int i=1; i<=9; i++){
				r = 0;
				c = 0;
				for(int a=0; a<9; a++){
					if(lists.get((x*9)+a).contains(i)){
						if(!foundR && r==0){
							foundR = true;
							r = i;
							a1 = a;
						}
						else
							foundR = false;
					}
					if(lists.get((a*9)+x).contains(i)){
						if(!foundC && c==0){
							foundC = true;
							c = i;
							a1 = a;
						}
						else
							foundC = false;
					}
				}
				if(foundR){
					graph[x][a1] = r;
					return true;
				}
				if(foundC){
					graph[a1][x] = c;
					return true;
				}
			}
		}
		return false;
	}
	
	public void updateLists(){
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				HashSet set = new HashSet<Integer>();
				if(graph[x][y]==0){
					for(int z=1; z<=9; z++){
						if(checkFor(z, x, y))
							set.add(z);
					}
				}
				lists.set((x*9)+y, set);
			}
		}
	}
	
	public boolean checkSS(){
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				if(lists.get((x*9)+y).size()==1){
					for(int i=1; i<=9; i++){
						if(lists.get((x*9)+y).contains(i)){
							graph[x][y] = i;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void lineCancel(){
		boolean line = true;
		while(line){
			line = false;
			for(int x=0; x<9; x+=3){
				for(int y=0; y<9; y+=3){
					for(int i=1; i<=9; i++){
						for(int a=x; a<(x+3); a++){
							for(int b=y; b<(y+3); b++){
								if(lists.get((a*9)+b).contains(i)){
									if(testRowLine(i, x, y, a)){
										for(int z=0; z<9; z++){
											if((z<y || z>(y+3)) && lists.get((a*9)+z).contains(i)){
												lists.get((a*9)+z).remove(i);
												line = true;
											}
										}
									}
									if(testColLine(i, x, y, b)){
										for(int z=0; z<9; z++){
											if((z<x || z>(x+3)) && lists.get((z*9)+b).contains(i)){
												lists.get((z*9)+b).remove(i);
												line = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean testRowLine(int i, int x, int y, int r){
		for(int a=x; a<(x+3); a++){
			for(int b=y; b<(y+3); b++){
				if(lists.get((a*9)+b).contains(i) && a!=r)
					return false;
			}
		}
		return true;
	}
	
	public boolean testColLine(int i, int x, int y, int c){
		for(int a=x; a<(x+3); a++){
			for(int b=y; b<(y+3); b++){
				if(lists.get((a*9)+b).contains(i) && b!=c)
					return false;
			}
		}
		return true;
	}
	
	public void disGraph(){
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				if(y%3==0)
					System.out.print("| ");
				if(graph[x][y]==0)
					System.out.print("  ");
				else
					System.out.print(graph[x][y]+" ");
			}
			System.out.println("");
			if(x%3==2)
				System.out.println(" ----------------------");
		}
	}
	
	public static void main(String args[]){
		SudoSolver s = new SudoSolver(new Board('a'));
	}
}

//Line up cancellation
//Pair/Triple exclusive cancellation