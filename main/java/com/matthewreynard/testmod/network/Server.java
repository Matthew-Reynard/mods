package com.matthewreynard.testmod.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Arrays;

public class Server extends Thread {
	
	public static boolean open = false;
	public static float[] state;
	public static int action;
	
	public static int port = 5555;
	
	public static ServerSocket server;
	
	public void run() {
		String fromClient;
		
//		int port = 5555;
		
//		ServerSocket server;
		
		try {
			server = new ServerSocket(port);
		
			System.out.println("TCP Server waiting for client on port " + port);
			
			Socket connected = server.accept();
			System.out.println("The Client "+ connected.getInetAddress() + ":" + connected.getPort() + " is connected");
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
			
			DataOutputStream outFromServer = new DataOutputStream(connected.getOutputStream());
			
			open = true;
			
			while(open) {
				fromClient = inFromClient.readLine();
//					outFromServer.writeUTF(Arrays.toString(state));
				
				if(fromClient.equals("q") || fromClient.equals("Q")) {
//					connected.close();
					break;
				}
				else if(fromClient.equals("p")) {
					System.out.println("Send from server to clients - the state");
//						outFromServer.writeUTF("State = 1");
					outFromServer.writeUTF(Arrays.toString(state));
					
					fromClient = inFromClient.readLine();
					
					System.out.println("Action: " + fromClient);
					if (!fromClient.startsWith("p")) {
						setAction(fromClient);							
					}
				}
				else {
					System.out.println("Recieved: " + fromClient);
				}
			}
			
			// close the server
//			server.close();
			connected.close();
			end();
			
			System.out.println("Server has been closed");
			
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setState(float[] s) {
		state = s;
	}
	
	public static void setAction(String a) {
		action = Integer.parseInt(a);
	}
	
	public static int getAction() {
		return action;
	}
	
	public static void send() {
		
		System.out.println("Sending state...");
//		sent = true;
	}
	
	public void closeConnection() {
		open = false;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void end() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
