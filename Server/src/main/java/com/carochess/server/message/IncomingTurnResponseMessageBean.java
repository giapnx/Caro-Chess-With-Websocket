package com.carochess.server.message;

/**
 * Represents incoming message from the user
 * @author lukasz madon
 */
public class IncomingTurnResponseMessageBean extends MessageBean {
	
    private int gameId;
	private String player;
	private int gridId;
	
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
	public int getGridId() {
		return gridId;
	}
	public void setGridId(int gridId) {
		this.gridId = gridId;
	}
//	public int getGridIdAsInt() {
//		return Integer.valueOf(gridId.substring(5));
//	}
}
