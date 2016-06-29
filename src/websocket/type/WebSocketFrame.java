package websocket.type;

public class WebSocketFrame {
	private static final byte FINAL = (byte) 0x80; // 是否是完全帧
	public static final byte CF = (byte) 0x0; // continuation frame
	public static final byte TF = (byte) 0x1; //text frame
	public static final byte BF = (byte) 0x2; // binary frame
	public static final byte CLOSEF = (byte) 0x8; // binary frame
	
	private byte[] data;
//	private int count=1;//待发送帧数
	private byte type=TF;//数据类型
	
	public WebSocketFrame(){
		
	}
	public WebSocketFrame(byte[] data,byte type){
		this.type = type;
		this.data = build(data);
	}
	
	private byte[] build(byte[] payload){
		int len = payload.length;
		System.out.printf("data length : %s\n",len);
		if(len < 126){
			data = new byte[2+len];
			data[0] = (byte) (FINAL + this.type);
			data[1] = (byte) ((byte)len & 0x7f);
			for(int i=0;i<len;i++){
				data[i+2] = payload[i];
			}
		}else if(len <= 65535&&len>=126){
			data = new byte[2+2+len];
			data[0] = (byte) (FINAL + this.type);
			data[1] = (byte) 0x7e;
			data[2] = (byte)(len >> 8 & 0x00ff);
			data[3] = (byte)(len & 0x00ff);
			for(int i=0;i<len;i++){
				data[i+2+2] = payload[i];
			}
		}else{
			data = new byte[2+4+len];
			data[0] = (byte) (FINAL + this.type);
			data[1] = (byte) 0x7f;
			data[2] = (byte)(len >> 24 & 0x00ff);
			data[3] = (byte)(len >> 16 & 0x00ff);
			data[4] = (byte)(len >> 8 & 0x00ff);
			data[5] = (byte)(len & 0x00ff);
			for(int i=0;i<len;i++){
				data[i+2+4] = payload[i];
			}
		}
		return data;
	}
	
	public WebSocketFrame close(){
		this.data = new byte[]{(byte) (FINAL + CLOSEF)};
		return this;
	}
	
	public byte[] getBytes() {
		return this.data;
	}
}