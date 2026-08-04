package com.bedsbutendgame;

import com.bedsbutendgame.block.ModBlocks;
import com.bedsbutendgame.command.BbeCommands;
import com.bedsbutendgame.config.ConfigManager;
import com.bedsbutendgame.network.ConfigNetworking;
import com.bedsbutendgame.sleep.NightmareManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BedsButEndgame implements ModInitializer {
	public static final String MOD_ID = "bedsbutendgame";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ConfigManager.load();
		ModBlocks.initialize();
		ConfigNetworking.initialize();
		BbeCommands.initialize();
		NightmareManager.initialize();
		LOGGER.info("Beds, but Endgame initialized");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
