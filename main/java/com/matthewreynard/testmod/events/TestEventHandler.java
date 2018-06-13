package com.matthewreynard.testmod.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Util.EnumOS;
import net.minecraft.util.math.BlockPos;
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

import com.matthewreynard.testmod.network.Server;
import com.matthewreynard.testmod.tickrate.ChangeTickRate;

import io.netty.channel.ChannelPipeline;

public class TestEventHandler {
	
	public static boolean toggled = false;
	public static boolean tickchange = false;
	public static boolean waiting = false;
	
	public static int r = 0;
	public static int action = 0;
	
	public static int food_x = 0;
	public static int food_z = 0;
	
	public static int prev_food_x = 0;
	public static int prev_food_z = 0;
	
	public static Random rnd = new Random();
	
	public static Server server;
	
	@SubscribeEvent
	public void test1(WorldTickEvent event)
	{
		if (toggled) {
			server.setmcServer(this);
			
			if (waiting) {
			
				synchronized (this) {
					try {
						System.out.println("Thread is running");
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void test(LivingUpdateEvent event)
	{
		
		//do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack heldItem = player.getHeldItemMainhand();
			
			Minecraft mc = Minecraft.getMinecraft();
			
			// Player is invincible
			player.capabilities.disableDamage = true;
			
//			player.capabilities.setPlayerWalkSpeed(1.0f);
			
//			player.setInvisible(true);
			
			if(!mc.world.isRemote && !waiting) {
				System.out.println("It is no waiting...");
			}
			
			
//			if (toggled) {
//				
//				// Is always 0 (therefore not on server?)
////				System.out.println("\nserverX:" + player.serverPosX + "\t serverY:" + player.serverPosY + "\t serverZ:" + player.serverPosZ); 
//				
//				// TEST
//				// Used to control player movement
//				if(mc.world.getWorldTime() % 10 == 0) {
//					
////					server.setmcServer(this);
////					
////					if (waiting) {
////					
////						synchronized (this) {
////							try {
////								System.out.println("Thread is running");
////								wait();
////							} catch (InterruptedException e) {
////								// TODO Auto-generated catch block
////								e.printStackTrace();
////							}
////						}
////					}
//					
//					System.out.println("Exited the loop");
//					
////					r = rnd.nextInt(40);
//					action = Server.getAction();
////					action = 5;
//					
//					if (player.world.isRemote) {
//						switch(action) {
//							case 0: //up
//								System.out.println("\nAction: "+r+" -> UP");
////								player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
////								player.move(MoverType.SELF, player.posX + 1.0, player.posY, player.posZ);
////								player.moveRelative(0.0f, 0, 1.0f, 0.5f);
////								player.setLocationAndAngles(player.posX - 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
//								player.setVelocity(-0.05f, 0.0f, 0.0f);
//								break;
//							
//							case 1: //down
//								System.out.println("\nAction: "+r+" -> DOWN"); 
////								player.setLocationAndAngles(player.posX - 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
////								player.moveRelative(0.0f, 0, -1.0f, 0.5f);
////								player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
//								player.setVelocity(0.05f, 0.0f, 0.0f);
//								break;
//							
//							case 2: //left
//								System.out.println("\nAction: "+r+" -> LEFT");
////								player.setLocationAndAngles(player.posX, player.posY, player.posZ - 1.0f, player.rotationYaw, player.rotationPitch);
////								player.moveRelative(1.0f, 0, 0.0f, 0.5f);
////								player.setLocationAndAngles(player.posX, player.posY, player.posZ - 1.0, player.rotationYaw, player.rotationPitch);
//								player.setVelocity(0.0f, 0.0f, -0.05f);
//								break;
//							
//							case 3: //right
//								System.out.println("\nAction: "+r+" -> RIGHT");
////								player.setLocationAndAngles(player.posX, player.posY, player.posZ + 1.0f, player.rotationYaw, player.rotationPitch);
////								player.moveRelative(-1.0f, 0, 0.0f, 0.5f);
////								player.setLocationAndAngles(player.posX, player.posY, player.posZ + 1.0, player.rotationYaw, player.rotationPitch);
//								player.setVelocity(0.0f, 0.0f, 0.05f);
//								break;
//							
//							default: 
//								System.out.println("Invalid action");
////								player.setVelocity(0.2, 0.0, 0.0);
////								player.travel(0.0f, 0.0f, 1.0f);
////								System.out.println(player.getDataManager().toString());
//						}
//					}
//					
//					float[] x = new float[4];
//					
//					x[0]= (float)Math.floor(mc.player.posZ);
//					x[1]= (float)Math.floor(mc.player.posX);
//					x[2]= (float)food_z;
//					x[3]= (float)food_x;
//					
//					Server.setState(x);
//					
//					if (Math.floor(player.posX) == food_x && Math.floor(player.posZ) == food_z) {
//						food_x = rnd.nextInt(10);
//			        	food_z = rnd.nextInt(10);
//			        	
//			        	IBlockState state_red = mc.world.getBlockState(new BlockPos(0,99,0));
//			        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(0,98,0));
//			        	
////			        	IBlockState state_red = mc.world.getBlockState(new BlockPos(-1,5,-1));
////			        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(-1,4,-1));
//			        	
//			        	mc.world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
//			        	mc.world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
//			        	
//			        	prev_food_x = food_x;
//			        	prev_food_z = food_z;
//					}
//					
////					player.turn(300, 0); // (0.15/360)*300
//					
////					if (player.onGround) {
////						System.out.println("\n\nplayer is on the ground\n");
////					}
////					else if (player.isAirBorne) {
////						System.out.print("\n\nPlayer is air borne\n");
////					}
//					
////					player.travel(0.0f, 0.0f, 1.0f);
////					player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
//				}
////				if(mc.world.getWorldTime() % 20 == 5) {
////					player.travel(10.0f, 0.0f, 0.0f);
////				}
////				if(mc.world.getWorldTime() % 20 == 10) {
////					player.travel(0.0f, 0.0f, -10.0f);
////				}
////				if(mc.world.getWorldTime() % 20 == 15) {
////					player.travel(-10.0f, 0.0f, 0.0f);
////				}
//							
//				if (heldItem != null && heldItem.isEmpty()) {
//					player.capabilities.allowFlying = true;
////					player.capabilities.setPlayerWalkSpeed(1.0f); //super speed
//					
////					mc.gameSettings.setOptionValue(Options.AUTO_JUMP, 1); //off
////					mc.gameSettings.setOptionFloatValue(Options.FOV, 70.0f);
//					
//				}
//
//			}
//			else {
////				player.capabilities.setPlayerWalkSpeed(0.1f); //0.2 = walk speed ; 0.1 = sneak speed
////				mc.gameSettings.setOptionFloatValue(Options.FOV, 70.0f);
////				mc.gameSettings.setOptionValue(Options.AUTO_JUMP, 0); //on
////				player.capabilities.allowFlying = false;
//				player.capabilities.allowFlying = player.capabilities.isCreativeMode ? true : false;
//			}
		}
	}
	
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
		
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.world;
		MinecraftServer mcServer = mc.player.getServer();
		
		float[] x = new float[4];
		
		x[0]= (float)Math.floor(mc.player.posZ);
		x[1]= (float)Math.floor(mc.player.posX);
		x[2]= (float)food_z;
		x[3]= (float)food_x;
		
		
        if (Keybinds.toggle.isPressed())
        {
        	toggled = !toggled;
            
        	if (toggled) {
        		Minecraft.getMinecraft().player.sendChatMessage("Mod ON");	
        	}
        	if (!toggled) {
        		Minecraft.getMinecraft().player.sendChatMessage("Mod OFF");
        	}
        	
        }
        
//        if (Keybinds.state.isPressed()) {
        	
//        	ChangeTickRate change = new ChangeTickRate();
//        	if (tickchange) {
//        		change.updateClientTickrate(1000);
//        		System.out.println("Tickrate changed to 1000");
//        		tickchange = !tickchange;
//        	}
//        	else {
//        		change.updateClientTickrate(20);
//        		System.out.println("Tickrate changed to 20");
//        		tickchange = !tickchange;
//        	}
        	
        
        if (Keybinds.print.isPressed())
        {
        	waiting = !waiting;
        	
        	System.out.println("Waiting " + waiting);
        	
//        	if(!world.isRemote) {
//	        	try {
//					mc.getIntegratedServer().wait(1000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//        	}
//        	else {
//        		System.out.println("Integrated server is null");
//        	}
//        	
//        	server.setmcServer(mc);
        	
//        	synchronized (server){
//        		System.out.println("Thread is running");
//        		try {
//        			net.minecraft.server.integrated.IntegratedServer.class.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//        		System.out.println("Resumed");
//        	}
        }
        
        if (Keybinds.state.isPressed())
        {
        	
        	food_x = rnd.nextInt(10);
        	food_z = rnd.nextInt(10);

        	Server.setState(x);
        	
//        	Server.send();        	
        	//world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(Blah item here)));
//        	world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Item().getItemById(371), 1)));
//        	world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Block(), 1)));
//        	world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Block(Material.WOOD).getBlockById(5), 1)));
//        	world.spawnEntity(new EntityItem(world, 0, 5, 4, new ItemStack(new ItemBlock(new Block(Material.WOOD)))));
//        	ItemBlock b = new ItemBlock(new Block(Material.WOOD).spawnAsEntity(world, new BlockPos(0, 0, 0), new ItemStack());
//        	world.getBlockState(new BlockPos(0,5,0)).getBlock().setLightLevel(0);
        	
        	IBlockState state_red = world.getBlockState(new BlockPos(0,99,0));
        	IBlockState state_wood = world.getBlockState(new BlockPos(0,98,0));
        	
//        	IBlockState state_red = mc.world.getBlockState(new BlockPos(-1,5,-1));
//        	IBlockState state_wood = mc.world.getBlockState(new BlockPos(-1,4,-1));
        	
//        	world.setBlockToAir(new BlockPos(0,5,0));
        	
        	world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
        	world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
        	
        	prev_food_x = food_x;
        	prev_food_z = food_z;
        	
//        	System.out.println(Item.REGISTRY.getKeys());
        }
    }
	
	
//	@SubscribeEvent
//    public void printStuff(KeyInputEvent event) throws IOException
//    {
//        if (Keybinds.print.isPressed())
//        {
////        	Minecraft mc = Minecraft.getMinecraft();
////        	EntityPlayer player = mc.player;
////        	
////        	
////        	System.out.println("\n\nChunks -> X: " + player.chunkCoordX + "\tY: " +  player.chunkCoordY + "\tZ: " + player.chunkCoordZ +"\n");
////			
////			
////			if (player.collided) {
////				System.out.println("\n\nThe player has collided with something\n");
////			}
////			
////			// Print out the coordinates of agent
////			System.out.println("\n\nx:" + player.posX + "\t y:" + player.posY + "\t z:" + player.posZ + "\n");
//        	
//        	Server server = new Server();
//        	
//        	server.start();
//        	
//        }
//    }
//	
//	@SubscribeEvent
//    public void sendState(KeyInputEvent event)
//    {
//		
//		Minecraft mc = Minecraft.getMinecraft();
//		
//		World world = mc.world;
//		
//		float[] x = new float[4];
////		x[0]= (float)mc.player.posX;
////		x[1]= (float)mc.player.posY;
////		x[2]= (float)mc.player.posZ;
//		
//		x[0]= (float)Math.floor(mc.player.posZ);
//		x[1]= (float)Math.floor(mc.player.posX);
//		x[2]= (float)food_z;
//		x[3]= (float)food_x;
//		
////		float x1 = 0.0f;
////		float y1 = 0.0f;
////		float z1 = 0.0f;
//        
//		if (Keybinds.state.isPressed())
//        {
//        	
//        	food_x = rnd.nextInt(10);
//        	food_z = rnd.nextInt(10);
//
//        	Server.setState(x);
//        	
////        	Server.send();        	
//        	//world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(Blah item here)));
////        	world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Item().getItemById(371), 1)));
////        	world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Block(), 1)));
////        	world.spawnEntity(new EntityItem(world, 0, 5, 0, new ItemStack(new Block(Material.WOOD).getBlockById(5), 1)));
////        	world.spawnEntity(new EntityItem(world, 0, 5, 4, new ItemStack(new ItemBlock(new Block(Material.WOOD)))));
////        	ItemBlock b = new ItemBlock(new Block(Material.WOOD).spawnAsEntity(world, new BlockPos(0, 0, 0), new ItemStack());
////        	world.getBlockState(new BlockPos(0,5,0)).getBlock().setLightLevel(0);
//        	IBlockState state_red = world.getBlockState(new BlockPos(0,99,0));
//        	IBlockState state_wood = world.getBlockState(new BlockPos(0,98,0));
//        	
////        	world.setBlockToAir(new BlockPos(0,5,0));
//        	
//        	world.setBlockState(new BlockPos(prev_food_x,100,prev_food_z), state_wood);
//        	world.setBlockState(new BlockPos(food_x,100,food_z), state_red);
//        	
//        	prev_food_x = food_x;
//        	prev_food_z = food_z;
//        	
////        	System.out.println(Item.REGISTRY.getKeys());
//        }
//    }
	
	@SubscribeEvent
    public void snakeMove(KeyInputEvent event)
    {
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		World world = mc.world;
		
		if (Keybinds.up.isPressed()) {
//			if(player.isSwingInProgress) {
//				System.out.println("Arm swing is in progress");
//			}
//			if(player.onGround) {
//				player.jump();				
//			}
//			player.knockBack(player, 1.0f, 1.0, 1.0);
			
//			player.swingArm(EnumHand.MAIN_HAND);
			
//			server.setmcServer(player.getServer().getClass());
			
			
//			System.out.println(player.getServer().worlds.toString());
			
//			synchronized (player.getServer().getClass()) {
//				try {
//					System.out.println("Thread is running");
//					player.getServer().getClass().wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
				
			
//			player.setLocationAndAngles(player.posX + 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.down.isPressed()) {
			player.setLocationAndAngles(player.posX - 1.0, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.left.isPressed()) {
			player.setLocationAndAngles(player.posX, player.posY, player.posZ - 1.0, player.rotationYaw, player.rotationPitch);
		}
		else if (Keybinds.right.isPressed()) {
			player.setLocationAndAngles(player.posX, player.posY, player.posZ + 1.0, player.rotationYaw, player.rotationPitch);
		}
		
		// simulating eating food
//		if (Math.floor(player.posX) == food_x && Math.floor(player.posZ) == food_z) {
//			food_x = rnd.nextInt(10);
//        	food_z = rnd.nextInt(10);
//        	
//        	IBlockState state_red = world.getBlockState(new BlockPos(-1,5,-1));
//        	IBlockState state_wood = world.getBlockState(new BlockPos(-1,4,-1));
//        	
//        	world.setBlockState(new BlockPos(prev_food_x,4,prev_food_z), state_wood);
//        	world.setBlockState(new BlockPos(food_x,4,food_z), state_red);
//        	
//        	prev_food_x = food_x;
//        	prev_food_z = food_z;
//		}
		
		
    }
	
	
	@SubscribeEvent
	public void superJump(LivingJumpEvent event) {
//		System.out.println("Some event called; is this the client side? " + event.getEntity().world.isRemote);
		
		if (event.getEntity() instanceof EntityPlayer && toggled) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			player.motionY += 1.2D;
		}
	}
	
	
	@SubscribeEvent
    public void networkStuff(ServerConnectionFromClientEvent event)
    {
		
		System.out.println("\n\nServerConnectionFromClientEvent was successful\n");
		
//		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
//		
//		server.isSinglePlayer();
//		
//		ChannelPipeline network = event.getManager().channel().pipeline();
//		System.out.print("Pipeline: " + network.toString());
		
    }
	
	public void getServer(Server s) {
		server = s;
	}

}
