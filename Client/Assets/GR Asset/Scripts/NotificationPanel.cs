using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NotificationPanel : MonoBehaviour {

	public static NotificationPanel instance;

	public UnityEngine.UI.Text NotifyText;

	void Awake()
	{
		instance = this;
	}

	public void SetTextNotification(string text)
	{
		NotifyText.text = text;
	}
}
