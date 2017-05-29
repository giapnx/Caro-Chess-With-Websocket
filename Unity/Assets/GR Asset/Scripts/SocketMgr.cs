using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using UnityEngine.UI;

public class SocketMgr : MonoBehaviour {

	public InputField RemoteInput;

	public static SocketMgr instance;

	WebSocket ws;

	BoardGameMgr BoardGameInstance;
	RoomInfoMgr RoomInfoInstance;

	void Awake()
	{
		instance = this;
	}

	void Start()
	{
		BoardGameInstance = BoardGameMgr.instance;
		RoomInfoInstance = RoomInfoMgr.instance;
	}

	public void StartWebSocket()
	{
		StartCoroutine (StartWebSocketCorou ());
	}

	IEnumerator StartWebSocketCorou () {
//		ws = new WebSocket(new Uri("ws://192.168.0.104:9000/websocket"));
		ws = new WebSocket(new Uri("ws://"+RemoteInput.text+":9000/websocket"));
		yield return StartCoroutine(ws.Connect());

		UIManager.Instance.ShowPage ("MainMenuPanel");

		while (true)
		{
			string reply = ws.RecvString();
			if (reply != null)
			{
				print ("Receive:" + reply);
				IncomingMessage incomingMessage = JsonUtility.FromJson<IncomingMessage> (reply);

				switch (incomingMessage.type) 
				{
				case GR_MessageType.HANDSHAKE:
					print ("Handshake success !");

					break;

				case GR_MessageType.CREATE_ROOM:
					
					if (incomingMessage.status != Strings.CREATE_FAIL)
					{
						BoardGameInstance.gameId = incomingMessage.gameId;
						BoardGameInstance.player = incomingMessage.player;
						if (BoardGameInstance.player == Player.X)
							BoardGameInstance.opponent = Player.O;
						else
							BoardGameInstance.opponent = Player.X;

						RoomInfoInstance.SetPlayerLetter ();
						RoomInfoInstance.SetStatusPlayer (incomingMessage.score);

						RoomInfoInstance.SetOppoLetter ();
						RoomInfoInstance.SetStatusOpponent (incomingMessage.score);

						RoomInfoInstance.SetGameRoom (incomingMessage.gameName);
						BoardGameInstance.ResetBoardGame ();

						UIManager.Instance.ShowPage ("PlayPanel");
//						RoomInfoInstance.SetStatusOpponent (incomingMessage.score);
					}
					else
					{
						NotificationPanel.instance.SetTextNotification (Strings.CREATE_FAIL_MSG);

						UIManager.Instance.ShowPage ("NotifycationPanel");
					}

					break;

				case GR_MessageType.LIST_ROOM:

					if(incomingMessage.rooms.Count == 0)
					{
						NotificationPanel.instance.SetTextNotification (Strings.JOIN_FAIL_MSG);

						UIManager.Instance.ShowPage ("NotifycationPanel");

					}
					else
					{
						RoomListMgr.instance.rooms = incomingMessage.rooms;

						UIManager.Instance.ShowPage ("ListRoomPanel");
					}

					break;

				case GR_MessageType.JOIN_ROOM:

					if (incomingMessage.status == Strings.JOIN_FAIL) 
					{
						NotificationPanel.instance.SetTextNotification (Strings.JOIN_FAIL_MSG);
						UIManager.Instance.ShowPage ("NotifycationPanel");
					}
					else
					{
						BoardGameInstance.gameId = incomingMessage.gameId;
						BoardGameInstance.player = incomingMessage.player;
						BoardGameInstance.opponent = incomingMessage.opponent;

						RoomInfoInstance.SetPlayerLetter ();
						RoomInfoInstance.SetStatusPlayer (incomingMessage.score);

						RoomInfoInstance.SetOppoLetter ();
						RoomInfoInstance.SetStatusOpponent (incomingMessage.score);

						RoomInfoInstance.SetGameRoom (incomingMessage.gameName);
						BoardGameInstance.ResetBoardGame ();

						UIManager.Instance.ShowPage ("PlayPanel");
					}

					break;

				case GR_MessageType.TURN:

					if (incomingMessage.turn == Strings.YOUR_TURN)
						BoardGameInstance.isPlayerTurn = true;
					else
						BoardGameInstance.isPlayerTurn = false;

					RoomInfoInstance.SetTurnStatus (BoardGameInstance.isPlayerTurn);
					break;

				case GR_MessageType.TURN_RESPONSE:

					BoardGameInstance.SetStatusTurnEnemy (incomingMessage.opponent, incomingMessage.gridId);

					if(!incomingMessage.winner)
					{
						BoardGameInstance.isPlayerTurn = true;
						RoomInfoInstance.SetTurnStatus (BoardGameInstance.isPlayerTurn);
					}
					else
					{
						// Set score + reset board game
						RoomInfoInstance.oppoScore++;
						RoomInfoInstance.UpdateScoreOpponent ();

						BoardGameInstance.ResetBoardGame ();
					}

					break;

				case GR_MessageType.GAMEOVER:

					if(incomingMessage.result == Strings.YOU_WIN)
					{
						UIManager.Instance.ShowPage ("WinPanel");
						RoomInfoInstance.playerScore = incomingMessage.score;
						RoomInfoInstance.UpdateScorePlayer ();
					}
					else if(incomingMessage.result == Strings.YOU_LOSE)
					{
						UIManager.Instance.ShowPage ("LosePanel");
						RoomInfoInstance.oppoScore = incomingMessage.score;
						RoomInfoInstance.UpdateScoreOpponent ();
					}
					else
					{
						BoardGameInstance.ResetBoardGame ();
					}

					break;

				case GR_MessageType.EXIT_GAME:

					print ("Exit Game");
					NotificationPanel.instance.SetTextNotification (Strings.EXIT_MSG);
					UIManager.Instance.ShowPage ("NotifycationPanel");
					break;

				default:
					break;
				}

			}
			if (ws.error != null)
			{
				Debug.LogError ("Error: "+ws.error);
				break;
			}
			yield return 0;
		}
		ws.Close();
	}

	public void Send(Message outMessage)
	{
		ws.SendString (JsonUtility.ToJson (outMessage));
	}

	public void CloseWS()
	{
		ws.Close ();
	}
}

[Serializable]
public class IncomingMessage
{
	public string 	type;

	public string 	turn;

	public string 	status;

	public List<RoomNode> rooms;

	public string 	gameName;
	public int 		gameId;
	public string 	player;
	public int 		gridId;
	public int 		score;

	public string 	opponent;
	public bool		winner;
	public bool		tied;

	public string	result;

	public string 	message;
}

[Serializable]
public class RoomNode
{
	public int roomId;
	public string roomName;
}

[Serializable]
public class OutgoingMessage
{
	public string	type;

	public int 		gameId;
	public string 	player;
	public int 		gridId;
}
