using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class RoomInfoMgr : MonoBehaviour {

	public static RoomInfoMgr instance;

	public Text GameRoomText;

	// player
//	public Text playerName;
	public Text PlayerLetterText;
	public Text playerScoreText;
	[HideInInspector]
	public int playerScore = 0;

	// opponent
//	public Text oppoName;
	public Text OppoLetterText;
	public Text oppoScoreText;
	[HideInInspector]
	public int oppoScore = 0;


	void Awake()
	{
		instance = this;
	}

	public void SetStatusPlayer(int score)
	{
//		playerName.text = name;
		playerScore = score;
		playerScoreText.text = score.ToString ();
	}

	public void SetStatusOpponent(int score)
	{
//		oppoName.text = name;
		oppoScore = score;
		oppoScoreText.text = oppoScore.ToString ();
	}

	public void UpdateScorePlayer()
	{
		playerScoreText.text = playerScore.ToString ();
	}

	public void UpdateScoreOpponent()
	{
		oppoScoreText.text = oppoScore.ToString ();
	}

	public void SetGameRoom(string name)
	{
		GameRoomText.text = name;
	}

	public void SetPlayerLetter()
	{
		PlayerLetterText.text = "You\n" + BoardGameMgr.instance.player;
	}

	public void SetOppoLetter()
	{
		OppoLetterText.text = "Enemy\n" + BoardGameMgr.instance.opponent;
	}
}
