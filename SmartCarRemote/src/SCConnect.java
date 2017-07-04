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
	
	private InetAddress ipAddr;
	private int portNumber;
	private Socket remoteSocket = new Socket();
	protected boolean connected = false;	//true if successful socket connection.
	
	public SCConnect() throws IOException {
		this(InetAddress.getByName("192.168.0.1"), 1337);
	}
	
	public SCConnect(String s, int portNumber) throws IOException {
		this(InetAddress.getByName(s), portNumber);
	}
	
	public SCConnect(InetAddress ipAddr, int portNumber) throws IOException {
		this.ipAddr = ipAddr;
		this.portNumber = portNumber;
		try {
			remoteSocket.connect(new InetSocketAddress(ipAddr, portNumber), 1000);
			connected = true;
		} catch (SocketTimeoutException e) {
			System.err.println("Couldn't connect to " +
	                ipAddr.toString() + ":" + portNumber);
			System.err.println("Timed out.");
		} catch (IOException e) {
			System.err.println("Couldn't connect to " +
	                ipAddr.toString() + ":" + portNumber);
            e.printStackTrace();
            System.exit(1);
		}
	}
	
	public InetAddress getAddr() {
		return ipAddr;
	}
	
	public int getPort() {
		return portNumber;
	}
	
}
