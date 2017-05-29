using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OutgoingTurnResponseMessage : Message {

	public int 		gameId;
	public string 	player;
	public int 		gridId;

	public OutgoingTurnResponseMessage(string type, int gameId, string player, int gridId)
	{
		this.type = type;
		this.gameId = gameId;
		this.player = player;
		this.gridId = gridId;
	}
}
