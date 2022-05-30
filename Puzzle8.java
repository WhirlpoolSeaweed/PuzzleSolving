import java.io.*;
import java.util.*;

public class Puzzle8 {
	public static class State {
		int[] board = {1,2,3,
					   4,5,6,
					   7,8,0};
		int moves = 0;
		int mht = 0;
		int empIndex = 8;
		State prev = null;
		
		// constructor
		public State(int[] tmpboard) {
			this.board = Arrays.copyOf(tmpboard, 9);
			this.mht = getMHT(tmpboard);
			this.empIndex = getEMP(tmpboard);
		}
		
		public State(State prevstate) {
			this.board = Arrays.copyOf(prevstate.board, 9);
			this.mht = prevstate.mht;
			this.moves = prevstate.moves;
			this.empIndex = prevstate.empIndex;
		}
		
		public static int getMHT(int[] tmpboard) {
			int mht = 0;
			//x = i%3 goal_x = tmpboard[i]%3-1
			//y = i/3 goal_y = (tmpboard[i]-1)/3
			for(int i = 0; i < 9; i++){
				if (tmpboard[i] != 0) {
					mht += Math.abs(i%3 - (tmpboard[i]-1)%3) + 
						   Math.abs(i/3 - (tmpboard[i]-1)/3);
				}
			}
			return mht;
		}
		
		public static int getEMP(int[] tmpboard){
			for(int i = 0; i < 9; i++){
				if (tmpboard[i] == 0)return i;
			}
			return -1;
		}
	}
	
	public static PriorityQueue<State> moveQ = new PriorityQueue<State>(new Comparator<State>(){
			@Override
			public int compare(State s1, State s2) {
				return (s1.moves+s1.mht) - (s2.moves+s2.mht);
			}
		});
		
	public static Queue<State> finalmoves = new LinkedList<State>();
	
	public static State makeMoves(State state){
		int x0 = state.empIndex%3;
		int y0 = state.empIndex/3;
		Stack<Integer> availablemoves = new Stack<Integer>();
		if(x0 - 1 >= 0)availablemoves.push(y0*3+x0-1);
		if(x0 + 1 <= 2)availablemoves.push(y0*3+x0+1);
		if(y0 - 1 >= 0)availablemoves.push((y0-1)*3+x0);
		if(y0 + 1 <= 2)availablemoves.push((y0+1)*3+x0);
		while(!availablemoves.isEmpty()) {
			State move = new State(state);
			int moved = availablemoves.pop();
			move.board[move.empIndex] = move.board[moved];
			move.board[moved] = 0;
			move.empIndex = moved;
			move.mht = move.getMHT(move.board);
			move.moves++;
			move.prev = state;
			moveQ.add(move);
		}
		return moveQ.poll();
	}
	
	public static void main(String args[]) throws Exception {
		BufferedReader readTxt = new BufferedReader(new FileReader(new File(args[0])));
		String textLine = "";
		String str = "";
		while(( textLine = readTxt.readLine()) != null){
			str += textLine + " ";
		}
		String[] numbersArray = str.split(" ");
		int[] initialBoard = new int[9];
		for(int i = 0; i < 9; i++) {
			initialBoard[i] = Integer.parseInt(numbersArray[i]);
		}
		State tmpstate = new State(initialBoard);
		boolean unsolved = true;
		int[] goalboard = {1,2,3,
						   4,5,6,
						   7,8,0};
		FileWriter fwriter = new FileWriter(args[1], false);
        BufferedWriter bwriter = new BufferedWriter(fwriter);
		while (unsolved) {
			if (Arrays.equals(tmpstate.board, goalboard))break;
			tmpstate = makeMoves(tmpstate);
		}
		Stack<State> reversePath = new Stack<State>();
		reversePath.push(tmpstate);
		while(tmpstate.prev != null){
			tmpstate = tmpstate.prev;
			reversePath.push(tmpstate);
		}
		while(!reversePath.isEmpty()){
			tmpstate = reversePath.pop();
			if(!reversePath.isEmpty()){
				for(int i = 0; i <= 6; i = i+3){
					bwriter.write(tmpstate.board[i]+" "+tmpstate.board[i+1]+" "+tmpstate.board[i+2]);
					bwriter.newLine();
				}
				bwriter.newLine();
			}
			else{
				for(int i = 0; i <= 6; i = i+3){
					if(i == 6){
						bwriter.write(tmpstate.board[i]+" "+tmpstate.board[i+1]+" "+tmpstate.board[i+2]);
					}
					else{
						bwriter.write(tmpstate.board[i]+" "+tmpstate.board[i+1]+" "+tmpstate.board[i+2]);
						bwriter.newLine();
					}
				}
			}
		}
		bwriter.flush();
        bwriter.close();
	}
}