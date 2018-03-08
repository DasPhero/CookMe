package services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class WebSocketEndpoint {
    
	private static Map<String, Session> sMap = new HashMap<String, Session>();
	
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId()); 
        sMap.put(session.getId(), session);
    }
    @OnClose
    public void onClose(Session session) {
    	sMap.remove(session.getId());
        System.out.println("onClose::" +  session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);
        
        try {
            for (String key : sMap.keySet()) {
                Session s = sMap.get(key); 
                s.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
}