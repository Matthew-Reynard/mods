package com.matthewreynard.testmod.util;

public class Reference {

	public static final String MOD_ID = "testmod";
	public static final String NAME = "Test Mod";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "com.matthewreynard.testmod.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.matthewreynard.testmod.proxy.CommonProxy";
	
	//Useful global variables
	public static boolean isTraining = false;
//	public static boolean isSending = true;
	public static volatile boolean isAwaitingAction = false;
	public static boolean isPerformingAction = false; 
	public static boolean isEpisodeDone = false;
	public static int actionCount = 0;
	
	public static int unpauseLimit = 0; // not used
	
	public static synchronized void setAction(boolean x) {
		isAwaitingAction = x;
	}
	
	public static synchronized void setDone(boolean x) {
		isEpisodeDone = x;
	}
	
	public static void actionCountIncrement() {
		actionCount++;
	}
	
	public static void actionCountReset() {
		actionCount = 0;
	}
	
}
