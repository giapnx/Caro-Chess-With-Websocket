package com.carochess.server.message;

/**
 * Represents a message of Tic Tac Toe turn
 * @author lukasz madon
 */
public class OutgoingTurnMessageBean extends MessageBean {
	
	
	private final String type = "turn";
	private String turn;

	public OutgoingTurnMessageBean(String t) {
		turn = t;
	}

	public String getType() {
		return type;
	}
	
	public String getTurn() {
		return turn;
	}	
	
}
