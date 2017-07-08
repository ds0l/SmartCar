

/**
 * @author David
 *
 */

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import java.awt.event.*;

public class SCWindow extends JFrame {
	
	/**
	 * This is the UI Window. It has two text boxes (host and port) and a 
	 * button (connect). When the user presses the connect button, SCWindow 
	 * creates a new SCConnect object that will send commands to the server.
	 */
	
	private static final long serialVersionUID = 1L;
	static final String newline = System.getProperty("line.separator");
	KeyAdapter keyListener;
	SCConnect socketControl;
	
	JTextField hostField, portField;
	JButton connectButton, disconnectButton;
	

	public SCWindow(String name) {
		super(name);
		keyListener = new KeyListen();
		socketControl = new SCConnect();
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		SCWindow frame = new SCWindow("Smart Car Remote");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Trippy moment when a static function calls an instance function.
        frame.addComponentsToPane();
 
        //Display the window.
        frame.pack();
        //frame.setSize(320, 100);
        frame.setVisible(true);
	}
	
	private void addComponentsToPane() {
		hostField = new JTextField("192.168.0.1");
		hostField.setColumns(9);
		
		portField = new JTextField("11337");
		portField.setColumns(4);
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ButtonListen());
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(hostField)
				.addComponent(portField)
				.addComponent(connectButton)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(hostField)
					.addComponent(portField)
					.addComponent(connectButton))
		);
	}
	
	private void connect() {
		// Change the UI.
		//hostField.setVisible(false);
		//portField.setVisible(false);
		hostField.setEditable(false);
		portField.setEditable(false);
		
		socketControl.connect(hostField.getText(),
				Integer.parseInt(portField.getText()));
		
		connectButton.setText("Disconnect");

    	addKeyListener(keyListener);
    	requestFocusInWindow();
	}
	
	private void disconnect() {
		socketControl.disconnect();
		
		// Change the UI.
		//hostField.setVisible(true);
		//portField.setVisible(true);
		hostField.setEditable(true);
		portField.setEditable(true);
		connectButton.setText("Connect");
		removeKeyListener(keyListener);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
private class KeyListen extends KeyAdapter {
	// Keyboard listener.
	boolean heldLeft = false, 
			heldUp = false, 
			heldRight = false, 
			heldDown = false;
	
	/** Key Pressed */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
        case 37:	// Left Arrow
        	if (!heldLeft) {
        		System.out.println("Pressed Left ");
        		heldLeft = true;
        		socketControl.sendCommand(SCConnect.GoLeft);
        	}
        	break;
        case 38:	// Up Arrow
        	if (!heldUp) {
	        	System.out.println("Pressed Up ");
	        	heldUp = true;
	        	socketControl.sendCommand(SCConnect.GoUp);
        	}
        	break;
        case 39:	// Right Arrow
        	if (!heldRight) {
	        	System.out.println("Pressed Right ");
	        	heldRight = true;
	        	socketControl.sendCommand(SCConnect.GoRight);
        	}
        	break;
        case 40:
        	if (!heldDown) {
	        	System.out.println("Pressed Down ");
	        	heldDown = true;
	        	socketControl.sendCommand(SCConnect.GoDown);
        	}
        	break;
	        }
        }
    
    /** Key Released */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
        case 37:	// Left Arrow
        	System.out.println("Left ");
        	socketControl.sendCommand(SCConnect.StopLeft);
        	heldLeft = false;
        	break;
        case 38:	// Up Arrow
        	System.out.println("Up ");
        	socketControl.sendCommand(SCConnect.StopUp);
        	heldUp = false;
        	break;
        case 39:	// Right Arrow
        	System.out.println("Right ");
        	socketControl.sendCommand(SCConnect.StopRight);
        	heldRight = false;
        	break;
        case 40:	// Down Arrow
        	System.out.println("Down ");
        	socketControl.sendCommand(SCConnect.StopDown);
        	heldDown = false;
        	break;
        }
    }
}

private class ButtonListen implements ActionListener {
	// Button listener.
	public void actionPerformed(ActionEvent be) {
		// If there is an active socket, change the button to a
		// disconnect button.
        if (!socketControl.isConnected()) {	
        	connect();
        } else {
        	disconnect();	// Change button text and disconnect.
        }
	}
}
}
