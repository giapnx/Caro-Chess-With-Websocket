package com.carochess.server.message;

/**
 * Game over message
 * @author lukas
 */
public class OutgoingGameOverMessageBean extends MessageBean {
	
	private final String type = "game_over";
	private String result;
	private int score;
	
	// Tied
	public OutgoingGameOverMessageBean(String r) {
		result = r;
	}
	
	// Win or Lose
	public OutgoingGameOverMessageBean(String r, int score) {
		result = r;
		this.score = score;
	}
	
	public String getType() {
		return type;
	}
	
	public String getResult() {
		return result;
	}
}
