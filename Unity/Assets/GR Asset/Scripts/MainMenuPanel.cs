using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MainMenuPanel : MonoBehaviour {

	public void FindGameRoom()
	{
		Message outMsg = new Message ();
		outMsg.type = GR_MessageType.FIND_ROOM;

		SocketMgr.instance.Send (outMsg);
	}

	public void QuickStart()
	{
		Message outMsg = new Message ();
		outMsg.type = GR_MessageType.QUICK_START;

		SocketMgr.instance.Send (outMsg);
	}

}


