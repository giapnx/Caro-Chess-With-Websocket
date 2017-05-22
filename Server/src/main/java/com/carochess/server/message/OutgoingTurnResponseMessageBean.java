package com.carochess.server.message;

/**
 * Represents response message
 * 
 */
public class OutgoingTurnResponseMessageBean extends MessageBean {
	private final String type = "turn_response";
	private String opponent;
	private int gridId;
//	private boolean winner;
//	private boolean tied;
	
	public OutgoingTurnResponseMessageBean(String opponent, int grid) {
		this.opponent = opponent;
		this.gridId = grid;
//		this.winner = winner;
//		this.tied = tied;
	}
	
	public String getType() {
		return type;
	}
	
	public String getOpponent() {
		return opponent;
	}

	public int getGrid() {
		return gridId;
	}
	
//	public boolean getWinner() {
//		return winner;
//	}
//	
//	public boolean getTied() {
//		return tied;
//	}
}
