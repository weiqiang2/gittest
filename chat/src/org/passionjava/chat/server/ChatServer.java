package org.passionjava.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private ServerSocket serverSocket;
	private List<ServerThread> clients = new ArrayList<ServerThread>();

	public ChatServer() {
		try {
			serverSocket = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchFrame() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ServerThread st = new ServerThread(socket);
				clients.add(st);
				new Thread(st).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private class ServerThread implements Runnable {
		private Socket socket;
		BufferedReader br = null;
		BufferedWriter bw = null;
		private boolean connect = true;

		public ServerThread(Socket socket) {
			this.socket = socket;
			try {
				br = new BufferedReader(new InputStreamReader(
						this.socket.getInputStream()));
				bw = new BufferedWriter(new OutputStreamWriter(
						this.socket.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {

			try {
				while (connect) {
					String str = br.readLine();
					if ("-7273886170355111465L".equals(str)) {
						disconnect();
						continue;
					}
					System.out.println(str);
					send(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void send(String message) {
			for(int i = 0; i < clients.size(); i++) {
				ServerThread serverThread = clients.get(i);
				try {
					serverThread.bw.write(message);
					serverThread.bw.newLine();
					serverThread.bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		private void disconnect() {
			connect = false;
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {

		new ChatServer().launchFrame();
	}

}
