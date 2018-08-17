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
	
	public static final boolean TRAINING = false;
	public static final boolean RUNNING = false; 
	public static final boolean BENCHMARKING = true;
	
	public static boolean open = false;
	public static float[] state = {0,0,0,0}; 
	public static int action = -1;
	
	// Needs to be a string to send it over the socket
	public static String reward = "0";
	
	public static int port = 5555;
	
	public static Minecraft minecraft;
	
	public static ServerSocket server;
	
	public static long pauseStartTime = 0;
	public static long currentTime = 0;
	
	// Threads main function
	public void run() {
		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		String fromClient;
		
//		Reference.isTraining = true;
		
		try {
			server = new ServerSocket(port);
		
			System.out.println("TCP Server waiting for client on port " + port);
			
			Socket connected = server.accept();
			System.out.println("The Client "+ connected.getInetAddress() + ":" + connected.getPort() + " is connected");
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
			
			DataOutputStream outFromServer = new DataOutputStream(connected.getOutputStream());
			
			// Open the server
			open = true;
			
			/**
			 * TRAINING the RL algorithm in Minecraft
			 */
			
			if (TRAINING) {
				
				currentTime = System.currentTimeMillis();
			
				while(open) {
					
					if(minecraft.isGamePaused() && !Reference.isAwaitingAction && Reference.isTraining) {
						
	//					Reference.setAction(true);
	//					System.out.println(Reference.isEpisodeDone);
						
	//					Reference.unpauseLimit++;
						currentTime = System.currentTimeMillis();
						
						if(currentTime-pauseStartTime >= 700) {
							//Unpause
							try {
								Log.info("UNPAUSE AGAIN");
								pauseStartTime = System.currentTimeMillis();
								Robot robot = new Robot();
								robot.keyPress(KeyEvent.VK_ESCAPE);
								robot.keyRelease(KeyEvent.VK_ESCAPE);
							} catch (AWTException e) {
								e.printStackTrace();
							}
						}
					}
					
					if(Reference.isAwaitingAction) {
						
						//Pause
	//					try {
	//						Robot robot = new Robot();
	//						robot.keyPress(KeyEvent.VK_ESCAPE);
	//						robot.keyRelease(KeyEvent.VK_ESCAPE);
	//					} catch (AWTException e) {
	//						e.printStackTrace();
	//					}
						
	//					if(!minecraft.isGamePaused()) { // THIS IS FOR RUNNING THE CNN IN MINECRAFT WITHOUT PAUSING... SMOOTH GAMEPLAY
						if(minecraft.isGamePaused()) { // THIS IS FOR TRAINING
							
	//							System.out.println("Sending state: " + Arrays.toString(state));
							
							if (Reference.isEpisodeDone) {
								// sends done to python
								outFromServer.writeUTF("[done, " + reward + "]");
								
								// Start of a new episode (isEpisodeDone = false)
								Reference.setDone(false);
								
	//							Reference.isEpisodeDone = false;
								
								// RUNNING
	//							while(Reference.isEpisodeDone) {
	//								Reference.setDone(false);
	//							}
							}
							else {
								
								// sends the state of Minecraft agent to python
								outFromServer.writeUTF(Arrays.toString(state).replace("]",", " + reward + "]"));
							
								//Python decides action...
								
								fromClient = inFromClient.readLine();
								
								// sets this class variable action to the correct action number from python
								setAction(fromClient);	
							}
							
							//Unpause
							try {
								Log.info("UNPAUSE");
								Robot robot = new Robot();
								robot.keyPress(KeyEvent.VK_ESCAPE);
								robot.keyRelease(KeyEvent.VK_ESCAPE);
							} catch (AWTException e) {
								e.printStackTrace();
							}
							
	//						minecraft.skipRenderWorld = true;
							
							// Agent is no longer waiting for an action number, the action number was set
							Reference.setAction(false);
							
						}
					}
				}
			}
			
			
			/**
			 * RUNNING the RL algorithm in Minecraft
			 */
			
			if (RUNNING) {
				
				while(open) {
					
					if(Reference.isAwaitingAction) {

						if(!minecraft.isGamePaused()) { // THIS IS FOR RUNNING THE CNN IN MINECRAFT WITHOUT PAUSING... SMOOTH GAMEPLAY
							
	//							System.out.println("Sending state: " + Arrays.toString(state));
							
							if (Reference.isEpisodeDone) {
								// sends done to python
								outFromServer.writeUTF("[done, " + reward + "]");
								
								// Start of a new episode (isEpisodeDone = false)
								Reference.setDone(false);
							}
							else {
								
								// sends the state of Minecraft agent to python
								outFromServer.writeUTF(Arrays.toString(state).replace("]",", " + reward + "]"));
							
								//Python decides action...
								
								fromClient = inFromClient.readLine();
								
								// sets this class variable action to the correct action number from python
								setAction(fromClient);	
							}
							
							// Agent is no longer waiting for an action number, the action number was set
							Reference.setAction(false);
							
						}
					}
				}
			}
			
			/**
			 * Benchmark testing
			 */
			
			if (BENCHMARKING) {
				
				while(open) {
					
//					System.out.println("hi");
					
					if(Reference.isAwaitingAction) {
				
//						for(int i = 0; i < 10000; i++) {
							
//							System.out.println(minecraft.isGamePaused());
							
							if(minecraft.isGamePaused()) { 
								
//								System.out.println("Unpausing");
								
								//Unpause
								try {
									Robot robot = new Robot();
									robot.keyPress(KeyEvent.VK_ESCAPE);
									robot.keyRelease(KeyEvent.VK_ESCAPE);
								} catch (AWTException e) {
									e.printStackTrace();
								}
								Reference.setAction(false);
								
							}
							
//							else {
//								i--;
//							}
//						}
						
//						Reference.setAction(false);
					
					}
				
				}
			}
			
			
			//Close the python client safely
			outFromServer.writeUTF("[close]");
			
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
	
	public static void setReward(String r) {
		reward = r;
	}
	
	public static void closeConnection() {
		open = false;
	}
	
	// is this used?
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
	
	public static void setMinecraft(Minecraft m) {
		minecraft = m;
	}
	
}

// POSSIBLY USEFUL IF THIS TECHNIQUE DOESNT WORK

//while(open) {
//fromClient = inFromClient.readLine();
//
////	outFromServer.writeUTF(Arrays.toString(state));
//
//if(fromClient.equals("q") || fromClient.equals("Q")) {
////	connected.close();
//	break;
//}
//else if(fromClient.equals("p")) {
//	
////	synchronized (mc){
////		System.out.println("Thread is still running");
////		mc.notifyAll();
////	}
//	
//	System.out.println("Send from server to clients - the state");
//	outFromServer.writeUTF(Arrays.toString(state));
//	
//	fromClient = inFromClient.readLine();
//	
//	System.out.println("Action: " + fromClient);
//	if (!fromClient.startsWith("p")) {
//		setAction(fromClient);							
//	}
//}
//else {
//	System.out.println("Recieved: " + fromClient);
//}
//}
