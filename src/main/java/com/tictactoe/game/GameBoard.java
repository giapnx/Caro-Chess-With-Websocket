package com.tictactoe.game;

import com.tictactoe.game.Game.PlayerLetter;

/**
 * A board for a game of Tic Tac Toe. Represents the current state of the game and which cells have been selected by whom. 
 * 
 */
public class GameBoard {
	
	/* The number of winning combinations are small, so we'll keep it simple and do "brute force" matching.
	 * For a game with a larger grid (such as Go), we would need to develop an algorithm, potentially based on
	 * "Magic square". http://en.wikipedia.org/wiki/Magic_square
	 */
	public static final int[][] WINNING = { {1,2,3}, {4,5,6}, {7,8,9}, {1,4,7}, {2,5,8}, {3,6,9}, {1,5,9}, {3,5,7} };
	
	public static final int MAX_ROW = 9;
	public static final int MAX_COL = 9;
	
	/*
	 * Represents a flattened game board for Tic Tac Toe. Below is the index value for each game cell.
	 * 
	 *    1 | 2 | 3
	 *    4 | 5 | 6
	 *    7 | 8 | 9
	 */
	PlayerLetter[][] cells = new PlayerLetter[MAX_ROW][MAX_COL];
	
	/**
	 * Mark a cell with the player's selection.
	 * 
	 * @param gridId
	 * @param player
	 */
	protected void markCell(int gridId, PlayerLetter player) {
		System.out.println("gridID: "+gridId);
		int row = (gridId - 1)/MAX_COL;
		int col = (gridId - 1) - row*MAX_COL;
		System.out.println("row: "+row + " | col: "+col);
		
		cells[row][col] = player;
	}
	
	/**
	 * Compare the current state of the game board with the possible winning combinations to determine a win condition.
	 * This should be checked at the end of each turn.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isWinner(PlayerLetter player) {
//		for (int i = 0; i < WINNING.length; i++) {
//			int[] possibleWinningCombo = WINNING[i];			
//			if (cells[possibleWinningCombo[0]-1] == player && cells[possibleWinningCombo[1]-1] == player && cells[possibleWinningCombo[2]-1] == player) {
//				return true;
//			}
//		}		
//		return false;
		
		for (int i = 0; i < MAX_ROW-4; i++) 
		{
			for (int j = 0; j < MAX_COL-4; j++) 
			{
				if(cells[i][j] == null) continue;
				// Check in vertical
				if(cells[i][j]==cells[i+1][j]&&cells[i][j]==cells[i+2][j]
	            &&cells[i][j]==cells[i+3][j]&&cells[i][j]==cells[i+4][j]
	            &&cells[i][j]==player)return true;
	 
				// Check in diagonal down
	            if(cells[i][j]==cells[i+1][j+1]&&cells[i][j]==cells[i+2][j+2]
	            &&cells[i][j]==cells[i+3][j+3]&&cells[i][j]==cells[i+4][j+4]
	            &&cells[i][j]==player)return true;
	 
	            // Check in horizontal
	            if(cells[i][j]==cells[i][j+1]&&cells[i][j]==cells[i][j+2]
	            &&cells[i][j]==cells[i][j+3]&&cells[i][j]==cells[i][j+4]
	            &&cells[i][j]==player)return true;
	            
	            // Check in diagonal up
	            if (i < 4) continue;
	            
	            if(cells[i][j]==cells[i-1][j+1]&&cells[i][j]==cells[i-2][j+2]
	            &&cells[i][j]==cells[i-3][j+3]&&cells[i][j]==cells[i-4][j+4]
	            &&cells[i][j]==player)return true;

			}
		}
			
		return false;
	}
	
	/**
	 * Determines if the game is tied. The game is considered tied if there is no winner and all cells have been selected.
	 */
	public boolean isTied() {
		boolean boardFull = true;
		boolean tied = false;
		
//		for (int i = 0; i < 9; i++) {
//			PlayerLetter letter = cells[i];
//			if (letter == null) {
//				boardFull = false;
//			}
//		}
//		
//		if (boardFull && (!isWinner(PlayerLetter.X) || !isWinner(PlayerLetter.O))) {
//			tied = true;
//		}
		
		return tied;
	}
}
