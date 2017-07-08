/**
 * 
 */

/**
 * @author David
 *
 */

import java.io.*;
import java.net.*;

public class SCConnect {
	
	Socket socket;
	DataOutputStream out;
	boolean connected = false;
	
	public static final byte GoLeft = 0;
	public static final byte GoUp = 1;
	public static final byte GoRight = 2;
	public static final byte GoDown = 3;
	public static final byte StopLeft = 4;
	public static final byte StopUp = 5;
	public static final byte StopRight = 6;
	public static final byte StopDown = 7;
	
	public SCConnect() {
		super();
	}

	public void connect(String addr, int port) {
		try {
			socket = new Socket(addr, port);
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		connected = true;
	}
	
	public void disconnect() {
		try {
			out.close();
			socket.close();
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		socket = null;
		connected = false;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void sendCommand (byte command) {
		try {
			out.writeByte(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
