package com.carochess.game;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a game of Caro Chess. Contains players and a game board.
 * 
 */
public class Game {
	
	private static int GAME_COUNT = 1;
	
	private String room_name;
	
	public enum Status {
	    WAITING, IN_PROGRESS, WON, TIED
	}
	
	public enum PlayerLetter { 
		X, O
	}
	
	private int[] scores;
	
	// The game ID. The server increments this count with each new game initiated.
	private final int id;
	
	// Status of the current game (WAITING, IN_PROGRESS, FINISHED)
	private Status status;
	
	private final GameBoard board;	
	private Map<PlayerLetter, Player> players;
	private PlayerLetter firstLetter;
	private PlayerLetter secondLetter;
	private PlayerLetter winner;

	public Game(String room_name) {
		this.id = GAME_COUNT++;
		this.setRoom_name(room_name);
		this.board = new GameBoard();
		status = Status.WAITING;
		players = new EnumMap<PlayerLetter, Player>(PlayerLetter.class);
		scores = new int[]{0,0};	// score[0] ~ X, score[1] ~ O
	}
	
	/**
	 * Reset Game when one of two player win.
	 */
	public void resetGame()
	{
		status = Status.IN_PROGRESS;
		board.resetBoardGame();
	}
	
	/**
	 * Increment score of player win
	 */
	public void incrementScoreOf(PlayerLetter playerLetter)
	{
		if (playerLetter == PlayerLetter.X) {
			scores[0]++;
		} else {
			scores[1]++;
		}
	}
	
	public int  getScoreOf(PlayerLetter playerLetter) 
	{
		if (playerLetter == PlayerLetter.X) {
			return scores[0];
		} else {
			return scores[1];
		}
	}
	
	public PlayerLetter getFirstLetter() {
		return firstLetter;
	}

	public PlayerLetter getSecondLetter() {
		return secondLetter;
	}
	
	/**
	 * Adds a player to this game. Changes status of game from WAITING to IN_PROGRESS if the game fills up.
	 * 
	 * @param p
	 * @return
	 * @throws RuntimeException if there are already 2 or more players assigned to this game. 
	 */
	public PlayerLetter addPlayer(Player p) {
		if (players.size() >= 2) {
			throw new RuntimeException("Too many players. Cannot add more than 1 player to a game.");
		}
		
		if (players.size() == 0)
		{
			// random letter X or O
			Random generator = new Random();
			int num = generator.nextInt(2);
			
			firstLetter = (num % 2 == 0) ? PlayerLetter.O : PlayerLetter.X;
			
			p.setLetter(firstLetter);
			players.put(firstLetter, p);
			
			return firstLetter;
		}
		else
		{
			secondLetter = (firstLetter == PlayerLetter.O) ? PlayerLetter.X : PlayerLetter.O;
			
			p.setLetter(secondLetter);
			players.put(secondLetter, p);
			
			status = Status.IN_PROGRESS;
			
			return secondLetter;
		}
				
		
	}
	
	/**
	 * Marks the selected cell of the user and updates the game's status.
	 * 
	 * @param gridId
	 * @param playerLetter
	 */
	public void markCell(int gridId, PlayerLetter playerLetter) {
		board.markCell(gridId, playerLetter);
		setStatus(playerLetter);
	}
	
	/**
	 * Updates the status of the game. Invoked after each player's turn.
	 * 
	 * @param playerLetter
	 */
	private void setStatus(PlayerLetter playerLetter) {		
		// Checks first to see if the board has a winner.
		if (board.isWinner(playerLetter)) {
			status = Status.WON;
			
			if (playerLetter == PlayerLetter.X) {
				winner = PlayerLetter.X;
			} else {
				winner = PlayerLetter.O;
			}
		// Next check to see if the game has been tied.	
		} else if (board.isTied()) {
			status = Status.TIED;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public Collection<Player> getPlayers() {
		return players.values();
	}
	
	public Player getPlayer(PlayerLetter playerLetter) {
		return players.get(playerLetter);
	}
	
	public Player getFirstPlayer()
	{
		return players.get(firstLetter);
	}
	
	public Player getSecondPlayer()
	{
		return players.get(secondLetter);
	}
	
	/**
	 * Returns the opponent given a player letter.
	 */
	public Player getOpponent(String currentPlayer) {
		PlayerLetter currentPlayerLetter = PlayerLetter.valueOf(currentPlayer);
		PlayerLetter opponentPlayerLetter = currentPlayerLetter.equals(PlayerLetter.X) ? PlayerLetter.O : PlayerLetter.X;
		return players.get(opponentPlayerLetter);
	}	
	
	public GameBoard getBoard() {
		return board;
	}
	
	public Status getStatus() {
		return status;
	}	
	
	public PlayerLetter getWinner() {
		return winner;
	}
	
	/**
	 * Convenience method to determine if a specific player is the winner.
	 */
	public boolean isPlayerWinner(PlayerLetter playerLetter) {
		return status == Status.WON && winner == playerLetter;
	}

	/**
	 * Convenience method to determine if the game has been tied.
	 */	
	public boolean isTied() {
		return status == Status.TIED;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
}
