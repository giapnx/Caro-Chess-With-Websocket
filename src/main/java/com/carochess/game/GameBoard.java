package com.carochess.game;

import com.carochess.game.Game.PlayerLetter;

/**
 * A board for a game of Caro Chess. Represents the current state of the game and which cells have been selected by whom. 
 * 
 */
public class GameBoard {
	
	public static final int MAX_ROW = 15;
	public static final int MAX_COL = 15;
	
	PlayerLetter[][] cells = new PlayerLetter[MAX_ROW][MAX_COL];
	
	/**
	 * Mark a cell with the player's selection.
	 * 
	 * @param gridId
	 * @param player
	 */
	protected void markCell(int gridId, PlayerLetter player) 
	{
		int row = (gridId - 1)/MAX_COL;
		int col = (gridId - 1) - row*MAX_COL;
//		System.out.println("row: " + row + " col: " + col);
		cells[row][col] = player;
	}
	
	protected void resetBoardGame() 
	{
		cells = new PlayerLetter[MAX_ROW][MAX_COL];
	}
	
	/**
	 * Compare the current state of the game board with the possible winning combinations to determine a win condition.
	 * This should be checked at the end of each turn.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isWinner(PlayerLetter player) {
		
		for (int i = 0; i < MAX_ROW; i++) 
		{
			for (int j = 0; j < MAX_COL; j++) 
			{
				if(cells[i][j] == null || cells[i][j] != player) continue;
				
				if(i <= MAX_ROW - 5)
				{
					// Check in vertical
					if(cells[i+1][j] == player && cells[i+2][j] == player
					&& cells[i+3][j] == player && cells[i+4][j] == player) return true;
					
					// Check in diagonal down
		            if(j <= MAX_COL - 5 && cells[i+1][j+1] == player && cells[i+2][j+2] == player 
		            && cells[i+3][j+3] == player && cells[i+4][j+4] == player) return true;
				}
	 
	            if(j <= MAX_COL - 5) 
            	{
            		// Check in horizontal
            		if(cells[i][j+1] == player && cells[i][j+2] == player
    				&& cells[i][j+3] == player && cells[i][j+4] == player) return true;
            		
            		// Check in diagonal up	            
    	            if(i >= 4 && cells[i-1][j+1] == player && cells[i-2][j+2] == player 
    	            && cells[i-3][j+3] == player && cells[i-4][j+4] == player) return true;
            	}   
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
