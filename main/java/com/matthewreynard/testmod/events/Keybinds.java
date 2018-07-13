package com.matthewreynard.testmod.events;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {
	
	public static KeyBinding toggle; //v
	public static KeyBinding print; //r 
	public static KeyBinding state; //b
	
	//Looking
	public static KeyBinding up;
	public static KeyBinding down;
	public static KeyBinding left;
	public static KeyBinding right;
	
	//Absolute movement
	public static KeyBinding north;
	public static KeyBinding south;
	public static KeyBinding west;
	public static KeyBinding east;
	
	//Other actions
	public static KeyBinding jumpForward;
	public static KeyBinding breakBlock;
	public static KeyBinding placeBlock;
	public static KeyBinding reset;
	 
    public static void register()
    {
        toggle = new KeyBinding("Toggle", Keyboard.KEY_V, "Mod Keys");
        print = new KeyBinding("Print stuff", Keyboard.KEY_R, "Mod Keys");
        state = new KeyBinding("Send state", Keyboard.KEY_B, "Mod Keys");
        
        up = new KeyBinding("Snake_UP", Keyboard.KEY_UP, "Mod Keys");
        down = new KeyBinding("Snake_DOWN", Keyboard.KEY_DOWN, "Mod Keys");
        left = new KeyBinding("Snake_LEFT", Keyboard.KEY_LEFT, "Mod Keys");
        right = new KeyBinding("Snake_RIGHT", Keyboard.KEY_RIGHT, "Mod Keys");
        
        north = new KeyBinding("Move_Up", Keyboard.KEY_Y, "Complex actions");
        south = new KeyBinding("Move_Down", Keyboard.KEY_H, "Complex actions");
        west = new KeyBinding("Move_Left", Keyboard.KEY_G, "Complex actions");
        east = new KeyBinding("Move_Right", Keyboard.KEY_J, "Complex actions");
        
        jumpForward = new KeyBinding("Jump_Forward", Keyboard.KEY_M, "Complex actions");
        breakBlock = new KeyBinding("Break_Block", Keyboard.KEY_U, "Complex actions");
        placeBlock = new KeyBinding("Place_Block", Keyboard.KEY_I, "Complex actions");
        reset = new KeyBinding("Face_North", Keyboard.KEY_N, "Complex actions");
 
        ClientRegistry.registerKeyBinding(toggle);
        ClientRegistry.registerKeyBinding(print);
        ClientRegistry.registerKeyBinding(state);
        
        ClientRegistry.registerKeyBinding(up);
        ClientRegistry.registerKeyBinding(down);
        ClientRegistry.registerKeyBinding(left);
        ClientRegistry.registerKeyBinding(right);
        
        ClientRegistry.registerKeyBinding(north);
        ClientRegistry.registerKeyBinding(south);
        ClientRegistry.registerKeyBinding(west);
        ClientRegistry.registerKeyBinding(east);
        
        ClientRegistry.registerKeyBinding(jumpForward);
        ClientRegistry.registerKeyBinding(breakBlock);
        ClientRegistry.registerKeyBinding(placeBlock);
        ClientRegistry.registerKeyBinding(reset);
        
        
    }
}
