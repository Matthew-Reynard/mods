package com.matthewreynard.testmod.actions;

import net.minecraft.entity.player.EntityPlayer;

public class Actions {
	
	EntityPlayer player;
	
	public Actions(EntityPlayer p){
		player = p;
	}
	
	
	public boolean moveForward() {
		player.setVelocity(-0.05f, 0.0f, 0.0f);
		return false;
	}
	
	public boolean moveLeft() {
		
		player.setVelocity(0.0f, 0.0f, -0.05f);
		return false;
	}
	
	public boolean moveRight() {
		
		player.setVelocity(0.0f, 0.0f, 0.05f);
		return false;
	}
	
	public boolean moveBackward() {
		
		player.setVelocity(0.05f, 0.0f, 0.0f);
		return false;
	}
	
}
