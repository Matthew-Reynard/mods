package com.matthewreynard.testmod.events;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Util.EnumOS;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
//import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import com.matthewreynard.testmod.actions.Actions;
import com.matthewreynard.testmod.network.MessageExplode;
import com.matthewreynard.testmod.network.NetworkHandler;
import com.matthewreynard.testmod.network.Server;
import com.matthewreynard.testmod.tickrate.ChangeTickRate;
import com.matthewreynard.testmod.util.Reference;

import io.netty.channel.ChannelPipeline;
import jline.internal.Log;

public class TestEventHandler {
	
	public static boolean toggled = false;
	public static boolean tickchange = false;
//	public static boolean isPerformingAction = false; // Found in Reference now...
	
	public static int r = 0;
	public static int action = -1; // initialized to do nothing
	
	public static boolean isActionComplete = false;
	
	public static int food_x = 0;
	public static int food_z = 0;
	
	public static int prev_food_x = 0;
	public static int prev_food_z = 0;
	
	public static Random rnd = new Random();
	
	public static Server server;
	
	public static float[] state = new float[4];
	
	//test
	public static long startTime = 0;
	public static long currentTime = 0;
	public static long numOfTicks = 0;
	
	/**
	 * Called every world tick
	 * Can only be called when game is not paused,
	 * BUG: Might be called while the game is pausing... not sure yet
	 * 
	 * @param event
	 */
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		Actions act = new Actions(mc.player);
		
		if(Reference.isTraining && mc.player.world.isRemote) {
			
//			mc.skipRenderWorld = true;
			
//			BlockPos blockpos = mc.player.getPosition().down();
			BlockPos blockpos = new BlockPos(Math.floor(mc.player.posX),Math.floor(mc.player.posY-1),Math.floor(mc.player.posZ));
			
			if(mc.world.getBlockState(blockpos).getMaterial() == Material.GLASS) {
				
				Log.info("RESET");
				
				mc.player.setLocationAndAngles(3, 101, 3, mc.player.rotationYaw, mc.player.rotationPitch);
			
				food_x = rnd.nextInt(8);
	        	food_z = rnd.nextInt(8);
	        	
	        	IBlockState state_red = mc.world.getBlockState(new BlockPos(0,99,0));
	        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(0,98,0));
	        	mc.world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
	        	mc.world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
	        	
	        	prev_food_x = food_x;
	        	prev_food_z = food_z;
			
	        	// Update state
				state[0]= (float)Math.floor(mc.player.posZ);
				state[1]= (float)Math.floor(mc.player.posX);
				state[2]= (float)food_z;
				state[3]= (float)food_x;
				
				// Doesn't require the server to be running
				Server.setState(state);
//				Server.setMinecraft(mc);
				
//				synchronized(Server.lock) {
//					Server.sendState();
//					System.out.println("send it");
//					Reference.isAwaitingAction = true;
					Reference.setAction(true);
					Reference.isPerformingAction = false;
//				}
				
				action = -1;
			
			}
			
			else if (!Reference.isPerformingAction) {
				
				// If the agent is on the food block
				if (Math.floor(mc.player.posX) == food_x && Math.floor(mc.player.posZ) == food_z) {
					food_x = rnd.nextInt(8);
		        	food_z = rnd.nextInt(8);
		        	
		        	IBlockState state_red = mc.world.getBlockState(new BlockPos(0,99,0));
		        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(0,98,0));
		        	mc.world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
		        	mc.world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
		        	
		        	prev_food_x = food_x;
		        	prev_food_z = food_z;
				}
				
				// Update state
				state[0]= (float)Math.floor(mc.player.posZ);
				state[1]= (float)Math.floor(mc.player.posX);
				state[2]= (float)food_z;
				state[3]= (float)food_x;
				
				// Doesn't require the server to be running
				Server.setState(state);
//				Server.setMinecraft(mc);
				
//				synchronized(Server.lock) {
//					Server.sendState();
//					Reference.isAwaitingAction = true;
					Reference.setAction(true);
//					System.out.println("send it");
//				}
				
				try {
					Log.info("PAUSE");
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
				} catch (AWTException e) {
					e.printStackTrace();
				}
				
//				synchronized (Server.actionLock) {
				action = Server.getAction();
//				action = -1;
//				}
	
