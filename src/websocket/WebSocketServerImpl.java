package websocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import websocket.http.Upgrade;
import websocket.type.WebSocketConfig;
import websocket.type.WebSocketHandle;

public class WebSocketServerImpl implements WebSocketServer {

	private WebSocketHandle handle = new WebSocketHandle();
	
	public WebSocketServerImpl(int port) {
		handle.setServerPort(port);
	}

	@Override
	public void start() {
		try{
			int port = handle.getServerPort();
			ServerSocket server = new ServerSocket(port);
			handle.setServer(server);
			while(true){
				System.out.printf("accept : %s\n",port);
				Socket client = server.accept();
				handle.addClient(client);
				new Runnable(){
					@Override
					public void run() {
						WebSocketWorker worker = Upgrade.hand(Upgrade.getRequest(client));
						worker.setHandle(handle);
						worker.start();
					}
				}.run();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void start(WebSocketConfig option) {

	}

	@Override
	public void stop() {
		try {
			handle.getServer().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
