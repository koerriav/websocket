package websocket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import websocket.type.WebSocketFrame;
import websocket.type.WebSocketHandle;

public class WebSocketWorker extends Thread {
	private Socket client;
	private WebSocketHandle handle;
	
	@Override
	public void run() {
		System.out.printf("running... %s\n",client);
		try {
			BufferedInputStream in = new BufferedInputStream(client.getInputStream());
			byte size = 0;
			boolean mask = false;
			byte type = 0;
			while(true){
				int count = 0;
				byte b = (byte) in.read();
				count++;
				System.out.printf("final : %s\n",(byte)(b & 0x80)==(byte)0x80);
				System.out.printf("type : %s\n", bin((byte)(b & 0xf)));
				type  = (byte)(b & 0xf);
				if(type==WebSocketFrame.CLOSEF){
					System.out.printf("close : %s\n", bin((byte)(b & 0xf)));
				    send(new WebSocketFrame().close());
				    break;
				}
				b = (byte) in.read();
				count++;
				System.out.printf("mask : %s\n", (byte)(b & 0x80)==(byte)0x80);
				System.out.printf("size : %s\n", (byte)(b & 0x7f));
				mask  = (byte)(b & 0x80)==(byte)0x80;
				if(!mask)break;
				size = (byte)(b & 0x7f);
				byte[] key = new byte[4];
				count+=in.read(key);
				System.out.printf("key : %s\n", bin(key));
				 //对数据和掩码做异或运算
				byte[] data = new byte[size];
				count+=in.read(data);
			    for(int i=0;i<size;i++){
			    	data[i]=(byte) (data[i] ^ key[i%4]);
			    }
			    System.out.printf("recv : %s, total : %s\n",new String(data),count);
			    WebSocketFrame frame = new WebSocketFrame(data,WebSocketFrame.TF);
			    send(frame);
			}
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public WebSocketWorker(Socket client){
		this.client = client;
	}

	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(WebSocketFrame frame) {
		try {
			for(Socket client : handle.getClients()){
				OutputStream out = client.getOutputStream();
				out.write(frame.getBytes());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}
	
	private String bin(byte[] data){
		String str="";
		for(byte b : data){
			str +=Integer.toBinaryString(Byte.toUnsignedInt(b));
		}
		return str;
	}
	
	private String bin(byte data){
		return Integer.toBinaryString(Byte.toUnsignedInt(data));
	}

	public WebSocketHandle getHandle() {
		return handle;
	}

	public void setHandle(WebSocketHandle handle) {
		this.handle = handle;
	}
}
