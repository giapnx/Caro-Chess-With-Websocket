// Constants - Status Updates
var STRATEGIZING_STATUS = "Your opponent's turn.";
var WAITING_STATUS = "Waiting for an opponent.";
var YOUR_TURN_STATUS = "It's your turn!";
var YOU_WIN_STATUS = "WINNER!";
var TIED_STATUS = "The game is tied.";
var WEBSOCKET_CLOSED_STATUS = "Connection has been closed.";

// Board game
var ROW = 15;
var COL = 15;

// Constants - Game
var PLAYER_O = "O";
var PLAYER_X = "X";

// Constants - Incoming message types
var MESSAGE_HANDSHAKE = "handshake";
var MESSAGE_OPPONENT_UPDATE = "response";
var MESSAGE_TURN_INDICATOR = "turn";
var MESSAGE_GAME_OVER = "game_over";
var MESSAGE_RESET_GAME = "reset_game";

// Constants - Message turn indicator types
var MESSAGE_TURN_INDICATOR_YOUR_TURN = "YOUR_TURN";
var MESSAGE_TURN_INDICATOR_WAITING = "WAITING";

// Constants - Game over message types
var MESSAGE_GAME_OVER_YOU_WIN = "YOU_WIN";
var MESSAGE_GAME_OVER_TIED = "TIED";

// Constants - WebSocket URL
// var WEBSOCKET_URL = "ws://192.168.1.72:9000/websocket";
var WEBSOCKET_URL = "ws://192.168.0.104:9000/websocket";

// Variables
var player;
var opponent;
var gameId;
var player_score;
var opponent_score;
var yourTurn = false;
var playerXGoFirst = true;

// WebSocket connection
var ws;

$(document).ready(function() {
    
    if (typeof MozWebSocket != "undefined") { // (window.MozWebSocket)
     ws = new MozWebSocket(
        );
    } else if (window.WebSocket) {
        ws = new WebSocket(WEBSOCKET_URL);
    } else {
        alert('ERROR: This browser does not support sWebSockets');
    }	

    // Create board game with row * col cells
    createBoardGame(ROW,COL);

	// bind to .grid class 
	$(".grid").click(function () {
            // Only process clicks if it's your turn.
            // if (yourTurn == true) { 
            if (yourTurn == true && $(this).html() == "&nbsp;") { 
    	       // Stop processing clicks and invoke sendMessage(). 
                yourTurn = false;
                sendMessage(this.id);
        	    // Add the X or O to the game board and update status.
    	      $("#" + this.id).addClass(player);
    	      $("#" + this.id).html(player);	    	  
    	      $('#status').text(STRATEGIZING_STATUS);  
    	    }
    });	

    $(".grid").mouseover(function() {
    	$(this).css("background-color", "rgba(107, 107, 107, 0.2)");
    });

    $(".grid").mouseout(function() {
    	// $(this).css("background-color", "white");
    	$(this).removeAttr("style");
    });
    
    ws.onopen = function(event) { 
    	$('#status').text(WAITING_STATUS); 
        // alert('onopen');
    }
    
    // Process turn message ("push") from the server.
    ws.onmessage = function(event) 
    {
        console.log(event);
        // alert('onmessage');
        var message = jQuery.parseJSON(event.data);
        // Process the handshake response when the page is opened
        if (message.type === MESSAGE_HANDSHAKE)
        {
            gameId = message.gameId;
            player = message.player;
            player_score = 0;
            opponent_score = 0;

            if (player === PLAYER_X) 
            {
                opponent = PLAYER_O;
            } 
            else 
            {
                opponent = PLAYER_X;
            }

            showGameID(gameId);
            setScore(player,player_score);
            setScore(opponent,opponent_score);
        }

        // Process your opponent's turn data.
        if (message.type === MESSAGE_OPPONENT_UPDATE) 
        {
            // Show their turn info on the game board.
            $("#" + message.gridId).addClass(message.opponent);
            $("#" + message.gridId).html(message.opponent);

            // Switch to your turn.
            if (message.winner == true) 
            {
                $('#status').text(message.opponent + " is the winner!");

                opponent_score = opponent_score + 1;
                setScore(opponent, opponent_score);

                setTimeout(function() { 
                    alert(message.opponent + " is the winner!"); 
                    resetBoardGame();

                    setNextTurn();
                }, 300);
            } 
            else if (message.tied == true) 
            {
                $('#status').text(TIED_STATUS);   	   	 			
            } 
            else 
            {
                yourTurn = true;
                $('#status').text(YOUR_TURN_STATUS);    	   	 			
            }
        }   	 	
        /* The initial turn indicator from the server. Determines who starts
            the game first. Both players wait until the server gives the OK
            to start a game. */
        if (message.type === MESSAGE_TURN_INDICATOR) 
        {
            if (message.turn === MESSAGE_TURN_INDICATOR_YOUR_TURN) 
            {
                yourTurn = true;
                $('#status').text(YOUR_TURN_STATUS);    	 			
            } 
            else if (message.turn === MESSAGE_TURN_INDICATOR_WAITING) 
            {
                $('#status').text(STRATEGIZING_STATUS);    	 					    	
            }
        }

        /* The server has determined you are the winner and sent you this message. */
        if (message.type === MESSAGE_GAME_OVER) 
        {
            if (message.result === MESSAGE_GAME_OVER_YOU_WIN) 
            {
                $('#status').text(YOU_WIN_STATUS);
                player_score = player_score + 1;
                setScore(player, player_score);

                setTimeout(function() { 
                    alert(YOU_WIN_STATUS);
                    resetBoardGame();

                    setNextTurn();
                }, 300);
            }
            else if (message.result === MESSAGE_GAME_OVER_TIED) 
            {
                $('#status').text(TIED_STATUS);
            }
        }
	
    }

    ws.onclose = function(event) { 
                //alert('close');
            $('#status').text(WEBSOCKET_CLOSED_STATUS); 
    } 		
});

// Send your turn information to the server.
function sendMessage(id) {
	var message = {gameId: gameId, player: player, gridId:id};
	console.log(message);
	var encoded = $.toJSON(message);
	ws.send(encoded);
}

// Reset board game
function resetBoardGame()
{
    console.log("reset board game");
    for (var i = 1; i <= ROW*COL; i++)
    {
        $("#grid_" + i).removeClass("X").removeClass("O").html("&nbsp;");
    }
}

function setScore(player, score)
{
    $("#score_"+player).html("Score: " + score);
}

function showGameID(gameId)
{
    $(".gameID").html("GameID: " + gameId);
}

function createBoardGame(row, col)
{
    console.log("createBoardGame");
    for (var i = row*col; i >= 1; i--) 
    {
        $(".centreinside").prepend("<div class=\"grid\" id=\"grid_" + i +"\">&nbsp;</div>");
    }
}

// check current turn and set next turn
function setNextTurn()
{
    if ((playerXGoFirst === true && player === PLAYER_O) || (playerXGoFirst === false && player === PLAYER_X)) 
    {
        yourTurn = true;
        $('#status').text(YOUR_TURN_STATUS);
    } else 
    {
        yourTurn = false;
        $('#status').text(STRATEGIZING_STATUS);  
    }
    
    playerXGoFirst = !playerXGoFirst;
}



