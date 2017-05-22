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
}
