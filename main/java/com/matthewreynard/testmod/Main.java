package com.matthewreynard.testmod;

import org.lwjgl.input.Keyboard;

import com.matthewreynard.testmod.commands.SocketServer;
import com.matthewreynard.testmod.events.Keybinds;
import com.matthewreynard.testmod.events.TestEventHandler;
import com.matthewreynard.testmod.events.TestFMLEventHandler;
import com.matthewreynard.testmod.network.NetworkHandler;
import com.matthewreynard.testmod.network.Server;
import com.matthewreynard.testmod.proxy.CommonProxy;
import com.matthewreynard.testmod.util.Reference;

import jline.internal.Log;
//import net.java.games.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	
//	Server s;
	static SocketServer s;
	
	static TestEventHandler teh;
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		Keybinds.register();
		NetworkHandler.init();
		Log.info("Pre Initialization Complete!");
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		
//		System.out.println("My mod has been initialized.");
		
		Log.info("Initialization Complete!");
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		
		teh = new TestEventHandler();
		
		MinecraftForge.EVENT_BUS.register(teh);
		
//		FMLCommonHandler.instance().bus().register(new TestFMLEventHandler());
		
		Log.info("Post Initialization Complete!");
	}
	
	// Commands
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		s = new SocketServer();
		
		event.registerServerCommand(s);
		
		teh.getServer(s.getServer());
	}

}
