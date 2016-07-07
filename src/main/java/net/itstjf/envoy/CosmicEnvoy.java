package net.itstjf.envoy;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

// 10 Min - Yellow
// 20 Min - Red

@Mod( modid 			= CosmicEnvoy.MODID
	, version 			= CosmicEnvoy.VERSION
	, name 				= CosmicEnvoy.NAME
	, clientSideOnly 	= true
	, canBeDeactivated 	= true
)
public class CosmicEnvoy {
	//Sets instance to this.
	@Instance(CosmicEnvoy.MODID)
	public static CosmicEnvoy instance;
	
	KeyBinding key_gui 		= new KeyBinding("Gui for Envoy timer", Keyboard.KEY_R, "CosmicPVP");
	public long lastEnvoy 	= 0;
	public String name 		= "Last Envoy: ";
	String envoyMsg 		= "A Cosmic Envoy is nearby, supply crates can be seen falling over the Warzone!";
	
	String lastMsg = "";
	
	//Forge stuffs
	public static final String NAME 		= "CosmicPVP Envoy Timer";
	public static final String MODID 		= "CosmicEnvoy";
	public static final String VERSION 		= "1.1";
	public static final String CLIENT_PROXY = "net.itstjf.envoy.ClientProxy";
	
	@SidedProxy(clientSide = CLIENT_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		FMLCommonHandler.instance().bus().register(this);
		proxy.preInit(new File(event.getModConfigurationDirectory(), "CosmicEnvoy.cfg"));
		ClientRegistry.registerKeyBinding(this.key_gui);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.load();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post event) {
		if (event.type != RenderGameOverlayEvent.ElementType.ALL || !GuiSomething.showIngame) return;
		
		int color = 0xffffffff;
		
		//If time equals 0
		if (this.lastEnvoy == 0) {
			this.name = "Last Envoy: Unknown";
		} else {
			int time 		= (int)((System.currentTimeMillis() - this.lastEnvoy)/1000);
			int seconds 	= time % 60;
		    int totalMinutes= time / 60;
		    int minutes 	= totalMinutes % 600;
		    int hours 		= totalMinutes / 600;
		    
		    
		    this.name = "Last Envoy: " + (hours > 0 ? hours + "h, " : "") + (minutes > 0 ? minutes + "m, " : "") + (seconds > 0 ? seconds + "s ago" : "0s ago");
			
		    //1h
			if (time > 3600) {
				this.name = "Last Envoy: Unknown";
			} else
			//20m
			if (time > 1200) {
				color = 0xffff0000;
			} else
			//10m
			if (time > 600) {
				color = 0xffffff00;
			}
		}
		
		Minecraft.getMinecraft().fontRendererObj.drawString(this.name, GuiSomething.x, GuiSomething.y, color);
	}
	
	@SubscribeEvent
	public void onClientChat(ClientChatReceivedEvent event) {
		String message = event.message.getUnformattedText();
		//When envoy happens.
		if (message.equals(envoyMsg)) {
			this.lastEnvoy = System.currentTimeMillis();
		} else
		//When you do /envoy
		if (this.lastMsg.startsWith("(!) PREVIOUS COSMIC ENVOY:")) {
			try {
				Matcher match = Pattern.compile("([0-9]+)m ([0-9]+)s").matcher(message);
				
				if (match.find()) {
					int mMilli = Integer.parseInt(match.group(1)) * 60 * 1000;
					int sMilli = Integer.parseInt(match.group(2)) * 1000;
					this.lastEnvoy = System.currentTimeMillis() - (mMilli + sMilli);
				}
			}
			catch(NumberFormatException e) {}
			catch(PatternSyntaxException e) {}
		}
		
		//Sets the last message; used for /envoy.
		this.lastMsg = message.trim();
	}
	
	@SubscribeEvent
	public void keyEvent(InputEvent.KeyInputEvent event) {
		//Displays gui when you press the keybind.
		if (this.key_gui.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiSomething());
		}
	}
}