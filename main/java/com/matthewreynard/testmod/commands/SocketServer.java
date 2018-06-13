package com.matthewreynard.testmod.commands;

import java.util.ArrayList;
import java.util.List;

import com.matthewreynard.testmod.network.Server;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SocketServer implements ICommand {
	
	Server s = new Server();

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		
		return "server";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "server <args> (start, stop)";
	}

	@Override
	public List<String> getAliases() {
		List<String> commandAliases = new ArrayList();
		commandAliases.add("myserver");
		commandAliases.add("socket");
		return commandAliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ITextComponent msg = new TextComponentString("Server: ");
			
			Style style = new Style();
			style.setColor(TextFormatting.BLUE);
			msg.setStyle(style);

//			Server s = new Server();
			
			if (args.length == 0) {
				player.sendMessage(msg.appendText("start or stop ???"));
			}
			else if (args[0].equals("start") && !s.isOpen()) {
				player.sendMessage(msg.appendText("started"));
				s.start(); 
			}
			else if (args[0].equals("stop") && s.isOpen()) {
				player.sendMessage(msg.appendText("stopped"));
				s.closeConnection();
			}
			
		}

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
	
	public Server getServer() {
		return s;
	}
	
//	public Server setServer() {
//		return s;
//	}

}
