package com.carochess.server.message;

import com.google.gson.Gson;

/**
 * Represents handshake message
 * @author lukasz madon
 */
public class HandshakeMessageBean extends MessageBean {
	
	private final String type = "handshake";
	private int gameId;
	private String player;
	private int score;

	public HandshakeMessageBean(int gameId, String player, int score) {
		this.gameId = gameId;
		this.player = player;
		this.score = score;
	}

	public String getType() {
		return type;
	}
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
