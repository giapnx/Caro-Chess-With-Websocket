using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CreateRoomPanel : MonoBehaviour {

	public InputField RoomNameInputField;

	public void CreateGameRoom()
	{
		OutgoingCreateRoomMessage outMess = new OutgoingCreateRoomMessage (GR_MessageType.CREATE_ROOM, RoomNameInputField.text);

		SocketMgr.instance.Send (outMess);
		RoomInfoMgr.instance.SetGameRoom (RoomNameInputField.text);
	}

}
