using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GR_MessageType : MonoBehaviour {

	// Incoming
	public const string HANDSHAKE 	= "handshake";
	public const string LIST_ROOM 	= "list_room";
	public const string JOIN_ROOM 	= "join_room";
	public const string TURN 		= "turn";
	public const string GAMEOVER 	= "game_over";

	// Outgoing
	public const string CREATE_ROOM	= "create_room";
	public const string FIND_ROOM 	= "find_room";
	public const string TURN_RESPONSE 	= "turn_response";
	public const string QUICK_START 	= "quick_start";
}