				Log.info("Action: " + Integer.toString(action) + "\n");
				
//				Reference.isSending = false;
				Reference.isPerformingAction = true;
				act.setPlayerPos(mc.player.posX, mc.player.posY, mc.player.posZ);
	        	act.setPlayerAngles(mc.player.rotationYaw, mc.player.rotationPitch);
			}
			
			if(!mc.isGamePaused() && !Reference.isAwaitingAction) {
				switch(action) {
					case 1: //up
//						System.out.println("\nAction: "+action+" -> UP");
	//					player.setVelocity(-0.05f, 0.0f, 0.0f);
						Reference.isPerformingAction = act.moveForward();
						break;
					
					case 0: //down
//						System.out.println("\nAction: "+action+" -> DOWN"); 
	//					player.setVelocity(0.05f, 0.0f, 0.0f);
						Reference.isPerformingAction = act.moveBackward();
						break;
					
					case 2: //left
//						System.out.println("\nAction: "+action+" -> LEFT");
	//					player.setVelocity(0.0f, 0.0f, -0.05f);
						Reference.isPerformingAction = act.moveLeft();
						break;
					
					case 3: //right
//						System.out.println("\nAction: "+action+" -> RIGHT");							
	//					player.setVelocity(0.0f, 0.0f, 0.05f);
						Reference.isPerformingAction = act.moveRight();
						break;
						
					case 4: //jump forward
//						System.out.println("\nAction: "+action+" -> JUMP FORWARD");
						Reference.isPerformingAction = act.jumpForward();
						break;
						
					case 5: //break block forward
//						System.out.println("\nAction: "+action+" -> BREAK BLOCK FORWARD");
						Reference.isPerformingAction = act.breakBlock();
						break;
						
					case 6: //place block forward
//						System.out.println("\nAction: "+action+" -> PLACE BLOCK FORWARD");
						Reference.isPerformingAction = act.placeBlock();
						break;
					
					case 7: //look up
//						System.out.println("\nAction: "+action+" -> LOOK UP");
						Reference.isPerformingAction = act.lookUp();
						break;
						
					case 8: //look down
//						System.out.println("\nAction: "+action+" -> LOOK DOWN");
						Reference.isPerformingAction = act.lookDown();
						break;
					
					case 9: //look left
//						System.out.println("\nAction: "+action+" -> LOOK LEFT");
						Reference.isPerformingAction = act.lookLeft();
						break;
						
					case 10: //look right
//						System.out.println("\nAction: "+action+" -> LOOK RIGHT");
						Reference.isPerformingAction = act.lookRight();
						break;
						
					case 11: //jump and place block below
//						System.out.println("\nAction: "+action+" -> JUMP AND PLACE");
						Reference.isPerformingAction = act.jumpAndPlaceBlock();
						break;
						
					case 12: //look north
//						System.out.println("\nAction: "+action+" -> LOOK NORTH");
						Reference.isPerformingAction = act.lookNorth();
						break;
					
					default: 
//						System.out.println("Invalid action - do nothing");
						Reference.isPerformingAction = false;
				}
			}
			
//			if (!Reference.isPerformingAction) {
////				Reference.isSending = true;
////				Reference.isAwaitingAction = true;
//				System.out.println("");
//			}
		}
	}
	
	/**DEACTIVATED EVENT - In order to re-enable it, uncomment the @SubscribeEvent line above function
	 * Main function to receive actions from python socket and execute an action every tick
	 * 
	 * @param event
	 */
	
