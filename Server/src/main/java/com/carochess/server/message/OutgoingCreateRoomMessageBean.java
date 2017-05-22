package com.carochess.server.message;

import com.carochess.common.Strings;

public class OutgoingCreateRoomMessageBean extends MessageBean {
	
	private String status;
	private int gameId;
	private String gameName;
	private String player;
	private int score;
	private String message;
	
	// create success
	public OutgoingCreateRoomMessageBean(int gameId, String gameName, String player, int score)
	{
		type = MessageType.CREATE_ROOM;
		this.status = Strings.CREATE_SUCCESS;
		this.gameId = gameId;
		this.setGameName(gameName);
		this.player = player;
		this.setScore(score);
	}
	
	public OutgoingCreateRoomMessageBean(String status, String messError)
	{
		type = MessageType.CREATE_ROOM;
		this.status = status;
		this.message = messError;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
