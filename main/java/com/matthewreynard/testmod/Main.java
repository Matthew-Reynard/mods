package com.matthewreynard.testmod;

import org.lwjgl.input.Keyboard;

import com.matthewreynard.testmod.events.Keybinds;
import com.matthewreynard.testmod.events.TestEventHandler;
import com.matthewreynard.testmod.events.TestFMLEventHandler;
import com.matthewreynard.testmod.proxy.CommonProxy;
import com.matthewreynard.testmod.util.Reference;

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

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		Keybinds.register();
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new TestEventHandler());
		
//		FMLCommonHandler.instance().bus().register(new TestFMLEventHandler());
	}

}
