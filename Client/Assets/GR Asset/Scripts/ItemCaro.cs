using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ItemCaro : MonoBehaviour {

	public static ItemCaro instance;

	public int gridId;
	public Text mark;

	void Awake()
	{
		instance = this;
	}

	// Use this for initialization
	void Start () {
		mark.text = "";
	}
	
	public void OnClickItem()
	{
		// player click
		if (BoardGameMgr.instance.isPlayerTurn && mark.text == "") 
		{
			SetStatusItem (BoardGameMgr.instance.player);

			OutgoingTurnResponseMessage outMess = new OutgoingTurnResponseMessage (
															GR_MessageType.TURN_RESPONSE, 
															BoardGameMgr.instance.gameId, 
															BoardGameMgr.instance.player, 
															gridId);

			SocketMgr.instance.Send (outMess);

			BoardGameMgr.instance.isPlayerTurn = false;
		}

	}

	public void SetStatusItem(string player)
	{
		if (mark.text == "") 
		{
			if (player == Player.X) 
			{
				mark.text = "X";
				mark.color = new Color32 (0, 255, 0, 255);
			}
			else
			{
				mark.text = "O";
				mark.color = new Color32 (255, 255, 0, 255);
			}
		}
	}

	public void ResetStatusItem()
	{
		mark.text = "";
	}
}

public class Player
{
	public const string X = "X";
	public const string O = "O";
}