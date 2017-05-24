using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Strings {

	// Game over status
	public const string YOU_WIN		= "you_win";
	public const string YOU_LOSE	= "you_lose";
	public const string TIED		= "tied";

	// Turn
	public const string WAITING		= "waiting";
	public const string YOUR_TURN 	= "your_turn";

	// Join status
	public const string JOIN_SUCCESS 	= "join_success";
	public const string JOIN_FAIL 		= "join_fail";

	// Create status
	public const string CREATE_SUCCESS 	= "create_success";
	public const string CREATE_FAIL 	= "create_fail";

	// message
	public const string EXIT_MSG 	= "The opponent has left the game. Press the OK button to return to MainMenu";
	public const string CREATE_FAIL_MSG 	= "Can not create more rooms.\nPlease join in an available room";
	public const string JOIN_FAIL_MSG 	= "Currently no room available.\nPlease create a new room or try to find it again";

}
