using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class RoomListMgr : MonoBehaviour {

	public static RoomListMgr instance;

	public Transform Content;
	public GameObject RoomItemPref;
	public Button JoinBtn;

	[HideInInspector]
	public int selectedId = 0;

	public List<RoomNode> rooms;

	void Awake()
	{
		instance = this;
	}
	// Use this for initialization
	void OnEnable ()
	{
		// List room
		if (rooms.Count != 0) {
			print (rooms.Count);
			foreach (var item in rooms) {
				var node = Instantiate (RoomItemPref) as GameObject;
//				node.transform.position = Vector3.one;
				node.transform.SetParent (Content);
				node.GetComponent <RectTransform>().localScale = Vector3.one;
				node.GetComponent <RoomItem> ().SetInfoRoom (item.roomId, item.roomName);
			}
		} else
			print ("Room count: " + rooms.Count);

		JoinBtn.interactable = false;
	}

	void OnDisable()
	{
		// reset item on room list
		foreach (Transform item in Content) {
			Destroy (item.gameObject);
		}
	}

	public void OnClickBack()
	{
		
	}

	public void OnClickJoinRoom()
	{
		if(selectedId != 0)
		{
			// setup value for msg and send to server
			OutgoingJoinRoomMessage outMsg = new OutgoingJoinRoomMessage (GR_MessageType.JOIN_ROOM, selectedId);

			SocketMgr.instance.Send (outMsg);
		}
		else
		{
			print ("have erro gameId");
		}

	}

}
