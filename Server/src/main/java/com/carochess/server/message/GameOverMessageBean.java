package com.carochess.server.message;

/**
 * Game over message
 * @author lukas
 */
public class GameOverMessageBean extends MessageBean {
	
	private final String type = "game_over";
	private String result;
	private int score;
	
	// Tied
	public GameOverMessageBean(String r) {
		result = r;
	}
	
	// Win or Lose
	public GameOverMessageBean(String r, int score) {
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
