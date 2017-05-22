package com.carochess.server.message;

public class IncomingCreateRoomMessageBean extends MessageBean {
	
	private String room_name;

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

}
