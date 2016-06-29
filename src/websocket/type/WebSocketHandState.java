package websocket.type;

import java.net.Socket;

public class WebSocketHandState {
	private Socket client;
	private String content;
	
	public WebSocketHandState(Socket client, String content) {
		super();
		this.client = client;
		this.content = content;
	}
	public Socket getClient() {
		return client;
	}
	public void setClient(Socket client) {
		this.client = client;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
