package org.passionjava.chat.client;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClient extends Frame {

	private static final long serialVersionUID = -7273886170355111465L;
	private TextArea textArea;
	private TextField textField;
	private Socket socket;
	private BufferedWriter bw;
	private BufferedReader br;

	public ChatClient() {
		textArea = new TextArea();
		textField = new TextField();
	}

	public void launchFrame() {

		setBounds(200, 200, 600, 500);
		add(textArea, BorderLayout.CENTER);
		add(textField, BorderLayout.SOUTH);
		textField.addActionListener(new TextFieldAction());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				send("-7273886170355111465L");
				System.exit(0);
			}
		});
		setResizable(false);
		setVisible(true);
		textField.requestFocus();
		connection();
		new Thread(new ReceiveThread()).start();
	}

	public void connection() {
		try {
			socket = new Socket("localhost", 8888);
			bw = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void send(String message) {

		try {
			bw.write(message);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class ReceiveThread implements Runnable {
		@Override
		public void run() {
			while(true) {
				try {
					String str = br.readLine();
//					textArea.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//						.format(new Date()) + "\n" + str + "\n");
					textArea.append(str + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class TextFieldAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			send(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())
					+ "\n" + textField.getText());
			textArea.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date()) + "\n" + textField.getText() + "\n");
			textField.setText("");
		}

	}

	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}
}
