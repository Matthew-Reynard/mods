package com.matthewreynard.testmod.actions;

import net.minecraft.entity.player.EntityPlayer;

public class Actions {
	
	static EntityPlayer player;
	public static boolean actionComplete = false;
	public static double startXpos;
	public static double startYpos;
	public static double startZpos;
	public static boolean jumped = false;
	
	public Actions(EntityPlayer p){
		player = p;
	}
	
	public void setPlayerPos(double x, double y, double z) {
		startXpos = x;
		startYpos = y;
		startZpos = z;
		jumped = false;
	}
	
	
	public boolean moveForward() {
		
		double distanceToMove = 1 - (Math.abs(startXpos) % 1.0) + 0.3;
		
		if (player.posX < startXpos + distanceToMove) {
			player.setVelocity(0.06f, 0.0f, 0.0f);
			actionComplete = false;
		}
		else {
			actionComplete = true;
		}

		return !actionComplete;
	}
	
	public boolean moveBackward() {
		
		double distanceToMove = (Math.abs(startXpos) % 1.0) + 0.3;
		
		if (player.posX > startXpos - distanceToMove) {
			player.setVelocity(-0.06f, 0.0f, 0.0f);
			actionComplete = false;
		}
		else {
			actionComplete = true;
		}

		return !actionComplete;
	}
	
	public boolean moveLeft() {
		
		double distanceToMove = (Math.abs(startZpos) % 1.0) + 0.3;
		
		if (player.posZ > startZpos - distanceToMove) {
			player.setVelocity(0.0f, 0.0f, -0.06f);
			actionComplete = false;
		}
		else {
			actionComplete = true;
		}

		return !actionComplete;
	}
	
	public boolean moveRight() {
		
		double distanceToMove = 1 - (Math.abs(startZpos) % 1.0) + 0.3;
		
		if (player.posZ < startZpos + distanceToMove) {
			player.setVelocity(0.0f, 0.0f, 0.06f);
			actionComplete = false;
		}
		else {
			actionComplete = true;
		}

		return !actionComplete;
	}
	
	public boolean jumpForward() {
		
		double distanceToMove = 1 - (Math.abs(startXpos) % 1.0) + 0.3;
		
		if (player.posX < startXpos + distanceToMove) {
			player.setVelocity(0.06f, player.motionY, 0.0f);
			if (Math.abs(player.posX % 1) > 0.65 && player.posY <= startYpos) {
				player.jump();
			}
			
			actionComplete = false;
			
		}
		else {
			actionComplete = true;
		}

		return !actionComplete;
	}
	
}
