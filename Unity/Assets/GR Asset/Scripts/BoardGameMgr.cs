using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BoardGameMgr : MonoBehaviour {

	public static BoardGameMgr instance;

	public int gameId;
	public string player;
	public string opponent;

	public bool isPlayerTurn = false;
	public bool isFirstTurn = false;

	void Awake()
	{
		instance = this;
	}
	// Use this for initialization
	void Start () {
		SetGridIdForAllItem ();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	void SetGridIdForAllItem()
	{
		int id = 1;

		foreach (Transform child in transform) {
			child.gameObject.GetComponent <ItemCaro>().gridId = id;
			id++;
		}
	}

	public void SetStatusTurnEnemy(string oppenent, int gridId)
	{
		transform.GetChild (gridId-1).GetComponent <ItemCaro>().SetStatusItem (oppenent);
	}

	public void ResetBoardGame()
	{
		foreach (Transform child in transform) {
			child.gameObject.GetComponent <ItemCaro> ().ResetStatusItem ();
		}
	}



}
