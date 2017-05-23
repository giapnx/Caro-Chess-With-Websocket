package com.carochess.server.message;

public class IncomingExitGameMessageBean extends MessageBean {
	
	private int gameId;
	private String player;

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



}
