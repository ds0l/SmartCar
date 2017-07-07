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
	
	public final short GoLeft = 0;
	public final short GoUp = 1;
	public final short GoRight = 2;
	public final short GoDown = 3;
	public final short StopLeft = 4;
	public final short StopUp = 5;
	public final short StopRight = 6;
	public final short StopDown = 7;
	
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
	
	public void sendCommand (short command) {
		try {
			out.writeShort(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
