using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ResultMgr : MonoBehaviour {

	BoardGameMgr BoardGameInstance;

	void Start()
	{
		BoardGameInstance = BoardGameMgr.instance;
	}

	public void Again()
	{
		BoardGameInstance.ResetBoardGame ();
	}

	public void BackToMenu()
	{
		OutgoingExitGameMessage outMsg = new OutgoingExitGameMessage (BoardGameInstance.gameId, BoardGameInstance.player);

		SocketMgr.instance.Send (outMsg);

		UIManager.Instance.ShowPage ("MainMenuPanel");
	}
}
