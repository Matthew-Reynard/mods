package com.matthewreynard.testmod.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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

import io.netty.channel.ChannelPipeline;
import jline.internal.Log;

public class TestEventHandler {
	
	public static boolean toggled = false;
	public static boolean tickchange = false;
	public static boolean waiting = false;
	public static boolean isPerformingAction = false;
	
	public static int r = 0;
	public static int action = -1; // initialised to do nothing
	
	public static int food_x = 0;
	public static int food_z = 0;
	
	public static int prev_food_x = 0;
	public static int prev_food_z = 0;
	
	public static Random rnd = new Random();
	
	public static Server server;
	
	//test
	public static long startTime = 0;
	public static long currentTime = 0;
	public static long numOfTicks = 0;
	
	/**
	 * Called every tick
	 * 
	 * @param event
	 */
	
	@SubscribeEvent
	public void test1(WorldTickEvent event)
	{
		
		if (waiting) {
			
			Log.info("Pressed R");

			

			NetworkHandler.sendToServer(new MessageExplode());
			
			try {
				Minecraft.getMinecraft().wait(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			waiting = false;
			
//			server.setmcServer(this);
//			
//			if (waiting) {
//			
//				synchronized (this) {
//					try {
//						System.out.println("Thread is running");
//						wait();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
		}
	}
	
	/**
	 * Main function to receive actions from python socket and execute an action every tick
	 * In order to re-enable it, uncomment the @SubscribeEvent line above function
	 * @param event
	 */
	
	@SubscribeEvent
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
			
//			if(!mc.world.isRemote && !waiting) {
//				System.out.println("It is not waiting...");
//			}
			
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
					
//					System.out.println("Exited the loop");
					
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
					
					if (!isPerformingAction) {
//						Server.sendState();
						action = -1;
					}
					
//					r = rnd.nextInt(40);
//					action = Server.getAction();
//					action = 5;
					
					if (player.world.isRemote) {
						switch(action) {
							case 0: //up
								System.out.println("\nAction: "+r+" -> UP");
//								player.setVelocity(-0.05f, 0.0f, 0.0f);
								isPerformingAction = act.moveForward();
								break;
							
							case 1: //down
								System.out.println("\nAction: "+r+" -> DOWN"); 
//								player.setVelocity(0.05f, 0.0f, 0.0f);
								isPerformingAction = act.moveBackward();
								break;
							
							case 2: //left
								System.out.println("\nAction: "+r+" -> LEFT");
//								player.setVelocity(0.0f, 0.0f, -0.05f);
								isPerformingAction = act.moveLeft();
								break;
							
							case 3: //right
								System.out.println("\nAction: "+r+" -> RIGHT");							
//								player.setVelocity(0.0f, 0.0f, 0.05f);
								isPerformingAction = act.moveRight();
								break;
								
							case 4: //jump forward
								System.out.println("\nAction: "+r+" -> JUMP FORWARD");
								isPerformingAction = act.jumpForward();
								break;
								
							case 5: //break block forward
								System.out.println("\nAction: "+r+" -> BREAK BLOCK FORWARD");
								isPerformingAction = act.breakBlock();
								break;
								
							case 6: //place block forward
								System.out.println("\nAction: "+r+" -> PLACE BLOCK FORWARD");
								isPerformingAction = act.placeBlock();
								break;
							
							case 7: //look up
								System.out.println("\nAction: "+r+" -> LOOK UP");
								isPerformingAction = act.lookUp();
								break;
								
							case 8: //look down
								System.out.println("\nAction: "+r+" -> LOOK DOWN");
								isPerformingAction = act.lookDown();
								break;
							
							case 9: //look left
								System.out.println("\nAction: "+r+" -> LOOK LEFT");
								isPerformingAction = act.lookLeft();
								break;
								
							case 10: //look right
								System.out.println("\nAction: "+r+" -> LOOK RIGHT");
								isPerformingAction = act.lookRight();
								break;
								
							case 11: //jump and place block below
								System.out.println("\nAction: "+r+" -> JUMP AND PLACE");
								isPerformingAction = act.jumpAndPlaceBlock();
								break;
								
							case 12: //look north
								System.out.println("\nAction: "+r+" -> LOOK NORTH");
								isPerformingAction = act.lookNorth();
								break;
							
							default: 
//								System.out.println("Invalid action - do nothing");
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
	 * Keys used:
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
		
		// Setting the state of snake minecraft
		float[] x = new float[4];
		
		x[0]= (float)Math.floor(mc.player.posZ);
		x[1]= (float)Math.floor(mc.player.posX);
		x[2]= (float)food_z;
		x[3]= (float)food_x;
		
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
        	waiting = !waiting;
        	
//        	System.out.println("Waiting " + waiting);
        	
//        	if(!world.isRemote) {
//	        	try {
//					mc.getIntegratedServer().wait(1000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//        	}
//        	else {
//        		System.out.println("Integrated server is null");
//        	}
        	
//        	action = 11;
//        	isPerformingAction = true;
//        	act.setPlayerPos(player.posX, player.posY, player.posZ);
//        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        	
//        	try {
//				Thread.sleep(1,10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
        	
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
        	
        	food_x = rnd.nextInt(10);
        	food_z = rnd.nextInt(10);

        	Server.setState(x);
        	
        	// Not used yet
//        	Server.send();
        	
        	IBlockState state_red = world.getBlockState(new BlockPos(0,99,0));
        	IBlockState state_wood = world.getBlockState(new BlockPos(0,98,0));
        	world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
        	world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
        	
//        	IBlockState state_red = mc.world.getBlockState(new BlockPos(-1,5,-1));
//        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(-1,4,-1));
//        	world.setBlockState(new BlockPos(prev_food_x,4,prev_food_z), state_wood);
//        	world.setBlockState(new BlockPos(food_x,4,food_z), state_red);
        	
        	prev_food_x = food_x;
        	prev_food_z = food_z;
        	
        }
        
        // Looking
        if (Keybinds.up.isPressed()) {
//			player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        	
        	action = 7;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.down.isPressed()) {
//			player.setLocationAndAngles(player.posX - 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			
        	action = 8;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.left.isPressed()) {
//			player.setLocationAndAngles(player.posX, player.posY, player.posZ - 1.0, player.rotationYaw, player.rotationPitch);
			
//			
        	action = 9;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.right.isPressed()) {
//			player.setLocationAndAngles(player.posX, player.posY, player.posZ + 1.0, player.rotationYaw, player.rotationPitch);
			
        	action = 10;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
		}
        
        //Absolute Movement
        
        // Key Y
        if (Keybinds.north.isPressed()) {
        	action = 0;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key H
        else if (Keybinds.south.isPressed()) { // Key H
        	action = 1;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key G
        else if (Keybinds.west.isPressed()) {
        	action = 2;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key J
        else if (Keybinds.east.isPressed()) {
        	action = 3;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key M
        else if (Keybinds.jumpForward.isPressed()) {
        	action = 4;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        
        //Other actions
        
        // Key U
        if (Keybinds.breakBlock.isPressed()) {
        	action = 5;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key I
        else if (Keybinds.placeBlock.isPressed()) {
        	action = 6;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
        // Key N
        else if (Keybinds.reset.isPressed()) {
        	action = 12;
        	isPerformingAction = true;
        	act.setPlayerPos(player.posX, player.posY, player.posZ);
        	act.setPlayerAngles(player.rotationYaw, player.rotationPitch);
        }
    }

	
	/**
	 * Super Jump if toggled
	 * Just for fun :)
	 * @param event
	 */
	
//	@SubscribeEvent
//	public void superJump(LivingJumpEvent event) {
////		System.out.println("Some event called; is this the client side? " + event.getEntity().world.isRemote);
//		
//		if (event.getEntity() instanceof EntityPlayer && toggled) {
//			EntityPlayer player = (EntityPlayer) event.getEntity();
//			player.motionY += 1.2D;
//		}
//	}
	
	/**Network event
	 * 
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
	
	// Used to set the server for the synchronized block to used same lock (wait and notify) -> NOT WORKING!!!
	public void getServer(Server s) {
		server = s;
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
 * */
