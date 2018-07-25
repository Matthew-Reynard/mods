package com.matthewreynard.testmod.network;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Arrays;

import com.matthewreynard.testmod.events.TestEventHandler;
import com.matthewreynard.testmod.util.Reference;

import jline.internal.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class Server extends Thread {
	
	public static boolean open = false;
	public static float[] state = {0,0,0,0};
	public static int action = -1;
	
	public static int port = 5555;
	
	public static Object mc;
	
	public static Object lock;
	public static Object actionLock;
	
	public static Minecraft minecraft;
	
	public static ServerSocket server;
	
	public static boolean sendState = false;
	
	public void run() {
		
		String fromClient;
		lock = new Object();
		actionLock = new Object();
		
		try {
			server = new ServerSocket(port);
		
			System.out.println("TCP Server waiting for client on port " + port);
			
			Socket connected = server.accept();
			System.out.println("The Client "+ connected.getInetAddress() + ":" + connected.getPort() + " is connected");
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
			
			DataOutputStream outFromServer = new DataOutputStream(connected.getOutputStream());
			
			open = true;
			
//			while(open) {
//				fromClient = inFromClient.readLine();
//				
////					outFromServer.writeUTF(Arrays.toString(state));
//				
//				if(fromClient.equals("q") || fromClient.equals("Q")) {
////					connected.close();
//					break;
//				}
//				else if(fromClient.equals("p")) {
//					
////					synchronized (mc){
////		        		System.out.println("Thread is still running");
////		        		mc.notifyAll();
////		        	}
//					
//					System.out.println("Send from server to clients - the state");
//					outFromServer.writeUTF(Arrays.toString(state));
//					
//					fromClient = inFromClient.readLine();
//					
//					System.out.println("Action: " + fromClient);
//					if (!fromClient.startsWith("p")) {
//						setAction(fromClient);							
//					}
//				}
//				else {
//					System.out.println("Recieved: " + fromClient);
//				}
//			}
			
			while(open) {
				
//				fromClient = inFromClient.readLine();
				
//				System.out.println("I'm here");
				
//				try {
//					this.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				
//				try {
//					Robot r = new Robot();
//					r.keyPress(KeyEvent.VK_ESCAPE);
//					r.keyRelease(KeyEvent.VK_ESCAPE);
//				} catch (AWTException e) {
//					e.printStackTrace();
//				}
				
//				if(fromClient.equals("p")) {
				synchronized (lock) {
					if(sendState) {
						
						//Pause
	//					try {
	//						Robot robot = new Robot();
	//						robot.keyPress(KeyEvent.VK_ESCAPE);
	//						robot.keyRelease(KeyEvent.VK_ESCAPE);
	//					} catch (AWTException e) {
	//						e.printStackTrace();
	//					}
						
						if(minecraft.isGamePaused()) {
							
							System.out.println("Sending state: " + Arrays.toString(state));
							
							outFromServer.writeUTF(Arrays.toString(state));
							
							//Python decides action
							System.out.println("Python deciding action");
							
							fromClient = inFromClient.readLine();
							
		//					if (!fromClient.startsWith("p")) {
		//						setAction(fromClient);							
		//					}
							
//							synchronized (actionLock) {
								setAction(fromClient);	
//							}
							
							sendState = false;
							
							System.out.println("Done");
							
							//Unpause
							try {
								System.out.println("UNPAUSE");
								Robot robot = new Robot();
								robot.keyPress(KeyEvent.VK_ESCAPE);
								robot.keyRelease(KeyEvent.VK_ESCAPE);
							} catch (AWTException e) {
								e.printStackTrace();
							}
							
	//						Reference.isSending = true;
						
						}
						
					}
				}
				
			}
			
			// Close the client
			outFromServer.writeUTF(closeClient());
			
			// Close the server
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
	
	// NOT USED
	public static void sendState() {
		
		System.out.println("Sending state...");
		sendState = true;
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
	
	//Close the python client safely
	public String closeClient() {
		String close = "[close]";
		return close;
	}
	
	public void setmcServer(Object s) {
		mc = s;
	}
	
	public static void setMinecraft(Minecraft m) {
		minecraft = m;
	}
	
}
