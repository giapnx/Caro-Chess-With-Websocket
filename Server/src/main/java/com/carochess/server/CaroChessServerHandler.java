package com.carochess.server;

import com.carochess.server.message.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.enterprise.inject.New;
import javax.persistence.criteria.CriteriaBuilder.Case;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import com.carochess.common.RoomNode;
import com.carochess.common.Strings;
import com.carochess.game.Game;
import com.carochess.game.Player;
import com.carochess.game.Game.PlayerLetter;
import com.carochess.server.message.GameOverMessageBean;
import com.carochess.server.message.HandshakeMessageBean;
import com.carochess.server.message.IncomingCreateRoomMessageBean;
import com.carochess.server.message.IncomingTurnResponseMessageBean;
import com.carochess.server.message.MessageType;
import com.carochess.server.message.OutgoingCreateRoomMessageBean;
import com.carochess.server.message.OutgoingTurnResponseMessageBean;
import com.carochess.server.message.OutgoingRoomListMessageBean;
import com.carochess.server.message.TurnMessageBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jboss.netty.handler.codec.http.websocketx.*;

/**
 * Handles a server-side channel for a multiplayer game of Caro Chess.
 */
public class CaroChessServerHandler extends SimpleChannelUpstreamHandler {

	static Map<Integer, Game> games = new HashMap<Integer, Game>();
	static Map<String, Channel> users = new HashMap<String, Channel>();

	private static final String WEBSOCKET_PATH = "/websocket";
	
	private static final int MAX_WAITING_ROOM = 20;
	public static int current_waiting_room = 0;
        
    private WebSocketServerHandshaker handshaker;
    private static ChannelGroup playerGroup;