//	@SubscribeEvent
	public void test(LivingUpdateEvent event)
	{
		// Do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack heldItem = player.getHeldItemMainhand();
			
			Minecraft mc = Minecraft.getMinecraft();
			
			Actions act = new Actions(player);
			
			ITextComponent msg = new TextComponentString("TPS: ");
			
			// Player is invincible
			player.capabilities.disableDamage = true;
			
			if (toggled) {
				
				currentTime = System.currentTimeMillis();
				
				//24000 day-night cycle. 1 time measurement = 1 tick
				if(mc.world.getWorldTime() % 1 == 0) {
					
					// Just to stop it from occurring twice
//					if (player.world.isRemote) {
//						numOfTicks++;
//					
//						if (currentTime - startTime >= 1000) {
//							System.out.println("TPS: " + numOfTicks + "           World time: " + mc.world.getWorldTime());
//							player.sendMessage(msg.appendText(Long.toString(numOfTicks)));
//							numOfTicks = 0;
//							startTime = System.currentTimeMillis();
//						}
//					}
					
//					server.setmcServer(this);
//					
//					if (waiting) {
//					
//						synchronized (this) {
//							try {
//								System.out.println("Thread is running");
//								wait();
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
					
					// If the agent is on the food block
					if (Math.floor(player.posX) == food_x && Math.floor(player.posZ) == food_z) {
						food_x = rnd.nextInt(10);
			        	food_z = rnd.nextInt(10);
			        	
			        	IBlockState state_red = mc.world.getBlockState(new BlockPos(0,99,0));
			        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(0,98,0));
			        	mc.world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
			        	mc.world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
			        	
//			        	IBlockState state_red = mc.world.getBlockState(new BlockPos(-1,5,-1));
//			        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(-1,4,-1));
//			        	mc.world.setBlockState(new BlockPos(prev_food_x,4,prev_food_z), state_wood);
//			        	mc.world.setBlockState(new BlockPos(food_x,4,food_z), state_red);
			        	
			        	prev_food_x = food_x;
			        	prev_food_z = food_z;
					}
					
					float[] x = new float[4];
					
					x[0]= (float)Math.floor(mc.player.posZ);
					x[1]= (float)Math.floor(mc.player.posX);
					x[2]= (float)food_z;
					x[3]= (float)food_x;
					
					// Doesn't require the server to be running
					Server.setState(x);
					Server.setMinecraft(mc);
					
					if (!Reference.isPerformingAction && player.world.isRemote) {
						System.out.println("Not performing action");
						Server.sendState();
						
						try {
							System.out.println("PAUSE");
							Robot robot = new Robot();
							robot.keyPress(KeyEvent.VK_ESCAPE);
							robot.keyRelease(KeyEvent.VK_ESCAPE);
						} catch (AWTException e) {
							e.printStackTrace();
						}
						Reference.isPerformingAction = true;
						System.out.println("After pause");
					}
					
//					Server.sendState();
					action = Server.getAction();
					
					if (player.world.isRemote) {
						System.out.println("\nAction: " + Integer.toString(action) + "\n");
						switch(action) {
							case 0: //up
								System.out.println("\nAction: "+action+" -> UP");
//								player.setVelocity(-0.05f, 0.0f, 0.0f);
								Reference.isPerformingAction = act.moveForward();
								break;
							
							case 1: //down
								System.out.println("\nAction: "+action+" -> DOWN"); 
//								player.setVelocity(0.05f, 0.0f, 0.0f);
								Reference.isPerformingAction = act.moveBackward();
								break;
							
							case 2: //left
								System.out.println("\nAction: "+action+" -> LEFT");
//								player.setVelocity(0.0f, 0.0f, -0.05f);
								Reference.isPerformingAction = act.moveLeft();
								break;
							
							case 3: //right
								System.out.println("\nAction: "+action+" -> RIGHT");							
//								player.setVelocity(0.0f, 0.0f, 0.05f);
								Reference.isPerformingAction = act.moveRight();
								break;
								
							case 4: //jump forward
								System.out.println("\nAction: "+action+" -> JUMP FORWARD");
								Reference.isPerformingAction = act.jumpForward();
								break;
								
							case 5: //break block forward
								System.out.println("\nAction: "+action+" -> BREAK BLOCK FORWARD");
								Reference.isPerformingAction = act.breakBlock();
								break;
								
							case 6: //place block forward
								System.out.println("\nAction: "+action+" -> PLACE BLOCK FORWARD");
								Reference.isPerformingAction = act.placeBlock();
								break;
							
							case 7: //look up
								System.out.println("\nAction: "+action+" -> LOOK UP");
								Reference.isPerformingAction = act.lookUp();
								break;
								
							case 8: //look down
								System.out.println("\nAction: "+action+" -> LOOK DOWN");
								Reference.isPerformingAction = act.lookDown();
								break;
							
							case 9: //look left
								System.out.println("\nAction: "+action+" -> LOOK LEFT");
								Reference.isPerformingAction = act.lookLeft();
								break;
								
							case 10: //look right
								System.out.println("\nAction: "+action+" -> LOOK RIGHT");
								Reference.isPerformingAction = act.lookRight();
								break;
								
							case 11: //jump and place block below
								System.out.println("\nAction: "+action+" -> JUMP AND PLACE");
								Reference.isPerformingAction = act.jumpAndPlaceBlock();
								break;
								
							case 12: //look north
								System.out.println("\nAction: "+action+" -> LOOK NORTH");
								Reference.isPerformingAction = act.lookNorth();
								break;
							
							default: 
//								System.out.println("Invalid action - do nothing");
								Reference.isPerformingAction = false;
						}
					}
				}
				
				// Allow flight when toggled if hand is empty
				if (heldItem != null && heldItem.isEmpty()) {
					player.capabilities.allowFlying = true;
				}
				else {
					player.capabilities.allowFlying = false;
				}

			}
			else {
				player.capabilities.allowFlying = player.capabilities.isCreativeMode ? true : false;
			}
		}
	}
	
	
	/**
	 * All key inputs function
	 * 
	 * Keys already in use in Minecraft:
	 * W, A, S, D, Space, Ctrl, Shift, Tab, Q, E, T, F, X, C, L, 1-9, ~, F1-F12 
	 * 
	 * 
	 * All keys available for use in Minecraft (without changing the defaults):
	 * Z, R, Y, U, I, O, P, G, H, J, K, V, B, N, M, [, ], ;, ', ., /, -, =, Arrow Keys
	 * 
	 * @param event
	 */
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		World world = mc.world;
		MinecraftServer mcServer = mc.player.getServer();
		
		Actions act = new Actions(player);
		
		// KEY V
        if (Keybinds.toggle.isPressed())
        {
        	toggled = !toggled;
            
        	if (toggled) {
        		Minecraft.getMinecraft().player.sendChatMessage("Mod ON");	
        	}
        	if (!toggled) {
        		Minecraft.getMinecraft().player.sendChatMessage("Mod OFF");
        	}
        	
        	startTime = System.currentTimeMillis();
        	numOfTicks = 0;
        }    	
        
        // KEY R
        if (Keybinds.print.isPressed())
        {
        	Log.info("RESET");
			mc.player.setLocationAndAngles(3, 101, 3, mc.player.rotationYaw, mc.player.rotationPitch);
		
			food_x = rnd.nextInt(8);
        	food_z = rnd.nextInt(8);
        	
        	IBlockState state_red = mc.world.getBlockState(new BlockPos(0,99,0));
        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(0,98,0));
        	mc.world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
        	mc.world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
        	
        	prev_food_x = food_x;
        	prev_food_z = food_z;
		
        	// Update state
			state[0]= (float)Math.floor(mc.player.posZ);
			state[1]= (float)Math.floor(mc.player.posX);
			state[2]= (float)food_z;
			state[3]= (float)food_x;
			
			// Doesn't require the server to be running
			Server.setState(state);
			Server.setMinecraft(mc);
			
