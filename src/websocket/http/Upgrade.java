package websocket.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import websocket.WebSocketWorker;
import websocket.type.WebSocketHandState;

public class Upgrade {

	public static WebSocketHandState getRequest(Socket client) {
		try{
			WebSocketHandState request;
			InputStream in = client.getInputStream();
			byte[] buff = new byte[1024];
			in.read(buff);
			String content = new String(buff).trim();
			request = new WebSocketHandState(client,content);
			return request;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//完成握手
	public static WebSocketWorker hand(WebSocketHandState req) {
		try {
			Socket client = req.getClient();
			String content = req.getContent();
			OutputStream out = client.getOutputStream();
			content = setSecWebSocketAccept(getSecWebSocketKey(content));
			out.write(content.getBytes());
			out.flush();
			return new WebSocketWorker(client);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getSecWebSocketKey(String request) throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader(request));
		List<String> rs = reader.lines().filter(x->{
			return x.matches("Sec-WebSocket-Key:.+");
		}).collect(Collectors.toList());
		if(rs.isEmpty())throw new Exception("客户端连接异常!");
		return rs.get(0).split(":")[1].trim();
	}
	private static String setSecWebSocketAccept(String key) throws Exception {
		String accept;
		String context = key+ "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		accept = new String(Base64.getEncoder().encode((sha1(context.getBytes("UTF-8")))));
		StringBuffer response = new StringBuffer();
		response.append("HTTP/1.1 101 Switching Protocols\r\n");
		response.append("Upgrade: websocket\r\n");
		response.append("Connection: Upgrade\r\n");
		response.append("Sec-WebSocket-Accept: "+accept+"\r\n\r\n");
		return response.toString();
	}
	
	private static byte[] sha1(byte[] data){
        MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(data);
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
    }
}
