package server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
 
@ServerEndpoint("/chat") 
public class PushServer {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
        sendMessageToAll("User has connected");
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sessions.add(session);
    }
	
	@OnMessage
	public void onMessage(String message, Session session){
		System.out.println("Message from " + session.getId() + ": " + message);
	    sendMessageToAll(message);
	}

    @OnClose
    public void onClose(Session session){
        System.out.println("Chat " +session.getId()+" has ended");
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable thr) {
    	System.out.println("Chat " + session.getId() + " has encountered an error: " + thr.getMessage());
    }
    
	private void sendMessageToAll(String message){
        for(Session s : sessions){
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
 }
