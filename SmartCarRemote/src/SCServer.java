/**
 * 
 */

/**
 * @author David
 *
 */

import java.net.*;
import java.io.*;

public class SCServer {

	int port;
	
	public SCServer() {
		this.port = 11337;
	}
	public SCServer(int port) {
		this.port = port;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

        SCServer serv = new SCServer(11337);
        serv.init();
	}
	
	
	public void init() {
        
        try (
            ServerSocket serverSocket =
                new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();     
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        
        System.out.println("Exited");
	}

}
