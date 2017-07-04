/**
 * 
 */

/**
 * @author David
 *
 */

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.Container;
import java.io.*;


public class SCWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final String newline = System.getProperty("line.separator");
	KeyAdapter listener;
	SCConnect sock;
	
	JTextField hostField, portField;
	

	public SCWindow(String name) {
		super(name);
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
		portField = new JTextField("1337");
		
		Container pane = getContentPane();
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ButtonListen());
		
		GroupLayout layout = new GroupLayout(pane);
		pane.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		hostField.setColumns(9);
		portField.setColumns(4);
		
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
	
	boolean pressLeft = false, 
			pressUp = false, 
			pressRight = false, 
			pressDown = false;
	
	/** Key Pressed */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
        case 37:	// Left Arrow
        	if (!pressLeft) {
        		System.out.println("Pressed Left ");
        		pressLeft = true;
        	}
        	break;
        case 38:	// Up Arrow
        	if (!pressUp) {
	        	System.out.println("Pressed Up ");
	        	pressUp = true;
        	}
        	break;
        case 39:	// Right Arrow
        	if (!pressRight) {
	        	System.out.println("Pressed Right ");
	        	pressRight = true;
        	}
        	break;
        case 40:
        	if (!pressDown) {
	        	System.out.println("Pressed Down ");
	        	pressDown = true;
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
        	pressLeft = false;
        	break;
        case 38:	// Up Arrow
        	System.out.println("Up ");
        	pressUp = false;
        	break;
        case 39:	// Right Arrow
        	System.out.println("Right ");
        	pressRight = false;
        	break;
        case 40:
        	System.out.println("Down ");
        	pressDown = false;
        	break;
        }
    }
}

private class ButtonListen implements ActionListener {
	public void actionPerformed(ActionEvent buttonE) {
        try {
        	sock = new SCConnect(hostField.getText(), Integer.parseInt(portField.getText()));
        } catch (IOException be) {
        	System.out.println("Problem connecting." + be);
        	System.exit(1);
        }
        if (sock.connected == true) {
        	((JButton)buttonE.getSource()).setEnabled(false);
        	addKeyListener(new KeyListen());
        }
	}
}
}
