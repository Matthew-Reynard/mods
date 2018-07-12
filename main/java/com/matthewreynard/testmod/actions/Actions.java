package com.matthewreynard.testmod.actions;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Actions {
	
	static EntityPlayer player;
	public static boolean actionComplete = false;
	public static double startXpos;
	public static double startYpos;
	public static double startZpos;
	public static double startYaw;
	public static double startPitch;
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
	
	public void setPlayerAngles(double yaw, double pitch) {
		startYaw = yaw;
		startPitch = pitch;
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
	
	public boolean breakBlock() {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		// For the animation
		player.swingArm(EnumHand.MAIN_HAND);
    	
		// Destroy the block and drop it on ground
		player.getEntityWorld().destroyBlock(new BlockPos(player.posX + 1.0, player.posY, player.posZ), true);
    	mc.getIntegratedServer().getEntityWorld().destroyBlock(new BlockPos(player.posX + 1.0, player.posY, player.posZ), true);
    	
    	//Set that block to air
    	player.getEntityWorld().setBlockToAir(new BlockPos(player.posX + 1.0, player.posY, player.posZ));
    	mc.getIntegratedServer().getEntityWorld().setBlockToAir(new BlockPos(player.posX + 1.0, player.posY, player.posZ));
		
    	actionComplete = true;
    	
		return !actionComplete;
	}
	
	public boolean placeBlock() {
		
		Minecraft mc = Minecraft.getMinecraft();
		BlockPos blockpos = new BlockPos(player.posX + 1.0, player.posY, player.posZ);
		
		// For the animation
		player.swingArm(EnumHand.MAIN_HAND);
    	
		// Get item from main hand
		Item myItem = player.getHeldItemMainhand().getItem();
//		player.getHeldItemMainhand().useItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
		
		// Check if the block can be placed, then place whatever block is in main hand and subtract stack by 1
		if (Block.getBlockFromItem(myItem).canPlaceBlockAt(player.getEntityWorld(), blockpos)) {
			
			player.getEntityWorld().setBlockState(blockpos, Block.getBlockFromItem(myItem).getDefaultState());
			mc.getIntegratedServer().getEntityWorld().setBlockState(blockpos, Block.getBlockFromItem(myItem).getDefaultState());
	    	player.getHeldItemMainhand().splitStack(1);
	    	
		}
		
    	actionComplete = true;
    	
		return !actionComplete;
	}
	
	public boolean lookLeft() {
		
//		Minecraft mc = Minecraft.getMinecraft();
    	
//		double angleToRotate = 1 - (Math.abs(startYaw) % 1.0) + 0.3;
		
//		if (player.posZ < startZpos + angleToRotate) {
//			player.setVelocity(0.0f, 0.0f, 0.06f);
//			actionComplete = false;
//		}
//		else {
//			actionComplete = true;
//		}
		
		player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw - 15, player.rotationPitch);
		
		actionComplete = true;
		
		return !actionComplete;
	}
	
	public boolean lookRight() {
		
		player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw + 15, player.rotationPitch);
		
		actionComplete = true;
		
		return !actionComplete;
	}
	
}
