package game;

import java.util.Random;
import java.io.*;

public class Board{
	private int[][] board;
	private NumGen n;
	private Random r;
	
	public int[][] getBoard(){ return board; }
	public NumGen getNums(){ return n; }
	
	public Board(){
		n = new NumGen();
		r = new Random();
		board = boardGen(r.nextInt(6));
//		board = boardGen(0);
		checkNums();
	}
	
	public Board(char c){
		n = new NumGen();
		r = new Random();
		board = boardGen(c);
	}
	
	public int[][] boardGen(int i){
		int[][] a = new int[9][9];
		try{
			BufferedReader gen = new BufferedReader(new FileReader("boards.txt"));
			for(int x=0; x<i*9; x++)
				gen.skip(10);
			for(int x=0; x<9; x++){
				for(int y=0; y<9; y++)
					a[x][y] = (int)gen.read()-48;
				gen.skip(1);
			}
			gen.close();
		}
		catch(FileNotFoundException e){}
		catch(IOException e){}
		return a;
	}
	
	public int[][] boardGen(char c){
		int[][] a = new int[9][9];
		try{
			BufferedReader gen = new BufferedReader(new FileReader("solver.txt"));
			for(int x=0; x<0; x++)
				gen.skip(10);
			for(int x=0; x<9; x++){
				for(int y=0; y<9; y++)
					a[x][y] = (int)gen.read()-48;
				gen.skip(1);
			}
			gen.close();
		}
		catch(FileNotFoundException e){}
		catch(IOException e){}
		return a;
	}
	
	public void disBoard(){
		for(int x=0; x<9; x++){
			for(int y=0; y<9; y++){
				if(y%3==0)
					System.out.print("| ");
				if(board[x][y] == 1)
					System.out.print(n.getBoard()[x][y]+" ");
				else
					System.out.print("  ");
			}
			System.out.println("");
			if(x%3==2)
				System.out.println(" ----------------------");
		}
	}
	
	public void checkNums(){
		boolean check;
		do{
			check = true;
			for(int x=0; x<9; x++){
				for(int y=0; y<9; y++){
					if(n.getBoard()[x][y]==0)
						check = false;
				}
			}
			n = new NumGen();
		}while(!check);
	}
	
	public static void main(String args[]){
		Board b = new Board('a');
		b.disBoard();
	}
}