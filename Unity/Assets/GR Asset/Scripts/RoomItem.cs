using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class RoomItem : MonoBehaviour {

	public Text RoomName;

	public int id = 0;

	public void SetInfoRoom(int roomId, string roomName)
	{
		id = roomId;
		RoomName.text = roomName;
	}

	public void OnClickItem()
	{
		// active button join and set selectedId
		RoomListMgr.instance.JoinBtn.interactable = true;
		RoomListMgr.instance.selectedId = id;
	}
}
