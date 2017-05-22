package com.carochess.server.message;

public class OutgoingJoinRoomMessageBean extends MessageBean {
	
	public int gameId;
	public String gameName;
	public String status;
	public String player;
	public String opponent;
	public int 	score;
	public String message;

	public OutgoingJoinRoomMessageBean(int gameId, String gameName, String player, String opponent, int score, String status) {
		type = MessageType.JOIN_ROOM;
		this.gameId 	= gameId;
		this.gameName 	= gameName;
		this.player 	= player;
		this.opponent 	= opponent;
		this.score		= score;
		this.status 	= status;
	}
	
	// Quick join fail
	public OutgoingJoinRoomMessageBean(String status, String msgError) {
		type = MessageType.JOIN_ROOM;
		this.status = status;
		this.message = msgError;
	}

}
