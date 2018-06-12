package com.matthewreynard.testmod.tickrate;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
//import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeTickRate implements IFMLLoadingPlugin, IFMLCallHook {
    
	// Stored client-side tickrate (default at 20TPS)
    private Field clientTimer = null;
    
    @SideOnly(Side.CLIENT)
    public void updateClientTickrate(float tickrate) {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc == null) return; // Oops! Try again!
        try {
            if(clientTimer == null) {
                for(Field f : mc.getClass().getDeclaredFields()) {
                    if(f.getType() == Timer.class) {
                        clientTimer = f;
                        clientTimer.setAccessible(true);
                        break;
                    }
                }
            }
            clientTimer.set(mc, new Timer(tickrate));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
	@Override
	public Void call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}
}