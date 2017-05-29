using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OutgoingCreateRoomMessage : Message {

	// create room
	public string	room_name;

	public OutgoingCreateRoomMessage(string type, string roomName)
	{
		this.type = type;
		this.room_name = roomName;
	}
}
