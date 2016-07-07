package net.itstjf.envoy;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(File configFile) {
		ConfigurationHandler.init(configFile);
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
	}
	
	@Override
	public void load() {
		MinecraftForge.EVENT_BUS.register(CosmicEnvoy.instance);
		FMLCommonHandler.instance().bus().register(CosmicEnvoy.instance);
	}
	
	@Override
	public void postInit() {}
}