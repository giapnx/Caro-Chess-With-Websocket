package com.carochess.server.message;

public class OutgoingQuickStartMessageBean extends MessageBean {
	
	public int gameId;
	public String gameName;
	public String status;
	public String player;

	public OutgoingQuickStartMessageBean(int gameId, String gameName, String player, String status) {
		type = MessageType.QUICK_START;
		this.gameId = gameId;
		this.gameName = gameName;
		this.player = player;
		this.status = status;
	}

	public OutgoingQuickStartMessageBean() {
		// TODO Auto-generated constructor stub
	}

}
