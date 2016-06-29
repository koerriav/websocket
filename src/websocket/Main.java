package websocket;

public class Main {

	public static void main(String[] args) {
		WebSocketServer server = new WebSocketServerImpl(8080);
		server.start();
	}

}