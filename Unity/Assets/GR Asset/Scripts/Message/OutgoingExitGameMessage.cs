using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OutgoingExitGameMessage : Message {

	public int 		gameId;
	public string 	player;

	public OutgoingExitGameMessage(int gameId, string player)
	{
		this.type = GR_MessageType.EXIT_GAME;
		this.player = player;
		this.gameId = gameId;
	}
}
