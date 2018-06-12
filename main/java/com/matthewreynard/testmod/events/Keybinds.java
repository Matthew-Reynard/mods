package com.matthewreynard.testmod.events;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {
	
	public static KeyBinding toggle; //v
	public static KeyBinding print; //r 
	public static KeyBinding state; //b
	public static KeyBinding up;
	public static KeyBinding down;
	public static KeyBinding left;
	public static KeyBinding right;
	 
    public static void register()
    {
        toggle = new KeyBinding("Toggle", Keyboard.KEY_V, "Mod Keys");
        print = new KeyBinding("Print stuff", Keyboard.KEY_R, "Mod Keys");
        state = new KeyBinding("Send state", Keyboard.KEY_B, "Mod Keys");
        up = new KeyBinding("Snake_UP", Keyboard.KEY_UP, "Mod Keys");
        down = new KeyBinding("Snake_DOWN", Keyboard.KEY_DOWN, "Mod Keys");
        left = new KeyBinding("Snake_LEFT", Keyboard.KEY_LEFT, "Mod Keys");
        right = new KeyBinding("Snake_RIGHT", Keyboard.KEY_RIGHT, "Mod Keys");
 
        ClientRegistry.registerKeyBinding(toggle);
        ClientRegistry.registerKeyBinding(print);
        ClientRegistry.registerKeyBinding(state);
        ClientRegistry.registerKeyBinding(up);
        ClientRegistry.registerKeyBinding(down);
        ClientRegistry.registerKeyBinding(left);
        ClientRegistry.registerKeyBinding(right);
        
    }
}
