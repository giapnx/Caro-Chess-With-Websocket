using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OutgoingJoinRoomMessage : Message {

	public int gameId;

	public OutgoingJoinRoomMessage(string type, int gameId)
	{
		this.type = type;
		this.gameId = gameId;
	}
}
