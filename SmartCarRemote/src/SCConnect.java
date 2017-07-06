/**
 * 
 */

/**
 * @author David
 *
 */

import java.io.*;
import java.net.*;

public class SCConnect extends Socket {
	
	public SCConnect() {
		super();
	}
	public SCConnect (String address, int port) throws IOException {
		super(address, port);
	}

	public void sendLeft () {
		return;
	}
	
	
}