//			synchronized(Server.lock) {
//				Server.sendState();
//				System.out.println("send it");
//			}
			
			action = -1;
        	
        	Reference.isTraining = !Reference.isTraining;
        	 	
        	
//			server.setmcServer(mc.getIntegratedServer().getServerThread());
//			
//			synchronized (mc.getIntegratedServer().getServerThread()) {
//				try {
//					System.out.println("Waiting...");
//					
//					mc.getIntegratedServer().getServerThread().wait();
//					
//					System.out.println("Resumed...");
//					
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
        	
        }
        
        // KEY B
        if (Keybinds.state.isPressed())
        {
        	System.out.println("STOP");
        	Reference.isTraining = !Reference.isTraining;
        	server.closeConnection();
        }
        
        // Looking
        if (Keybinds.up.isPressed()) {
//			player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        	
        	action = 7;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.down.isPressed()) {
//			player.setLocationAndAngles(player.posX - 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			
        	action = 8;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.left.isPressed()) {
//			player.setLocationAndAngles(player.posX, player.posY, player.posZ - 1.0, player.rotationYaw, player.rotationPitch);
			
//			
        	action = 9;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.right.isPressed()) {
//			player.setLocationAndAngles(player.posX, player.posY, player.posZ + 1.0, player.rotationYaw, player.rotationPitch);
			
        	action = 10;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
        
        //Absolute Movement
        
        // Key Y
        if (Keybinds.north.isPressed()) {
        	action = 0;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key H
        else if (Keybinds.south.isPressed()) { // Key H
        	action = 1;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key G
        else if (Keybinds.west.isPressed()) {
        	action = 2;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key J
        else if (Keybinds.east.isPressed()) {
        	action = 3;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key M
        else if (Keybinds.jumpForward.isPressed()) {
        	action = 4;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        
        //Other actions
        
        // Key U
        if (Keybinds.breakBlock.isPressed()) {
        	action = 5;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key I
        else if (Keybinds.placeBlock.isPressed()) {
        	action = 6;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key N
        else if (Keybinds.reset.isPressed()) {
        	action = 12;
        	Reference.isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
    }

	
	/**DEACTIVATED EVENT
	 * Super Jump if toggled
	 * Just for fun :)
	 * @param event
	 */
	
//	@SubscribeEvent
	public void superJump(LivingJumpEvent event) {
		
		if (event.getEntity() instanceof EntityPlayer && toggled) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			player.motionY += 1.2D;
		}
	}
	
	/**
	 * Network event
	 * Not used at the moment
	 * 
	 * @param event
	 */
	
	@SubscribeEvent
    public void networkStuff(ServerConnectionFromClientEvent event)
    {
		
		System.out.println("\n\nServerConnectionFromClientEvent was successful\n");
		
//		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
//		
//		ChannelPipeline network = event.getManager().channel().pipeline();
//		System.out.print("Pipeline: " + network.toString());
		
    }
	
	
	/**
	 * Used for the other classes to get access to the same instance of the Socket Server used for Java-Python communication
	 * Set the server for the synchronized block to used same lock (wait() and notify()) -> NOT WORKING!!!
	 *  
	 * @param s
	 * An instance of the Server class 
	 */
	public void getServer(Server s) {
		server = s;
	}
	
	
	/**
	 * Resets the player back to the start coordinates of the snake game (could be random)
	 * Resets the position of the food on the 10x10 grid
	 * 
	 * @param null 
	 */
	public void resetPlayer() {
		System.out.println("Does nothing yet");
	}

}

/* Useful commands:
 * 
 * WORLD STUFF:
 * world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Item().getItemById(371), 1)));
 * world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Block(Material.WOOD).getBlockById(5), 1)));
 * world.spawnEntity(new EntityItem(world, 0, 5, 4, new ItemStack(new ItemBlock(new Block(Material.WOOD)))));
 * world.setBlockToAir(new BlockPos(0,5,0));
 * 
 * PRINT OUT STUFF:
 * System.out.println("\n\nChunks -> X: " + player.chunkCoordX + "\tY: " +  player.chunkCoordY + "\tZ: " + player.chunkCoordZ +"\n");
 * System.out.println("\nserverX:" + player.serverPosX + "\t serverY:" + player.serverPosY + "\t serverZ:" + player.serverPosZ); 
 * System.out.println("\n\nx:" + player.posX + "\t y:" + player.posY + "\t z:" + player.posZ + "\n");
 * 
 * MINECRAFT GAME SETTINGS:
 * mc.gameSettings.setOptionFloatValue(Options.FOV, 70.0f);
 * mc.gameSettings.setOptionValue(Options.AUTO_JUMP, 1); //off
 * 
 * PLAYER MOVEMENT:
 * player.travel(0.0f, 0.0f, 1.0f);
 * player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch); // Teleport
 * player.moveRelative(-1.0f, 0, 0.0f, 0.5f);
 * player.turn(300, 0); // (0.15/360)*300
 * 
 * PLAYER STUFF:
 * player.setInvisible(true);
 * player.knockBack(player, 1.0f, 1.0, 1.0);
 * player.swingArm(EnumHand.MAIN_HAND);
 * player.capabilities.setPlayerWalkSpeed(0.1f); //0.2 = walk speed ; 0.1 = sneak speed
 * 
 * PLAYER BOOLEANS:
 * if (player.isAirBorne){//do something useful}
 * if (player.collided) {System.out.println("\n\nThe player has collided with something\n");}
 * if (player.onGround) {player.jump();}
 * if (player.isSwingInProgress) {System.out.println("Arm swing is in progress");}
 * 
 * SERVER STUFF:
 * server.isSinglePlayer();
 * 
 * NetworkHandler.sendToServer(new MessageExplode());
 * System.out.println("Some event called; is this the client side? " + event.getEntity().world.isRemote);
 * 
 * */
