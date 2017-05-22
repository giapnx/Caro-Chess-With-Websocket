package com.carochess.server.message;

public class IncomingJoinRoomMessageBean extends MessageBean {
	
	private int gameId;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

}