	/* (non-Javadoc)
	 * 
	 * An incoming message (event). Invoked when either a:
	 * 
	 * - A player navigates to the page. The initial page load triggers an HttpRequest. We perform the WebSocket handshake 
	 * 		and assign them to a particular game.
	 * 
	 * - OR A player clicks on a caro chess square. The message contains who clicked 
	 * 		on which square and which game they're playing.
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object msg = e.getMessage();
		if (msg instanceof HttpRequest) {
			handleHttpRequest(ctx, (HttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	
//	@Override
//	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) {
//	    System.out.println("Bound: " + e.getChannel());
//	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	    System.out.println("Connected: " + e.getChannel().getRemoteAddress());
//	    playerGroup.add(e.getChannel());
//	    System.out.println("player group: "+playerGroup.size());
	}

//	@Override
//	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
//	    System.out.println("Closed: " + e.getChannel().getRemoteAddress());
//	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	    System.out.println("Disconnected: " + e.getChannel().getRemoteAddress());
	}
	

	/**
	 * Handles all HttpRequests. Must be a GET. Performs the WebSocket handshake 
	 * and assigns a player to a game.
	 * 
	 * @param ctx
	 * @param req
	 * @throws Exception
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req)
			throws Exception {

		// Allow only GET methods.
		if (req.getMethod() != HttpMethod.GET) {
			sendHttpResponse(ctx, req, new DefaultHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
			return;
		}

          // Handshake
          WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                  this.getWebSocketLocation(req), null, false);
          this.handshaker = wsFactory.newHandshaker(req);
          if (this.handshaker == null) {
              wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.getChannel());
         } else {
              
              this.handshaker.handshake(ctx.getChannel(), req);
              ctx.getChannel().write(new TextWebSocketFrame(new HandshakeMessageBean().toJson()));
//              initGame(ctx);
         }
	}

	/**
	 * Process turn data from players. Message contains which square they clicked on. Sends turn data to their
	 * opponent.
	 * 
	 * @param ctx
	 * @param frame
	 */
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) 
	{
       // Check for closing frame
         if (frame instanceof CloseWebSocketFrame) {
             this.handshaker.close(ctx.getChannel(), (CloseWebSocketFrame) frame);
             return;
         } else if (frame instanceof PingWebSocketFrame) {
             ctx.getChannel().write(new PongWebSocketFrame(frame.getBinaryData()));
             return;
         } 
         else if (!(frame instanceof BinaryWebSocketFrame)) {
             throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                     .getName()));
         }
        
        String json = ((BinaryWebSocketFrame) frame).getBinaryData().toString(CharsetUtil.UTF_8);
        String type = getMessageType(json);
        
        System.out.println("Json: "+json);
    	
    	Gson gson = new Gson();
    	Game game;
    	Player player;
    	ArrayList<RoomNode> rooms;
    	
    	switch (type) {
		case MessageType.CREATE_ROOM:

			if(current_waiting_room < MAX_WAITING_ROOM)
			{
				IncomingCreateRoomMessageBean message = gson.fromJson(((BinaryWebSocketFrame) frame).getBinaryData().toString(CharsetUtil.UTF_8), IncomingCreateRoomMessageBean.class);
				System.out.println("GameRoom: " + message.getRoom_name());
				game = new Game(message.getRoom_name());
				current_waiting_room++;
				
				// Create a new instance of player and assign their channel for WebSocket communications.
				player = new Player(ctx.getChannel());
				
				// Add the player to the game.
				Game.PlayerLetter first_letter = game.addPlayer(player);
				
				// Add the game to the collection of games.
				games.put(game.getId(), game);
				
				// Send confirmation created game to player with game ID and assigned letter (X) 
				ctx.getChannel().write(new TextWebSocketFrame(new OutgoingCreateRoomMessageBean(game.getId(), game.getRoom_name(), first_letter.toString(), 0).toJson()));
			}
			else
			{
				ctx.getChannel().write(new TextWebSocketFrame(new OutgoingCreateRoomMessageBean(Strings.CREATE_FAIL, Strings.MESSAGE_CREATE_FAIL).toJson()));
			}
			
			
			break;
		case MessageType.FIND_ROOM:
			
			rooms = new ArrayList<RoomNode>();
			// Find an existing game and return it
			for (Game g : games.values()) 
			{
				if (g.getStatus().equals(Game.Status.WAITING)) 
				{
					RoomNode node = new RoomNode(g.getId(), g.getRoom_name());
					rooms.add(node);
				}
	        }
			
			// Send list room to client 
			ctx.getChannel().write(new TextWebSocketFrame(new OutgoingRoomListMessageBean(rooms).toJson()));
			
			break;
			
		case MessageType.JOIN_ROOM:
			
			IncomingJoinRoomMessageBean join_message = gson.fromJson(((BinaryWebSocketFrame) frame).getBinaryData().toString(CharsetUtil.UTF_8), IncomingJoinRoomMessageBean.class);
			
			Game game_join = games.get(join_message.getGameId());
			
			// Create a new instance of player and assign their channel for WebSocket communications.
			Player join_player = new Player(ctx.getChannel());
			
			// Add the player to the game.
			Game.PlayerLetter second_letter = game_join.addPlayer(join_player);
			
			// Add the game to the collection of games.
			games.put(game_join.getId(), game_join);
			
			ctx.getChannel().write(new TextWebSocketFrame(new OutgoingJoinRoomMessageBean(game_join.getId(), game_join.getRoom_name(), second_letter.toString(), game_join.getFirstLetter().toString(), 0, Strings.JOIN_SUCCESS).toJson()));
			
			// If the game has begun we need to inform the players. Send them a "turn" message (either "waiting" or "your_turn")
			if (game_join.getStatus() == Game.Status.IN_PROGRESS) {			
				game_join.getFirstPlayer().getChannel().write(new TextWebSocketFrame(new TurnMessageBean(Strings.YOUR_TURN).toJson()));
				game_join.getSecondPlayer().getChannel().write(new TextWebSocketFrame(new TurnMessageBean(Strings.WAITING).toJson()));
			}
			
			current_waiting_room--;
			
			break;
			
		case MessageType.QUICK_START:
			
			rooms = new ArrayList<RoomNode>();
			// Find an existing game and return it
			for (Game g : games.values()) 
			{
				if (g.getStatus().equals(Game.Status.WAITING))
				{
					RoomNode node = new RoomNode(g.getId(), g.getRoom_name());
					rooms.add(node);
				}
	        }
			
			if(rooms.size() != 0)
			{
				Random generator = new Random();
				int roomNo = generator.nextInt(rooms.size());
				
				Game quick_game_join = games.get(rooms.get(roomNo).roomId);
				
				// Create a new instance of player and assign their channel for WebSocket communications.
				Player quick_otherPlayer = new Player(ctx.getChannel());
				
				// Add the player to the game.
				Game.PlayerLetter quick_otherLetter = quick_game_join.addPlayer(quick_otherPlayer);
				
				// Add the game to the collection of games.
				games.put(quick_game_join.getId(), quick_game_join);
				
				ctx.getChannel().write(new TextWebSocketFrame(new OutgoingJoinRoomMessageBean(quick_game_join.getId(), quick_game_join.getRoom_name(), quick_otherLetter.toString(), quick_game_join.getFirstLetter().toString(), 0, Strings.JOIN_SUCCESS).toJson()));
				
				// If the game has begun we need to inform the players. Send them a "turn" message (either "waiting" or "your_turn")
				if (quick_game_join.getStatus() == Game.Status.IN_PROGRESS) {
					quick_game_join.getFirstPlayer().getChannel().write(new TextWebSocketFrame(new TurnMessageBean(Strings.YOUR_TURN).toJson()));
					quick_game_join.getSecondPlayer().getChannel().write(new TextWebSocketFrame(new TurnMessageBean(Strings.WAITING).toJson()));
				}
				current_waiting_room--;
			}
			else 
			{
				ctx.getChannel().write(new TextWebSocketFrame(new OutgoingJoinRoomMessageBean(Strings.JOIN_FAIL, Strings.MESSAGE_JOIN_FAIL).toJson()));
			}
			
			break;
			
		case MessageType.TURN_RESPONSE:
			
			IncomingTurnResponseMessageBean turn_response_message = gson.fromJson(((BinaryWebSocketFrame) frame).getBinaryData().toString(CharsetUtil.UTF_8), IncomingTurnResponseMessageBean.class);
			System.out.println(turn_response_message.getGameId() + " | "+turn_response_message.getPlayer()+" | "+turn_response_message.getGridId());
			game = games.get(turn_response_message.getGameId());
			Player opponent = game.getOpponent(turn_response_message.getPlayer());
			player = game.getPlayer(PlayerLetter.valueOf(turn_response_message.getPlayer()));
			
			// Mark the cell the player selected.
			game.markCell(turn_response_message.getGridId(), player.getLetter());
			
			// Respond to the opponent in order to update their screen.
			String responseToOpponent = new OutgoingTurnResponseMessageBean(player.getLetter().toString(), turn_response_message.getGridId()).toJson();		
			opponent.getChannel().write(new TextWebSocketFrame(responseToOpponent));
			
			
			// Get the status for the current game.
			boolean winner = game.isPlayerWinner(player.getLetter());
			boolean tied = game.isTied();
			
			
			
			// Respond to the player to let them know they won.
			if (winner) {
//				System.out.println("Win...............");
				game.incrementScoreOf(PlayerLetter.valueOf(turn_response_message.getPlayer()));
				player.getChannel().write(new TextWebSocketFrame(new GameOverMessageBean(Strings.YOU_WIN, game.getScoreOf(PlayerLetter.valueOf(turn_response_message.getPlayer()))).toJson()));
				opponent.getChannel().write(new TextWebSocketFrame(new GameOverMessageBean(Strings.YOU_LOSE, game.getScoreOf(PlayerLetter.valueOf(turn_response_message.getPlayer()))).toJson()));
				
				// Reset status and board game, start a new game
				game.resetGame();
				
			} else if (tied) {
				player.getChannel().write(new TextWebSocketFrame(new GameOverMessageBean(Strings.TIED).toJson()));
			}
			
			break;
			
		case MessageType.EXIT_GAME:
			
			IncomingExitGameMessageBean exit_message = gson.fromJson(((BinaryWebSocketFrame) frame).getBinaryData().toString(CharsetUtil.UTF_8), IncomingExitGameMessageBean.class);
			// Find other Player in game and notify
			Player opponentPlayer = games.get(exit_message.getGameId()).getOpponent(exit_message.getPlayer());
			
			MessageBean msg = new MessageBean();
			msg.type = MessageType.EXIT_GAME;
			opponentPlayer.getChannel().write(new TextWebSocketFrame(msg.toJson()));
			
			// Deleta this game
			games.remove(exit_message.getGameId());
			
			break;

		default:
			break;
		}

		

	}

	private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req,
			HttpResponse res) {
		// Generate an error page if response status code is not OK (200).
		if (res.getStatus().getCode() != 200) {
			res.setContent(ChannelBuffers.copiedBuffer(res.getStatus()
					.toString(), CharsetUtil.UTF_8));
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.getChannel().write(res);
		if (!isKeepAlive(req) || res.getStatus().getCode() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	private String getMessageType(String jsonLine)
	{
		JsonElement jelement = new JsonParser().parse(jsonLine);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    return jobject.get("type").getAsString();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	private String getWebSocketLocation(HttpRequest req) {
		return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
	}
}