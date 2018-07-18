package com.matthewreynard.testmod.network;

import io.netty.buffer.ByteBuf;
import jline.internal.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MessageExplode extends MessageBase<MessageExplode> {
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
	}

	@Override
	public void handleClientSide(MessageExplode message, EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(MessageExplode message, EntityPlayer player) {
//		player.world.createExplosion(player, player.posX, player.posY-10, player.posZ, 3.0F, true);
		
//		try {
//			Minecraft.getMinecraft().wait(10);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		Log.info(player.getServer().isSinglePlayer()); 
//		Log.info(Thread.sleep(millis););
		try {
			System.out.println("start");
			Thread.sleep(1000);
			System.out.println("finish");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
