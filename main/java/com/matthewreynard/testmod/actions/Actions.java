package com.matthewreynard.testmod.actions;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

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
//		actionComplete = false;
//		System.out.print("X:" + player.posX);
//		System.out.println("\tStartX"+startXpos);
		if (player.posX < startXpos + distanceToMove) {
			player.setVelocity(0.07f, 0.0f, 0.0f);
			actionComplete = false;
			
		}
		else {
			actionComplete = true;
			player.setVelocity(0.0f, 0.0f, 0.0f);
		}
		return !actionComplete;
	}
	
	public boolean moveBackward() {
		
		double distanceToMove = (Math.abs(startXpos) % 1.0) + 0.3;
//		actionComplete = false;
//		System.out.print("X:" + player.posX);
//		System.out.println("\tStartX"+startXpos);
		if (player.posX > startXpos - distanceToMove) {
			player.setVelocity(-0.07f, 0.0f, 0.0f);
			actionComplete = false;

		}
		else {
			actionComplete = true;
			player.setVelocity(0.0f, 0.0f, 0.0f);
		}
		return !actionComplete;
	}
	
	public boolean moveLeft() {
		
		double distanceToMove = (Math.abs(startZpos) % 1.0) + 0.3;
//		actionComplete = false;
//		System.out.print("Z:" + player.posZ);
//		System.out.println("\tStartZ"+startZpos);
		if (player.posZ > startZpos - distanceToMove) {
			player.setVelocity(0.0f, 0.0f, -0.07f);
			actionComplete = false;
			
		}
		else {
			actionComplete = true;
			player.setVelocity(0.0f, 0.0f, 0.0f);
		}
		return !actionComplete;
	}
	
	public boolean moveRight() {
		
		double distanceToMove = 1 - (Math.abs(startZpos) % 1.0) + 0.3;
//		actionComplete = false;
//		System.out.print("Z:" + player.posZ);
//		System.out.println("\tStartZ"+startZpos);
		if (player.posZ < startZpos + distanceToMove) {
			player.setVelocity(0.0f, 0.0f, 0.07f);
			actionComplete = false;
			
		}
		else {
			actionComplete = true;
			player.setVelocity(0.0f, 0.0f, 0.0f);
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
//		mc.skipRenderWorld = true;
		
//		Vec3d posVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
//		Vec3d lookVec = player.getLookVec();
//		RayTraceResult rtr = mc.world.rayTraceBlocks(posVec, lookVec);
		
		RayTraceResult rtr = player.rayTrace(4, 1); 
		
		BlockPos blockpos = new BlockPos(rtr.getBlockPos().getX(), rtr.getBlockPos().getY(), rtr.getBlockPos().getZ());
		
		// For the animation
		player.swingArm(EnumHand.MAIN_HAND);
    	
		// Destroy the block and drop it on ground
		player.getEntityWorld().destroyBlock(blockpos, true);
    	mc.getIntegratedServer().getEntityWorld().destroyBlock(blockpos, false);
    	
    	//Set that block to air
    	player.getEntityWorld().setBlockToAir(blockpos);
    	mc.getIntegratedServer().getEntityWorld().setBlockToAir(blockpos);
		
    	actionComplete = true;
    	
		return !actionComplete;
	}
	
	public boolean placeBlock() {
		
		Minecraft mc = Minecraft.getMinecraft();
//		mc.skipRenderWorld = false;
		
		RayTraceResult rtr = player.rayTrace(4, 1); 
		
		Vec3i offset = rtr.sideHit.getDirectionVec();
		
		BlockPos blockpos = new BlockPos(rtr.getBlockPos().getX() + offset.getX(), 
				rtr.getBlockPos().getY() + offset.getY(), 
				rtr.getBlockPos().getZ() + offset.getZ());
		
		// For the animation
		player.swingArm(EnumHand.MAIN_HAND);
    	
		// Get item from main hand
		Item myItem = player.getHeldItemMainhand().getItem();
//		player.getHeldItemMainhand().useItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
		
		// Check if the block can be placed, then place whatever block is in main hand and subtract stack by 1
		if (Block.getBlockFromItem(myItem).canPlaceBlockAt(player.getEntityWorld(), blockpos)) {
			
			player.getEntityWorld().setBlockState(blockpos, Block.getBlockFromItem(myItem).getDefaultState());
			mc.getIntegratedServer().getEntityWorld().setBlockState(blockpos, Block.getBlockFromItem(myItem).getDefaultState());
//			player.getHeldItemMainhand().splitStack(1); // Disabled for now
	    	
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
	
	public boolean lookUp() {
		
		player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch - 15);
		
		actionComplete = true;
		
		return !actionComplete;
	}
	
	public boolean lookDown() {
		
		player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch + 15);
		
		actionComplete = true;
		
		return !actionComplete;
	}
	
	public boolean jumpAndPlaceBlock() {
		
		Minecraft mc = Minecraft.getMinecraft();
		BlockPos blockpos = new BlockPos(startXpos, startYpos, startZpos);
		
		player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch + 180);
    	
		// Get item from main hand
		Item myItem = player.getHeldItemMainhand().getItem();
		
		if (player.onGround && player.posY == startYpos) {
			player.jump();
			actionComplete = false;
		}
		// Check if the block can be placed, then place whatever block is in main hand and subtract stack by 1
		else if (player.posY >= startYpos + 1.0 && Block.getBlockFromItem(myItem).canPlaceBlockAt(player.getEntityWorld(), blockpos)) {
			
			// For the animation
			player.swingArm(EnumHand.MAIN_HAND);
			
			player.getEntityWorld().setBlockState(blockpos, Block.getBlockFromItem(myItem).getDefaultState());
			mc.getIntegratedServer().getEntityWorld().setBlockState(blockpos, Block.getBlockFromItem(myItem).getDefaultState());
	    	player.getHeldItemMainhand().splitStack(1);
	    	
	    	actionComplete = true;
		}
		
		return !actionComplete;
		
	}

	public boolean lookNorth() {
		
		player.setLocationAndAngles(player.posX, player.posY, player.posZ, -90, 0);
		
		actionComplete = true;
		
		return !actionComplete;
	}

}
