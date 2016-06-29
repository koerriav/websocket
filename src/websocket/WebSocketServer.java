package websocket;

import websocket.type.WebSocketConfig;

public interface WebSocketServer {
	public void start();
	public void start(WebSocketConfig option);
	public void stop();
}
