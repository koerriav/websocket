package websocket.type;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WebSocketHandle {
	
	private int serverPort;
	private ServerSocket server;
	private List<Socket> clients = new ArrayList<Socket>();
	
	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void addClient(Socket client){
		clients.add(client);
	}

	public List<Socket> getClients() {
		return this.clients;
	}
}
