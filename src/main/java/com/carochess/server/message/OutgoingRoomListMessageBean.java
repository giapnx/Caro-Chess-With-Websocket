package com.carochess.server.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carochess.common.RoomNode;

public class OutgoingRoomListMessageBean extends MessageBean {
	
//	Map<Integer, String> rooms = new HashMap<Integer, String>();
	ArrayList<RoomNode> rooms = new ArrayList<RoomNode>();

	public OutgoingRoomListMessageBean(ArrayList<RoomNode> roonList) {
		type = MessageType.LIST_ROOM;
		rooms = roonList;
	}

}

