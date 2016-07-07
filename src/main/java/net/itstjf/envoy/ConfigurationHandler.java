package net.itstjf.envoy;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {
	public static Configuration configuration;

	public static void init(File configFile) {
		if (configuration == null) {
			configuration = new Configuration(configFile);
			loadConfig();
		}
	}

	public static void loadConfig() {
		GuiSomething.x 			= configuration.getInt("x", "options", 2, 0, 9999, "x location of text");
		GuiSomething.y 			= configuration.getInt("y", "options", 2, 0, 9999, "y location of text");
		GuiSomething.showIngame = configuration.getBoolean("showIngame", "options", true, "Show text overlay?");

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(CosmicEnvoy.MODID)) {
			loadConfig();
		}
	}
}